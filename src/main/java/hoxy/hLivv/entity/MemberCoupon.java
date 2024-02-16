package hoxy.hLivv.entity;

import hoxy.hLivv.entity.compositekey.MemberCouponId;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
@Entity
@Table(name = "member_coupon")
@Getter
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

    @Column(name = "expire_date")
    private LocalDate expireDate;
    @Column(name = "is_used")
    private Boolean isUsed;

    // 생성자, 게터, 세터는 Lombok 어노테이션으로 대체

    public static MemberCoupon issue(Member member, Coupon coupon) {
        LocalDate expireDate = LocalDate.now().plusDays(coupon.getCouponDuration());

        return MemberCoupon.builder()
                .mcId(new MemberCouponId(member.getMemberId(), coupon.getCouponId()))
                .member(member)
                .coupon(coupon)
                .expireDate(expireDate)
                .isUsed(false)
                .build();
    }

}
