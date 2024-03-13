package hoxy.hLivv.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * @author 이상원, 반정현
 */
@Entity
@Table(name = "coupon")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "coupon_seq")
    @SequenceGenerator(name = "coupon_seq", sequenceName = "COUPON_SEQ", allocationSize = 1)
    @Column(name = "coupon_id")
    private Long couponId;
    @Column(name = "coupon_duration")
    private Integer couponDuration;
    @Column(name = "coupon_desc")
    private String couponDesc;
    @Column(columnDefinition = "NUMBER", name = "discount_rate")
    private Integer discountRate;
    //private Long raffleProduct;

    /**
     * @author 반정현
     */
    public void update(Integer duration, String desc, Integer discountRate) {
        this.couponDuration = duration;
        this.couponDesc = desc;
        this.discountRate = discountRate;
    }
}