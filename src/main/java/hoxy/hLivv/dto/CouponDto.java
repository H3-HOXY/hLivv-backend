package hoxy.hLivv.dto;


import hoxy.hLivv.entity.Coupon;
import hoxy.hLivv.entity.MemberCoupon;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CouponDto {
    private Integer couponDuration;
    private String couponDesc;
    private Double discountRate;

    public static CouponDto from(Coupon coupon) {
        return CouponDto.builder()
                .couponDuration(coupon.getCouponDuration())
                .couponDesc(coupon.getCouponDesc())
                .discountRate(coupon.getDiscountRate())
                .build();
    }

    public Coupon toEntity(){
        return Coupon.builder()
                .duration(this.getCouponDuration())
                .desc(this.getCouponDesc())
                .discountRate(this.getDiscountRate())
                .build();

    }
}