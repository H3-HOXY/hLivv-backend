package hoxy.hLivv.dto.product;

import hoxy.hLivv.dto.CategoryDto;
import hoxy.hLivv.entity.Product;
import hoxy.hLivv.entity.ProductImage;
import hoxy.hLivv.entity.ProductOption;
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
    protected CategoryDto category;
    List<ProductImageDto> productImages = new ArrayList<>();
    List<ProductOptionDto> productOptions = new ArrayList<>();

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
                             .category(category.toEntity())
                             .productBrand(productBrand)
                             .build();

        product.setProductImages(productImages.stream()
                                              .map(item -> new ProductImage(product, item.getImageUrl()))
                                              .toList());
        product.setProductOptions(productOptions.stream()
                                                .map(item -> new ProductOption(null, product, item.getOriginalPrice(), item.getDiscountPrice(), item.getOptionName()))
                                                .toList());
        return product;
    }

    public static ProductDto from(Product product) {
        return ProductDto.builder()
                         .id(product.getId())
                         .name(product.getName())
                         .productDesc(product.getProductDesc())
                         .productType(ProductType.getProductType(product))
                         .price(product.getPrice())
                         .category(CategoryDto.from(product.getCategory()))
                         .stockQuantity(product.getStockQuantity())
                         .productImages(product.getProductImages()
                                               .stream()
                                               .map(ProductImage::toDto)
                                               .toList())
                         .productOptions(product.getProductOptions()
                                                .stream()
                                                .map(ProductOptionDto::from)
                                                .toList())
                         .discountPercent(product.getDiscountPercent())
                         .isArSupported(product.isArSupported())
                         .isQrSupported(product.isQrSupported())
                         .isRestore(product.isRestore())
                         .isEco(product.isEco())
                         .productBrand(product.getProductBrand())
                         .build();
    }
}
