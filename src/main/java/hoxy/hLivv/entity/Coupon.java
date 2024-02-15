package hoxy.hLivv.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "coupon")
@SequenceGenerator(name = "coupon_seq", sequenceName = "COUPON_SEQ", allocationSize = 1)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "coupon_seq")
    @Column(name = "coupon_id")
    private Long couponId;
    @Column(name = "coupon_duration")
    private Integer couponDuration;
    @Column(name = "coupon_desc")
    private String couponDesc;
    @Column(columnDefinition = "NUMBER", name = "discount_rate")
    private Double discountRate;
    //private Long raffleProduct;

    @Builder
    public Coupon(Integer duration, String desc, Double discountRate) {
        this.couponDuration = duration;
        this.couponDesc = desc;
        this.discountRate = discountRate;
    }

    public void update(Integer duration, String desc, Double discountRate) {
        this.couponDuration = duration;
        this.couponDesc = desc;
        this.discountRate = discountRate;
    }
}