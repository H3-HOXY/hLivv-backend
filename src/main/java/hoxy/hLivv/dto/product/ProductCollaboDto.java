package hoxy.hLivv.dto.product;

import hoxy.hLivv.entity.ProductCollabo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductCollaboDto {
    private Long productId;
    private Integer quantity;

}
