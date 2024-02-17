package hoxy.hLivv.dto;

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
public class MemberCouponDto {
    @Temporal(TemporalType.DATE)
    private Date expireDate;
    private String couponDesc;
    private Double discountRate;
    private boolean isUsed;

    public static MemberCouponDto from(MemberCoupon memberCoupon) {
        return MemberCouponDto.builder()
                .expireDate(memberCoupon.getExpireDate())
                .couponDesc(memberCoupon.getCoupon().getCouponDesc())
                .discountRate(memberCoupon.getCoupon().getDiscountRate())
                .isUsed(memberCoupon.getIsUsed()).build();
    }
}
