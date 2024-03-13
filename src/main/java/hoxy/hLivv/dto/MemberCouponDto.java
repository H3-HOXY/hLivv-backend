package hoxy.hLivv.dto;

import hoxy.hLivv.entity.MemberCoupon;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.*;

import java.time.LocalDate;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberCouponDto {
    private Long couponId;
    @Temporal(TemporalType.DATE)
    private LocalDate expireDate;
    private String couponDesc;
    private Integer discountRate;
    private boolean isUsed;

    /**
     * @author 반정현
     */
    public static MemberCouponDto from(MemberCoupon memberCoupon) {
        return MemberCouponDto.builder()
                              .couponId(memberCoupon.getCoupon()
                                                    .getCouponId())
                .expireDate(memberCoupon.getExpireDate())
                .couponDesc(memberCoupon.getCoupon().getCouponDesc())
                .discountRate(memberCoupon.getCoupon().getDiscountRate())
                .isUsed(memberCoupon.getIsUsed()).build();
    }
}
