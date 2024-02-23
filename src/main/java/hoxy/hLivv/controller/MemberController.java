package hoxy.hLivv.controller;

import hoxy.hLivv.dto.*;
import hoxy.hLivv.entity.Member;
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
    public ResponseEntity<Page<MemberCouponDto>> getUnusedCoupons(@PageableDefault(size = 10, sort = "expireDate", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<MemberCouponDto> coupons = memberService.getUnusedCoupons(pageable);
        return ResponseEntity.ok(coupons);
    }

    @GetMapping("member/cart")
    public ResponseEntity<Page<CartDto>> getCarts(@PageableDefault(size = 10, sort = {"lastModifiedDate", "createdDate"}, direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(memberService.getCartsByMember(pageable));
    }

    @PutMapping("/updateMember")
    public ResponseEntity<MemberDto> updateMember(@Valid @RequestBody MemberDto memberDto) {
        Member updatedMember = memberService.updateMember(memberDto);
        return ResponseEntity.ok(MemberDto.from(updatedMember));
    }
}
