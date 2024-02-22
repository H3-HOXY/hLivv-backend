package hoxy.hLivv.dto.order;

import hoxy.hLivv.entity.OrderProduct;
import hoxy.hLivv.entity.Product;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderProductResDto {
    private String productName;
    private Integer productQty;
    private Long productPrice;

    public static OrderProductResDto from(OrderProduct orderProduct){
        Product product=orderProduct.getProduct();
        return OrderProductResDto.builder()
                .productName(product.getName())
                .productQty(orderProduct.getOrderProductQty())
                .productPrice((long) ((long) product.getPrice()* (1-product.getDiscountPercent()*0.01)))
                .build();
    }
}
