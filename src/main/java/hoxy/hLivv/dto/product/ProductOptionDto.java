package hoxy.hLivv.dto.product;


import hoxy.hLivv.entity.ProductOption;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductOptionDto {
    private String optionName;
    private Long originalPrice;
    private Long discountPrice;


    public static ProductOptionDto from(ProductOption productOption) {
        return new ProductOptionDto(productOption.getOptionName(), productOption.getOriginalPrice(), productOption.getDiscountPrice());
    }
}
