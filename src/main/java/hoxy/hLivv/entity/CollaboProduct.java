package hoxy.hLivv.entity;

import hoxy.hLivv.dto.CollaboProductDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "product_collabo")
@Getter
@Setter
@NoArgsConstructor
public class CollaboProduct {

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
    private Long quantity;

    public CollaboProduct(Collabo collabo, Product product, Long quantity) {
        this.collabo = collabo;
        this.product = product;
        this.quantity = quantity;
    }

    public CollaboProductDto toDto() {
        return new CollaboProductDto(product.toDto(), quantity);
    }
}
