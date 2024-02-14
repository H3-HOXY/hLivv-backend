package hoxy.hLivv.entity;

import hoxy.hLivv.dto.CouponDto;
import hoxy.hLivv.dto.ProductDto;
import hoxy.hLivv.entity.compositekey.MemberCouponId;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "member_coupon")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberCoupon {
    @EmbeddedId
    private MemberCouponId mcId;

    @MapsId("memberId")
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @MapsId("couponId")
    @ManyToOne
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;

    @Temporal(TemporalType.DATE)
    @Column(name="expire_date")
    private Date expireDate;
    @Column(name="is_used")
    private Boolean isUsed;

    // 생성자, 게터, 세터는 Lombok 어노테이션으로 대체

    public static MemberCoupon issue(Member member, Coupon coupon) {
        // LocalDate-Date 변환
        LocalDate expireLocalDate = LocalDate.now().plusDays(coupon.getCouponDuration());
        Date expireDate = java.sql.Date.valueOf(expireLocalDate);

        return MemberCoupon.builder()
                .mcId(new MemberCouponId(member.getMemberId(), coupon.getCouponId()))
                .member(member)
                .coupon(coupon)
                .expireDate(expireDate)
                .isUsed(false)
                .build();
    }

}
