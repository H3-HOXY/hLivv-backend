package hoxy.hLivv.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "coupon")
@Getter
@Setter
public class Coupon {
    @Id
    private Long couponId;
    private Integer couponDuration;
    private String couponDesc;
    @Column(columnDefinition = "NUMBER")
    private Double discountRate;

    // 생성자, 게터, 세터는 Lombok 어노테이션으로 대체
}