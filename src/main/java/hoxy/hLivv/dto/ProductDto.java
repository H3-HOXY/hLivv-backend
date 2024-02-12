package hoxy.hLivv.dto;

import hoxy.hLivv.entity.Product;
import hoxy.hLivv.entity.ProductImage;
import hoxy.hLivv.entity.enums.ProductType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    protected Long id;

    protected String name;

    protected String productDesc;

    protected ProductType productType;

    protected int price;

    protected int stockQuantity;
    protected int discountPercent;
    protected boolean isArSupported;
    protected boolean isQrSupported;
    protected boolean isRestore;
    protected boolean isEco;
    protected String productBrand;
    List<ProductImageDto> productImages = new ArrayList<>();

    public Product toEntity() {
        var product = Product.builder()
                             .id(id)
                             .name(name)
                             .productDesc(productDesc)
                             .price(price)
                             .stockQuantity(stockQuantity)
                             .discountPercent(discountPercent)
                             .isArSupported(isArSupported)
                             .isQrSupported(isQrSupported)
                             .isRestore(isRestore)
                             .isEco(isEco)
                             .productBrand(productBrand)
                             .build();

        product.setProductImages(
                productImages.stream()
                             .map(item -> new ProductImage(product, item.getImageUrl()))
                             .toList()
        );
        return product;
    }
}
