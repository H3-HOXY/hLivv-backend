package hoxy.hLivv.controller;

import hoxy.hLivv.dto.CartDto;
import hoxy.hLivv.dto.DeliveryResDto;
import hoxy.hLivv.dto.MemberCouponDto;
import hoxy.hLivv.dto.member.*;
import hoxy.hLivv.dto.order.OrderResDto;
import hoxy.hLivv.entity.Member;
import hoxy.hLivv.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 이상원, 반정현
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "회원 API", description = "회원 관리와 관련된 작업들")
public class MemberController {
    private final MemberService memberService;

    /**
     * @author 이상원
     */
    @Operation(summary = "회원 가입")
    @PostMapping("/signup")
    public ResponseEntity<MemberDto> signup(@Valid @RequestBody SignupDto signupDto) {
        return ResponseEntity.ok(memberService.signup(signupDto));
    }

    /**
     * @author 이상원
     */
    @Operation(summary = "회원 가입 데이터 생성")
    @PostMapping("/signup-data-gen")
    public ResponseEntity<String> signupDataGen(@Valid @RequestBody List<SignupDataGenDto> signupDataGenDtos) {
        memberService.signupDataGen(signupDataGenDtos);
        return ResponseEntity.ok("성공");
    }

    /**
     * @author 이상원
     */
    @Operation(summary = "로그인 된 멤버 정보 조회")
    @GetMapping("/member")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<MemberDto> getMyUserInfo() {
        return ResponseEntity.ok(memberService.getMyMemberWithAuthorities());
    }

    /**
     * @author 이상원
     */
    @Operation(summary = "특정 회원 정보 조회")
    @GetMapping("/member/{loginId}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<MemberDto> getUserInfo(@PathVariable String loginId) {
        return ResponseEntity.ok(memberService.getMemberWithAuthorities(loginId));
    }

    @GetMapping("/member/mypage")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<MemberResponseDto> getMyUserInfo(@AuthenticationPrincipal User user) {
        MemberDto member = memberService.getMemberWithAuthorities(user.getUsername());
        MemberResponseDto memberResponseDto = MemberResponseDto.from(member);
        return ResponseEntity.ok(memberResponseDto);
    }

