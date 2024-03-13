package hoxy.hLivv.dto;

import hoxy.hLivv.dto.order.OrderProductResDto;
import hoxy.hLivv.dto.order.OrderRequesterDto;
import hoxy.hLivv.dto.order.OrderResDto;
import hoxy.hLivv.entity.OrderProduct;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderProductDto {
    private Long orderId;
    private Long orderProductId;
    private String productName;
    private Integer productQty;
    private DeliveryDto deliveryDto;

    /**
     * @author 반정현
     */
    public static OrderProductDto from(OrderProduct orderProduct){
        return OrderProductDto.builder()
                .orderId(orderProduct.getOrder().getOrderId())
                .orderProductId(orderProduct.getOrderProductId())
                .productName(orderProduct.getProduct().getName())
                .productQty(orderProduct.getOrderProductQty())
                .deliveryDto(DeliveryDto.from(orderProduct.getDelivery()))
                .build();
    }
}
