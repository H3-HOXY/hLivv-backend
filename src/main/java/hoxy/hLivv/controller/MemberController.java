package hoxy.hLivv.controller;

import hoxy.hLivv.dto.*;
import hoxy.hLivv.dto.member.*;
import hoxy.hLivv.dto.order.OrderResDto;
import hoxy.hLivv.entity.Member;
import hoxy.hLivv.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
//            BindingResult bindingResult
    ) {
//        if (bindingResult.hasErrors()) {
//            // 유효성 검사 실패 처리
//            return ResponseEntity.badRequest().body("advdv");
//        }
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
    public ResponseEntity<Page<MemberCouponDto>> getUnusedCoupons(@RequestParam("page") int pageNo, @RequestParam("pageSize") int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.ASC, "expireDate"));
        Page<MemberCouponDto> coupons = memberService.getUnusedCoupons(pageable);
        return ResponseEntity.ok(coupons);
    }

    @GetMapping("member/cart")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<Page<CartDto>> getCarts(@RequestParam("page") int pageNo, @RequestParam("pageSize") int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.DESC, "lastModifiedDate", "createdDate"));
        return ResponseEntity.ok(memberService.getCartsByMember(pageable));
    }

    @GetMapping("member/order")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<Page<OrderResDto>> getOrders(@RequestParam("page") int pageNo, @RequestParam("pageSize") int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.ASC, "orderDate"));
        return ResponseEntity.ok(memberService.getOrdersByMember(pageable));
    }

    @PostMapping("member/cart/order")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<List<CartDto>> getSelectedItems(@RequestBody List<Long> productIds) {
        return ResponseEntity.ok(memberService.getSelectedItems(productIds));
    }

    @PutMapping("/updateMember")
    public ResponseEntity<MemberDto> updateMember(@Valid @RequestBody MemberDto memberDto) {
        Member updatedMember = memberService.updateMember(memberDto);
        return ResponseEntity.ok(MemberDto.from(updatedMember));
    }

    @GetMapping("/member/grade")
    public ResponseEntity<List<MemberGradeDto>> getMemberGradeCnt() {
        return ResponseEntity.ok(memberService.getMemberGrade());
    }

    @GetMapping("/member/month/signup")
    public ResponseEntity<List<MonthlyMemberRegisterDto>> getMemberCntMonthly() {
        return ResponseEntity.ok(memberService.getMonthlyMemberRegi());
    }

}
