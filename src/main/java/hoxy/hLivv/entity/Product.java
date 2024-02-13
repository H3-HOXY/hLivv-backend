package hoxy.hLivv.entity;

import hoxy.hLivv.dto.ProductDto;
import hoxy.hLivv.entity.enums.ProductType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;


@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@DiscriminatorColumn(name = "dtype")
@Entity(name = "product")
@Getter
@Setter
public class Product {
    @Id
    @GeneratedValue
    @Column(name = "product_id")
    protected Long id;

    @Column(name = "product_name")
    protected String name;

    @Column(name = "product_desc")
    protected String productDesc;

    @Column(name = "product_price")
    protected int price;

    @Column(name = "product_stock")
    protected int stockQuantity;
    @Column(name = "discount_percent")
    protected int discountPercent;
    @Column(name = "ar_supported")
    protected boolean isArSupported;
    @Column(name = "qr_supported")
    protected boolean isQrSupported;
    @Column(name = "is_restore")
    protected boolean isRestore;
    @Column(name = "is_eco")
    protected boolean isEco;
    @Column(name = "product_brand")
    protected String productBrand;
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    List<ProductImage> productImages = new ArrayList<>();

    public ProductDto toDto() {
        return ProductDto.builder()
                         .id(id)
                         .name(name)
                         .productDesc(productDesc)
                         .productType(ProductType.getProductType(this))
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
                         .build();
    }
}
