package hoxy.hLivv.entity;

import hoxy.hLivv.entity.enums.DeliveryStatus;
import hoxy.hLivv.exception.DeliveryUpdateException;
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
        if (this.deliveryStatus==DeliveryStatus.배송중){
            this.deliveryStatus=DeliveryStatus.배송완료;
        } else{
            throw new DeliveryUpdateException("배송 상태가 '배송 중'이 아니므로 '배송 완료' 상태로 변경할 수 없습니다.");
        }
    }

    public void updateDeliveryProgressStatus(){
        if (this.deliveryStatus==DeliveryStatus.배송접수){
            this.deliveryStatus=DeliveryStatus.배송중;
        } else{
            throw new DeliveryUpdateException("배송 상태가 '배송 접수'가 아니므로 '배송 중' 상태로 변경할 수 없습니다.");
        }
    }



}
