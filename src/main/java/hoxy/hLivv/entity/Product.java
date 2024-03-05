package hoxy.hLivv.entity;

import hoxy.hLivv.entity.enums.ProductType;
import hoxy.hLivv.exception.StockOverFlowException;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
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

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "product_category")
    protected Category category;

    @Min(0)
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

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    List<ProductOption> productOptions = new ArrayList<>();

//    public void decreaseStock(int qty) {
//        if (this.stockQuantity < qty) {
//            throw new StockOverFlowException("Not enough stock for product with id: " + this.id);
//        }
//        this.stockQuantity -= qty;
//    }

    public void decreaseStock(int qty) {
        if (ProductType.getProductType(this) == ProductType.COLLABO) {
            if (this instanceof Collabo collabo) {
                for (ProductCollabo productCollabo : collabo.getProductCollabo()) {
                    if (productCollabo.getProduct() != null && productCollabo.getQuantity() != null) {
                        Product product = productCollabo.getProduct();
                        product.decreaseStock(qty * productCollabo.getQuantity());
                    }
                }
            }
        } else {
            if (this.stockQuantity < qty) {
                throw new StockOverFlowException("Not enough stock for product with id: " + this.id);
            }
            this.stockQuantity -= qty;
        }
    }


}
