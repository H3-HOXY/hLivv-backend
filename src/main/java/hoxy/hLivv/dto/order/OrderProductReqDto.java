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
public class OrderProductReqDto {
    private Long productId;
    private Integer productQty;
}
