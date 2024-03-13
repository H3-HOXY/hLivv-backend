package hoxy.hLivv.dto;

import hoxy.hLivv.entity.Cart;
import hoxy.hLivv.entity.Delivery;
import hoxy.hLivv.entity.enums.DeliveryStatus;
import lombok.*;

import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryDto {
    private Long deliveryId;
    private DeliveryStatus deliveryStatus;
    private LocalDate deliveryStart;
    private LocalDate deliveryEnd;

    /**
     * @author 반정현
     */
    public static DeliveryDto from(Delivery delivery){
        return DeliveryDto.builder()
                .deliveryId(delivery.getDeliveryId())
                .deliveryStatus(delivery.getDeliveryStatus())
                .deliveryStart(delivery.getDeliveryStart())
                .deliveryEnd(delivery.getDeliveryEnd())
                .build();

    }
}