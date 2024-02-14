package hoxy.hLivv.entity;

import hoxy.hLivv.dto.product.CollaboDto;
import hoxy.hLivv.entity.enums.ProductType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SuperBuilder
@Entity(name = "collabo")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Collabo extends Product {
    @OneToMany(mappedBy = "collabo", cascade = CascadeType.ALL)
    private List<ProductCollabo> productCollabo = new ArrayList<>();

    private Date startDate;
    private Date endDate;

    @Override
    public CollaboDto toDto() {
        return CollaboDto.builder()
                         .id(id)
                         .name(name)
                         .productDesc(productDesc)
                         .productType(ProductType.getProductType(this))
                         .startDate(startDate)
                         .endDate(endDate)
                         .price(price)
                         .stockQuantity(stockQuantity)
                         .productImages(productImages.stream()
                                                     .map(ProductImage::toDto)
                                                     .toList())
                         .discountPercent(discountPercent)
                         .isArSupported(isArSupported)
                         .isQrSupported(isQrSupported)
                         .isRestore(isRestore)
                         .isEco(isEco)
                         .productBrand(productBrand)
                         .collaboProduct(productCollabo.stream()
                                                       .map(ProductCollabo::toDto)
                                                       .toList())
                         .build();
    }
}
