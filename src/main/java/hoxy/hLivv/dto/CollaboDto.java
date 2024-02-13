package hoxy.hLivv.dto;

import hoxy.hLivv.entity.Collabo;
import hoxy.hLivv.entity.CollaboProduct;
import hoxy.hLivv.entity.ProductImage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CollaboDto extends ProductDto {
    private List<CollaboProductDto> collaboProduct = new ArrayList<>();

    @Override
    public Collabo toEntity() {
        var collabo = Collabo.builder()
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
        collabo.setProductImages(productImages.stream()
                                              .map(item -> new ProductImage(collabo, item.getImageUrl()))
                                              .toList());
        collabo.setCollaboProduct(collaboProduct.stream()
                                                .map(dto -> new CollaboProduct(collabo, dto.getProduct()
                                                                                           .toEntity(), dto.getQuantity()))
                                                .toList());
        return collabo;
    }
}
