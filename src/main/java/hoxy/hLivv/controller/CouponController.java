package hoxy.hLivv.controller;

import hoxy.hLivv.dto.CouponDto;
import hoxy.hLivv.dto.MemberCouponDto;
import hoxy.hLivv.service.CouponService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
//@Tag(name = "쿠폰 API", description = "쿠폰 관리와 관련된 API 목록")
public class CouponController {
    private final CouponService couponService;

    //@Operation(summary = "couponId로 쿠폰 발급", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/coupons/{couponId}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<MemberCouponDto> issueCoupon(@PathVariable Long couponId) {
        MemberCouponDto memberCouponDto = couponService.issueCoupon(couponId);
        return ResponseEntity.ok(memberCouponDto);
    }

    //@Operation(summary = "쿠폰 저장", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/coupons")
    public ResponseEntity<CouponDto> saveCoupon(@RequestBody CouponDto couponDto) {
        return ResponseEntity.ok(couponService.saveCoupon(couponDto));
    }

    //@Operation(summary = "couponId로 쿠폰 조회", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/coupons/{couponId}")
    public ResponseEntity<CouponDto> getCouponBy(@PathVariable Long couponId) {
        return ResponseEntity.ok(couponService.getCouponBy(couponId));
    }

    //@Operation(summary = "모든 쿠폰 조회", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/coupons")
    public ResponseEntity<List<CouponDto>> getAllCoupon() {
        return ResponseEntity.ok(couponService.getAllCoupon());
    }

    //@Operation(summary = "couponId로 쿠폰 수정", security = @SecurityRequirement(name = "bearerAuth"))
    @PutMapping("/coupons/{couponId}")
    public ResponseEntity<CouponDto> updateCoupon(@PathVariable Long couponId, @RequestBody CouponDto couponDto) {
        return ResponseEntity.ok(couponService.updateCoupon(couponId, couponDto));
    }

    //@Operation(summary = "couponId로 쿠폰 삭제", security = @SecurityRequirement(name = "bearerAuth"))
    @DeleteMapping("/coupons/{couponId}")
    public ResponseEntity<Void> deleteCoupon(@PathVariable Long couponId) {
        couponService.deleteCoupon(couponId);
        return ResponseEntity.noContent().build();
    }
}