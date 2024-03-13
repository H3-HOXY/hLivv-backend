package hoxy.hLivv.backoffice.controller;


import hoxy.hLivv.dto.member.LoginDto;
import hoxy.hLivv.dto.member.MemberDto;
import hoxy.hLivv.dto.member.SignupDto;
import hoxy.hLivv.dto.order.MonthlyOrderSummaryDto;
import hoxy.hLivv.dto.product.ProductDto;
import hoxy.hLivv.dto.restore.RestoreDto;
import hoxy.hLivv.entity.enums.RestoreProductStatus;
import hoxy.hLivv.entity.enums.RestoreStatus;
import hoxy.hLivv.jwt.JwtFilter;
import hoxy.hLivv.jwt.TokenProvider;
import hoxy.hLivv.service.MemberService;
import hoxy.hLivv.service.OrderService;
import hoxy.hLivv.service.ProductService;
import hoxy.hLivv.service.RestoreService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

@Controller
@RequestMapping("/backoffice")
@Slf4j
@RequiredArgsConstructor
public class BackofficeController {

    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final MemberService memberService;
    private final RestoreService restoreService;
    private final ProductService productService;
    private final OrderService orderService;

    NumberFormat formatter = NumberFormat.getNumberInstance(Locale.KOREA);

    @GetMapping("/register")
    public String signupPage() {
        return "backoffice/register";
    }

    @PostMapping("/register")
    public void signup(
            @Valid @RequestBody SignupDto signupDto
    ) {
        memberService.signupAdmin(signupDto);
//        return ResponseEntity.ok();
    }

    @GetMapping("/login")
    public String login() {
        return "backoffice/login";
    }

    @PostMapping("/login")
    public void authorize(@Valid @RequestBody LoginDto loginDto,
                          HttpServletResponse response) {
        log.info(loginDto.toString());
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getLoginId(), loginDto.getLoginPw());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.createToken(authentication);

        // 쿠키에 JWT 추가
        Cookie cookie = new Cookie(JwtFilter.AUTHORIZATION_HEADER, jwt);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(24 * 60 * 60); // 쿠키 유효 시간 설정 (24시간)
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    @PostMapping("/logout")
    public void logout(HttpServletResponse response) {
        SecurityContextHolder.clearContext();

        Cookie cookie = new Cookie(JwtFilter.AUTHORIZATION_HEADER, null);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0); // 쿠키 만료
        response.addCookie(cookie);

    }

    @GetMapping("/home")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public String home(Model model) {
        List<MonthlyOrderSummaryDto> monthlyOrder = orderService.getMonthlyOrder();
        MonthlyOrderSummaryDto todayOrder = orderService.getTodayOrder();
        Long annualEarning = 0L;
        Long annualOrderCnt = 0L;
        Long monthlyEarning = 0L;
        Long monthlyOrderCnt = 0L;
        Long todayEarning = 0L;
        Long todayOrderCnt = 0L;

        if (!monthlyOrder.isEmpty()) {
            annualEarning = monthlyOrder.stream()
                    .mapToLong(MonthlyOrderSummaryDto::getOrderTotal)
                    .sum();
            annualOrderCnt = monthlyOrder.stream()
                    .mapToLong(MonthlyOrderSummaryDto::getCnt)
                    .sum();
            monthlyEarning = monthlyOrder.get(monthlyOrder.size() - 1).getOrderTotal();
            monthlyOrderCnt = monthlyOrder.get(monthlyOrder.size() - 1).getCnt();
        }
        if (todayOrder != null) {
            todayEarning = todayOrder.getOrderTotal();
            todayOrderCnt = todayOrder.getCnt();
        }

        String strAnnualEarning = formatter.format(annualEarning) + "원 / " + annualOrderCnt.toString() + "개";
        String strMonthlyEarning = formatter.format(monthlyEarning) + "원 / " + monthlyOrderCnt.toString() + "개";
        String strTodayEarning = formatter.format(todayEarning) + "원 / " + todayOrderCnt.toString() + "개";
        model.addAttribute("annualEarning", strAnnualEarning);
        model.addAttribute("monthlyEarning", strMonthlyEarning);
        model.addAttribute("todayEarning", strTodayEarning);

        String restoreInfo = restoreService.getRestoreByRestoreStatus(RestoreStatus.접수완료).toString() + " / "
                + restoreService.getRestoreByRestoreStatus(RestoreStatus.검수중).toString() + " / "
                + restoreService.getRestoreByRestoreStatus(RestoreStatus.리스토어완료).toString();

        model.addAttribute("restoreInfo", restoreInfo);

//        String memberInfo = memberService.getMemberGrade().stream().map(item -> String.join(item.getGrade()))
        return "backoffice/home";
    }

    @GetMapping("/members")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public String memberPage(Model model, @PageableDefault(size = 50) Pageable pageable) {
        model.addAttribute("members", memberService.getAllMembersWithPagination(pageable));
        return "backoffice/members";
    }

    @GetMapping("/auth")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public String auth() {
        return "backoffice/request_auth";
    }

    @GetMapping("/restores")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public String restores(Model model, @PageableDefault(size = 50, sort = "regDate", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<RestoreDto> allProductsWithPagination = restoreService.getAllProductsWithPagination(pageable);
        model.addAttribute("restores", allProductsWithPagination);
        return "backoffice/restore";
    }

    @GetMapping("/products")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public String products(Model model, @PageableDefault(size = 50) Pageable pageable) {
        model.addAttribute("products", productService.getAllProductsWithPagination(pageable));
        return "backoffice/products";
    }

    @PostMapping("/updateMember")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @ResponseBody
    public ResponseEntity<MemberDto> updateMember(@RequestBody MemberDto memberDto) {
        return ResponseEntity.ok(MemberDto.from(memberService.updateMember(memberDto)));
    }


    @GetMapping("/transfer")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public String transferPage(
            Model model,
            @RequestParam(required = true) Long restoreId
    ) {
        MemberDto memberDto = null;
        ProductDto productDto = null;
        RestoreDto restoreDto = null;
        if (restoreId != null) {
            restoreDto = restoreService.getRestore(restoreId);
            memberDto = memberService.getMemberById(restoreDto.getMemberId());
            productDto = productService.getProductWith(restoreDto.getProductId());

            model.addAttribute("memberDto", memberDto);
            model.addAttribute("productDto", productDto);
            model.addAttribute("restoreDto", restoreDto);
        }

        if (restoreDto != null) {
            String msg = restoreDto.getRejectMsg();
            if (msg == null) {
                msg = "";
            }
            RestoreProductStatus inspectedGrade = restoreDto.getInspectedGrade();
            String insGrade = "";
            if (inspectedGrade != null) {
                insGrade = inspectedGrade.name() + "등급";
            }
            String payback = "0";
            if (restoreDto.getPayback() != null) {
                payback = formatter.format(restoreDto.getPayback());
            }

            String inspectMessage = String.format("""
                    [H.Livv]
                    %s고객님 안녕하세요. 종합 인테리어 플랫폼 H.Livv 입니다.
                    신청하신 Re-Store 검수가 완료되었습니다.
                    
                    요청 상품명: %s
                    요청하신 등급: %s등급
                    검수등급: %s
                    적립 포인트: %s
                    검수 담당자 메세지:
                     - %s
                    
                    항상 H.Livv를 이용해주셔서 감사합니다.
                    """, memberDto.getName()
                    , productDto.getName()
                    , restoreDto.getRequestGrade()
                    , insGrade
                    , payback + "원"
                    , msg
            );

            model.addAttribute("inspectMessage", inspectMessage);
        }


        return "backoffice/transfer";
    }
}
