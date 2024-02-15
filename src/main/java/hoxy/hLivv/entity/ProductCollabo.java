package hoxy.hLivv.entity;

import hoxy.hLivv.dto.product.ProductCollaboDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "product_collabo")
@Getter
@Setter
@NoArgsConstructor
public class ProductCollabo {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "collabo_id")
    private Collabo collabo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "quantity")
    private Integer quantity;

    public ProductCollabo(Collabo collabo, Product product, Integer quantity) {
        this.collabo = collabo;
        this.product = product;
        this.quantity = quantity;
    }

    public ProductCollaboDto toDto() {
        return new ProductCollaboDto(product.getId(), quantity);
    }
}
