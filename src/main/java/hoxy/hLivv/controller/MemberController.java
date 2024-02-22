package hoxy.hLivv.controller;

import hoxy.hLivv.dto.*;
import hoxy.hLivv.dto.order.OrderProductReqDto;
import hoxy.hLivv.entity.Member;
import hoxy.hLivv.entity.compositekey.CartId;
import hoxy.hLivv.service.MemberService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

//    @GetMapping("/hello")
//    public ResponseEntity<String> hello() {
//        return ResponseEntity.ok("hello");
//    }
//
//    @PostMapping("/test-redirect")
//    public void testRedirect(HttpServletResponse response) throws IOException {
//        response.sendRedirect("/api2/member");
//    }



    @PostMapping("/signup")
    public ResponseEntity<MemberDto> signup(
            @Valid @RequestBody SignupDto signupDto
    ) {
        return ResponseEntity.ok(memberService.signup(signupDto));
    }

    @PostMapping("/signup-data-gen")
    public ResponseEntity<String> signupDataGen(
            @Valid @RequestBody List<SignupDataGenDto> signupDataGenDtos
    ) {
        memberService.signupDataGen(signupDataGenDtos);
        return ResponseEntity.ok("성공");
    }

    @GetMapping("/member")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<MemberDto> getMyUserInfo() {
        return ResponseEntity.ok(memberService.getMyMemberWithAuthorities());
    }


    @GetMapping("/member/{loginId}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<MemberDto> getUserInfo(@PathVariable String loginId) {
        return ResponseEntity.ok(memberService.getMemberWithAuthorities(loginId));
    }

    @GetMapping("/member/coupons")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<Page<MemberCouponDto>> getUnusedCoupons(@PageableDefault(size = 10, sort = "expireDate", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<MemberCouponDto> coupons = memberService.getUnusedCoupons(pageable);
        return ResponseEntity.ok(coupons);
    }

    @GetMapping("member/cart")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<Page<CartDto>> getCarts(@PageableDefault(size = 10, sort = {"lastModifiedDate", "createdDate"}, direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(memberService.getCartsByMember(pageable));
    }

    @PostMapping("member/cart/order")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<List<CartDto>> getSelectedItems(@RequestBody List<CartId> cartIds) {
        return ResponseEntity.ok(memberService.getSelectedItems(cartIds));
    }

    @PutMapping("/member/{memberId}")
    public ResponseEntity<MemberDto> updateMember(@PathVariable Long memberId, @Valid @RequestBody MemberDto memberDto) {
        Member updatedMember = memberService.updateMember(memberId, memberDto);
        return ResponseEntity.ok(MemberDto.from(updatedMember));
    }
}
