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
    private Integer discountRate;

    /**
     * @author 반정현
     */
    public static CouponDto from(Coupon coupon) {
        return CouponDto.builder()
                .couponDuration(coupon.getCouponDuration())
                .couponDesc(coupon.getCouponDesc())
                .discountRate(coupon.getDiscountRate())
                .build();
    }

    /**
     * @author 반정현
     */
    public Coupon toCoupon() {
        return Coupon.builder()
                .couponDuration(this.getCouponDuration())
                .couponDesc(this.getCouponDesc())
                .discountRate(this.getDiscountRate())
                .build();

    }
}