package hoxy.hLivv.dto;


import hoxy.hLivv.entity.Coupon;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
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

    public Coupon toEntity() {
        return Coupon.builder()
                .couponDuration(this.getCouponDuration())
                .couponDesc(this.getCouponDesc())
                .discountRate(this.getDiscountRate())
                .build();

    }
}