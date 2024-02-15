package hoxy.hLivv.dto.product;

import hoxy.hLivv.entity.Collabo;
import hoxy.hLivv.entity.Product;
import hoxy.hLivv.entity.ProductCollabo;
import hoxy.hLivv.entity.ProductImage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CollaboDto extends ProductDto {
    private List<ProductCollaboDto> collaboProduct = new ArrayList<>();
    private Date startDate;
    private Date endDate;

    @Override
    public Collabo toEntity() {
        var collabo = Collabo.builder()
                             .id(id)
                             .name(name)
                             .productDesc(productDesc)
                             .price(price)
                             .startDate(startDate)
                             .endDate(endDate)
                             .stockQuantity(stockQuantity)
                             .discountPercent(discountPercent)
                             .isArSupported(isArSupported)
                             .isQrSupported(isQrSupported)
                             .isRestore(isRestore)
                             .isEco(isEco)
                             .productBrand(productBrand)
                             .build();
        collabo.setProductImages(productImages.stream()
                                              .map(item -> new ProductImage(collabo, item.getImageUrl()))
                                              .toList());
        collabo.setProductCollabo(collaboProduct.stream()
                                                .map(dto -> {
                                                    var product = new Product();
                                                    product.setId(dto.getProductId());
                                                    return new ProductCollabo(collabo, product, dto.getQuantity());
                                                })
                                                .toList());
        return collabo;
    }
}
