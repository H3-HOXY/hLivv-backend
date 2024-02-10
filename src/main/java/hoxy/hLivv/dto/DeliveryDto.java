package hoxy.hLivv.dto;

import hoxy.hLivv.entity.enums.DeliveryStatus;
import lombok.Data;

import java.util.Date;

@Data
public class DeliveryDto {
    private Long deliveryId;
    private DeliveryStatus deliveryStatus;
    private Date deliveryStart;
    private Date deliveryEnd;
}