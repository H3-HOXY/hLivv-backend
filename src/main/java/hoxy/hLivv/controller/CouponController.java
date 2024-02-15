package hoxy.hLivv.controller;

import hoxy.hLivv.dto.CouponDto;
import hoxy.hLivv.dto.MemberCouponDto;
import hoxy.hLivv.service.CouponService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class CouponController {
    private final CouponService couponService;

    @PostMapping("/coupons/{couponId}/issue")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<MemberCouponDto> issueCoupon(@PathVariable Long couponId, HttpServletRequest request) {
        MemberCouponDto memberCouponDto = couponService.issueCoupon(couponId);
        return ResponseEntity.ok(memberCouponDto);
    }

    @PostMapping("/coupons")
    public ResponseEntity<CouponDto> saveCoupon(@RequestBody CouponDto couponDto) {
        return ResponseEntity.ok(couponService.saveCoupon(couponDto));
    }

    @GetMapping("/coupons/{couponId}")
    public ResponseEntity<CouponDto> getCouponBy(@PathVariable Long couponId) {
        return ResponseEntity.ok(couponService.getCouponBy(couponId));
    }

    @GetMapping("/coupons")
    public ResponseEntity<List<CouponDto>> getAllCoupon() {
        return ResponseEntity.ok(couponService.getAllCoupon());
    }

    @PutMapping("/coupons/{couponId}")
    public ResponseEntity<CouponDto> updateCoupon(@PathVariable Long couponId, @RequestBody CouponDto couponDto) {
        return ResponseEntity.ok(couponService.updateCoupon(couponId, couponDto));
    }

    @DeleteMapping("/coupons/{couponId}")
    public ResponseEntity<Void> deleteCoupon(@PathVariable Long couponId) {
        couponService.deleteCoupon(couponId);
        return ResponseEntity.noContent().build();
    }
}
