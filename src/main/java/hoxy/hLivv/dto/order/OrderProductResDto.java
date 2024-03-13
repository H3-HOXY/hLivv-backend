package hoxy.hLivv.dto.order;

import hoxy.hLivv.entity.OrderProduct;
import hoxy.hLivv.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author 반정현
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderProductResDto {
    private Long productId;
    private String productName;
    private Integer productQty;
    private Long productPrice;

    /**
     * @author 반정현
     */
    public static OrderProductResDto from(OrderProduct orderProduct){
        Product product=orderProduct.getProduct();
        return OrderProductResDto.builder()
                                 .productId(product.getId())
                                 .productName(product.getName())
                                 .productQty(orderProduct.getOrderProductQty())
                                 .productPrice(
                                         (long) ((long) product.getPrice() * (1 - product.getDiscountPercent() * 0.01)))
                                 .build();
    }
}
