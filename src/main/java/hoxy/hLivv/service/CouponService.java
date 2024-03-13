package hoxy.hLivv.service;

import hoxy.hLivv.dto.CouponDto;
import hoxy.hLivv.dto.MemberCouponDto;
import hoxy.hLivv.entity.Coupon;
import hoxy.hLivv.entity.Member;
import hoxy.hLivv.entity.MemberCoupon;
import hoxy.hLivv.exception.DuplicateMemberCouponException;
import hoxy.hLivv.exception.NotFoundCouponException;
import hoxy.hLivv.exception.NotFoundMemberException;
import hoxy.hLivv.repository.CouponRepository;
import hoxy.hLivv.repository.MemberCouponRepository;
import hoxy.hLivv.repository.MemberRepository;
import hoxy.hLivv.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CouponService {
    private final MemberRepository memberRepository;
    private final CouponRepository couponRepository;
    private final MemberCouponRepository memberCouponRepository;

    /**
     * @author 반정현
     */
    @Transactional
    public MemberCouponDto issueCoupon(Long couponId) {
        Member member = SecurityUtil.getCurrentUsername()
                .flatMap(memberRepository::findOneWithAuthoritiesByLoginId)
                .orElseThrow(() -> new NotFoundMemberException("Member not found"));
        Coupon coupon = couponRepository.findById(couponId).orElseThrow(() -> new NotFoundCouponException("Coupon not found"));

        Optional<MemberCoupon> existingCoupon = memberCouponRepository.findByMcId_MemberIdAndMcId_CouponIdAndIsUsed(member.getMemberId(), couponId, false);
        if (existingCoupon.isPresent()) {
            throw new DuplicateMemberCouponException("쿠폰함에 동일한 쿠폰이 존재합니다.");
        }

        MemberCoupon memberCoupon = memberCouponRepository.save(MemberCoupon.issue(member, coupon));
        member.getCoupons().add(memberCoupon);
        return MemberCouponDto.from(memberCoupon);
    }

    //C
    /**
     * @author 반정현
     */
    @Transactional
    public CouponDto saveCoupon(CouponDto couponDto) {
        Coupon coupon = couponRepository.save(couponDto.toCoupon());
        return CouponDto.from(coupon);
    }

    //R
    /**
     * @author 반정현
     */
    @Transactional(readOnly = true)
    public CouponDto getCouponBy(Long couponId) {
        return CouponDto.from(couponRepository.findById(couponId).orElseThrow(() -> new NotFoundCouponException("Coupon not found")));
    }

    /**
     * @author 반정현
     */
    @Transactional(readOnly = true)
    public List<CouponDto> getAllCoupon() {
        return couponRepository.findAll()
                .stream()
                .map(CouponDto::from)
                .toList();
    }

    //U
    /**
     * @author 반정현
     */
    @Transactional
    public CouponDto updateCoupon(Long couponId, CouponDto couponDto) {
        Coupon coupon = couponRepository.findById(couponId).orElseThrow(() -> new NotFoundCouponException("Coupon not found"));
        coupon.update(couponDto.getCouponDuration(), couponDto.getCouponDesc(), couponDto.getDiscountRate());
        return CouponDto.from(coupon);
    }

    //D
    /**
     * @author 반정현
     */
    @Transactional
    public void deleteCoupon(Long couponId) {
        Coupon coupon = couponRepository.findById(couponId).orElseThrow(() -> new NotFoundCouponException("Coupon not found"));
        couponRepository.delete(coupon);
    }
}
