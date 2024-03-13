package hoxy.hLivv.repository;

import hoxy.hLivv.entity.Coupon;
import hoxy.hLivv.entity.Member;
import hoxy.hLivv.entity.MemberCoupon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;
/**
 * @author 반정현
 */
public interface MemberCouponRepository extends JpaRepository<MemberCoupon, Long> {
    //Page<MemberCoupon> findByMemberAndIsUsedFalse(Member member, Pageable pageable);
    @Query("SELECT mc FROM MemberCoupon mc WHERE mc.member = :member AND mc.isUsed = false AND mc.expireDate >= :today")
    Page<MemberCoupon> findByMemberAndIsUsedFalseAndExpireDateAfter(
            @Param("member") Member member,
            @Param("today") LocalDate today,
            Pageable pageable);

    Optional<MemberCoupon> findByMcId_MemberIdAndMcId_CouponIdAndIsUsed(Long memberId, Long couponId, Boolean isUsed);

    Optional<MemberCoupon> findByMemberAndCoupon(Member member, Coupon coupon);
}