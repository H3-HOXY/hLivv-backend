package hoxy.hLivv.dto;

import lombok.Data;

@Data
public class OrderProductDto {
    private Long orderProductId;
    private Long orderId;
    private Long deliveryId;
}
