package hoxy.hLivv.dto;

import hoxy.hLivv.entity.OrderProduct;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryResDto {
    private Long orderId;
    private Long orderProductId;
    private String productName;
    private Integer productQty;
    private DeliveryDto deliveryDto;

    /**
     * @author 반정현
     */
    public static DeliveryResDto from(OrderProduct orderProduct){
        return DeliveryResDto.builder()
                .orderId(orderProduct.getOrder().getOrderId())
                .orderProductId(orderProduct.getOrderProductId())
                .productName(orderProduct.getProduct().getName())
                .productQty(orderProduct.getOrderProductQty())
                .deliveryDto(DeliveryDto.from(orderProduct.getDelivery()))
                .build();
    }
}