    /**
     * @since 2024.02.14
     * @author 반정현
     */
    @Operation(summary = "로그인 된 멤버 미사용 쿠폰 조회")
    @GetMapping("/member/coupons")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<Page<MemberCouponDto>> getUnusedCoupons(@RequestParam("page") int pageNo,
                                                                  @RequestParam("pageSize") int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.ASC, "expireDate"));
        Page<MemberCouponDto> coupons = memberService.getUnusedCoupons(pageable);
        return ResponseEntity.ok(coupons);
    }

    /**
     * @since 2024.02.15
     * @author 반정현
     */
    @Operation(summary = "로그인 된 멤버 장바구니 목록 조회")
    @GetMapping("member/cart")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<Page<CartDto>> getCarts(@RequestParam("page") int pageNo,
                                                  @RequestParam("pageSize") int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize,
                                           Sort.by(Sort.Direction.DESC, "lastModifiedDate", "createdDate"));
        return ResponseEntity.ok(memberService.getCartsByMember(pageable));
    }

    /**
     * @since 2024.03.04
     * @author 반정현
     */
    @Operation(summary = "로그인 된 멤버 주문 목록 조회")
    @GetMapping("member/cart/all")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<List<CartDto>> getAllCarts() {
        return ResponseEntity.ok(memberService.getAllCartsByMember());
    }

    /**
     * @since 2024.02.22
     * @author 반정현
     */
    @Operation(summary = "전체 주문 목록 조회")
    @GetMapping("member/order")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<Page<OrderResDto>> getOrders(@RequestParam("page") int pageNo,
                                                       @RequestParam("pageSize") int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.ASC, "requestDate"));
        return ResponseEntity.ok(memberService.getOrdersByMember(pageable));
    }

    /**
     * @since 2024.02.22
     * @author 반정현
     */
    @Operation(summary = "선택된 상품 목록 조회")
    @PostMapping("member/cart/order")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<List<CartDto>> getSelectedItems(@RequestBody List<Long> productIds) {
        return ResponseEntity.ok(memberService.getSelectedItems(productIds));
    }

    @Operation(summary = "회원 정보 업데이트")
    @PutMapping("/updateMember")
    public ResponseEntity<MemberDto> updateMember(@Valid @RequestBody(required = true) MemberDto memberDto) {
        Member updatedMember = memberService.updateMember(memberDto);
        return ResponseEntity.ok(MemberDto.from(updatedMember));
    }

    @Operation(summary = "회원의 인테리어 취향 업데이트", description = "로그인된 회원의 인테리어 취향을 업데이트합니다.")
    @PostMapping("/member/season")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<SeasonDto> updateSeason(
            @Parameter(description = "사용자의 인테리어 취향에 해당하는 계절", example = "SPRING") @RequestBody(required = true) SeasonDto seasonDto) {
        Season season = memberService.updateSeason(seasonDto.getSeason());
        return ResponseEntity.ok(SeasonDto.from(season));
    }

    @Operation(summary = "멤버 등급별 멤버 수 조회")
    @GetMapping("/member/grade")
    public ResponseEntity<List<MemberGradeDto>> getMemberGradeCnt() {
        return ResponseEntity.ok(memberService.getMemberGrade());
    }

    @Operation(summary = "월별 회원 가입 수 조회")
    @GetMapping("/member/month/signup")
    public ResponseEntity<List<MonthlyMemberRegisterDto>> getMemberCntMonthly() {
        return ResponseEntity.ok(memberService.getMonthlyMemberRegi());
    }

    /**
     * @since 2024.03.08
     * @author 반정현
     */
    @Operation(summary = "배송 완료 목록 조회")
    @GetMapping("member/delivery/completed")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<Page<DeliveryResDto>> getCompletedDeliveries(@RequestParam("page") int pageNo,
                                                          @RequestParam("pageSize") int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.DESC, "order.requestDate"));
        return ResponseEntity.ok(memberService.findCompletedDeliveriesByMemberId(pageable));
    }

    /**
     * @since 2024.03.08
     * @author 반정현
     */
    @Operation(summary = "배송 중 목록 조회")
    @GetMapping("member/delivery/progress")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<Page<DeliveryResDto>> getProgressDeliveries(@RequestParam("page") int pageNo,
                                                                       @RequestParam("pageSize") int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.DESC, "order.requestDate"));
        return ResponseEntity.ok(memberService.findInProgressDeliveriesByMemberId(pageable));
    }

    /**
     * @since 2024.03.08
     * @author 반정현
     */
    @Operation(summary = "주문 조회")
    @GetMapping("member/order/{orderId}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<OrderResDto> getOrder(@PathVariable Long orderId){
        return ResponseEntity.ok(memberService.findOrderById(orderId));
    }

    /**
     * @since 2024.03.08
     * @author 반정현
     */
    @Operation(summary = "배송 조회")
    @GetMapping("member/delivery/{deliveryId}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<List<DeliveryResDto>> getDelivery(@PathVariable Long deliveryId){
        return ResponseEntity.ok(memberService.findDeliveryById(deliveryId));
    }

    /**
     * @since 2024.03.08
     * @author 반정현
     */
    @Operation(summary = "전체 배송 목록 조회")
    @GetMapping("member/delivery")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public  ResponseEntity<Page<DeliveryResDto>> getDeliveries(@RequestParam("page") int pageNo,
                                                               @RequestParam("pageSize") int pageSize){
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.DESC, "order.requestDate"));
        return ResponseEntity.ok(memberService.findDeliveriesByMemberId(pageable));
    }


}
