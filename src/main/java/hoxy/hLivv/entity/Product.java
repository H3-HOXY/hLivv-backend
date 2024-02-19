package hoxy.hLivv.entity;

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

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "product_category")
    protected Category category;

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

}
