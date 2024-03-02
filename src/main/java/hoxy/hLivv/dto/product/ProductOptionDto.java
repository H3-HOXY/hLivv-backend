package hoxy.hLivv.dto.product;


import hoxy.hLivv.entity.ProductOption;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProductOptionDto {
    private Long originalPrice;
    private Long discountPrice;
    private String optionName;

    public ProductOptionDto(Long originalPrice, Long discountPrice, String optionName) {
        this.originalPrice = originalPrice;
        this.discountPrice = discountPrice;
        this.optionName = optionName;
    }

    public static ProductOptionDto from(ProductOption productOption) {
        return new ProductOptionDto(productOption.getOriginalPrice(), productOption.getDiscountPrice(), productOption.getOptionName());
    }
}
