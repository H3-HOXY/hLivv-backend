package hoxy.hLivv.controller;

import hoxy.hLivv.dto.CartDto;
import hoxy.hLivv.dto.MemberCouponDto;
import hoxy.hLivv.dto.member.*;
import hoxy.hLivv.dto.order.OrderResDto;
import hoxy.hLivv.entity.Member;
import hoxy.hLivv.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
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

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
//@Tag(name = "회원 API", description = "회원 관리와 관련된 작업들")
public class MemberController {
    private final MemberService memberService;

    //@Operation(summary = "회원 가입")
    @PostMapping("/signup")
    public ResponseEntity<MemberDto> signup(@Valid @RequestBody SignupDto signupDto) {
        return ResponseEntity.ok(memberService.signup(signupDto));
    }

    //@Operation(summary = "회원 가입 데이터 생성")
    @PostMapping("/signup-data-gen")
    public ResponseEntity<String> signupDataGen(@Valid @RequestBody List<SignupDataGenDto> signupDataGenDtos) {
        memberService.signupDataGen(signupDataGenDtos);
        return ResponseEntity.ok("성공");
    }

    //@Operation(summary = "로그인 된 멤버 정보 조회", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/member")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<MemberDto> getMyUserInfo() {
        return ResponseEntity.ok(memberService.getMyMemberWithAuthorities());
    }

    //@Operation(summary = "특정 회원 정보 조회", security = @SecurityRequirement(name = "bearerAuth"))
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




    //@Operation(summary = "로그인 된 멤버 미사용 쿠폰 조회", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/member/coupons")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<Page<MemberCouponDto>> getUnusedCoupons(@RequestParam("page") int pageNo, @RequestParam("pageSize") int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.ASC, "expireDate"));
        Page<MemberCouponDto> coupons = memberService.getUnusedCoupons(pageable);
        return ResponseEntity.ok(coupons);
    }

    ////@Operation(summary = "로그인 된 멤버 장바구니 목록 조회", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("member/cart")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<Page<CartDto>> getCarts(@RequestParam("page") int pageNo, @RequestParam("pageSize") int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.DESC, "lastModifiedDate", "createdDate"));
        return ResponseEntity.ok(memberService.getCartsByMember(pageable));
    }

    //@Operation(summary = "로그인 된 멤버 주문 목록 조회", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("member/cart/all")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<List<CartDto>> getAllCarts() {
        return ResponseEntity.ok(memberService.getAllCartsByMember());
    }

    @GetMapping("member/order")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<Page<OrderResDto>> getOrders(@RequestParam("page") int pageNo, @RequestParam("pageSize") int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.ASC, "orderDate"));
        return ResponseEntity.ok(memberService.getOrdersByMember(pageable));
    }

    //@Operation(summary = "선택된 상품 목록 조회", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("member/cart/order")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<List<CartDto>> getSelectedItems(@RequestBody List<Long> productIds) {
        return ResponseEntity.ok(memberService.getSelectedItems(productIds));
    }

    //@Operation(summary = "회원 정보 업데이트", security = @SecurityRequirement(name = "bearerAuth"))
    @PutMapping("/updateMember")
    public ResponseEntity<MemberDto> updateMember(@Valid @RequestBody MemberDto memberDto) {
        Member updatedMember = memberService.updateMember(memberDto);
        return ResponseEntity.ok(MemberDto.from(updatedMember));
    }

    //@Operation(summary = "멤버 등급별 멤버 수 조회")
    @GetMapping("/member/grade")
    public ResponseEntity<List<MemberGradeDto>> getMemberGradeCnt() {
        return ResponseEntity.ok(memberService.getMemberGrade());
    }

    //@Operation(summary = "월별 회원 가입 수 조회")
    @GetMapping("/member/month/signup")
    public ResponseEntity<List<MonthlyMemberRegisterDto>> getMemberCntMonthly() {
        return ResponseEntity.ok(memberService.getMonthlyMemberRegi());
    }
}
