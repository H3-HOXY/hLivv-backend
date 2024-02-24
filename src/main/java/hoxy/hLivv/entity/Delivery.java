package hoxy.hLivv.entity;

import hoxy.hLivv.entity.enums.DeliveryStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "delivery")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Delivery {
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="delivery_seq")
    @SequenceGenerator(name="delivery_seq", sequenceName="delivery_seq", allocationSize=1)
    @Column(name = "delivery_id")
    private Long deliveryId;

    @Column(name = "delivery_status")
    @Enumerated(EnumType.STRING)
    private DeliveryStatus deliveryStatus;

    @Column(name = "delivery_start")
    private LocalDate deliveryStart;

    @Column(name = "delivery_end")
    private LocalDate deliveryEnd;

    public static Delivery prepareDelivery(){
        return Delivery.builder()
                .deliveryStatus(DeliveryStatus.배송접수)
                .build();
    }

    public void updateDeliveryCompletedStatus(){
        this.deliveryStatus=DeliveryStatus.배송완료;
    }

}
