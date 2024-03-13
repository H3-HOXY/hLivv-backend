package hoxy.hLivv.entity;

import hoxy.hLivv.entity.compositekey.MemberCouponId;
import hoxy.hLivv.exception.AlreadyUsedCouponException;
import hoxy.hLivv.exception.ExpiredCouponException;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

/**
 * @author 이상원, 반정현
 */
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

    /**
     * @author 반정현
     */
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

    /**
     * @author 반정현
     */
    public void use() {
        if (isUsed) {
            throw new AlreadyUsedCouponException("Coupon is already used.");
        }

        if (expireDate.isBefore(LocalDate.now())) {
            throw new ExpiredCouponException("Coupon has expired.");
        }
        this.isUsed = true;
    }

}
