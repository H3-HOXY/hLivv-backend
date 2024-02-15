package hoxy.hLivv.repository;

import hoxy.hLivv.entity.Member;
import hoxy.hLivv.entity.MemberCoupon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberCouponRepository extends JpaRepository<MemberCoupon, Long> {
    Page<MemberCoupon> findByMemberAndIsUsedFalse(Member member, Pageable pageable);

    Optional<MemberCoupon> findByMcId_MemberIdAndMcId_CouponIdAndIsUsed(Long memberId, Long couponId, Boolean isUsed);
}