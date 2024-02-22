package hoxy.hLivv.dto;

import hoxy.hLivv.entity.enums.DeliveryStatus;
import lombok.Data;
import java.time.LocalDate;

@Data
public class DeliveryDto {
    //private Long deliveryId;
    private DeliveryStatus deliveryStatus;
    private LocalDate deliveryStart;
    private LocalDate deliveryEnd;
}