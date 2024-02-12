package hoxy.hLivv.entity;

import hoxy.hLivv.dto.ProductImageDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "product_image")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductImage {
    @Id
    @GeneratedValue
    @Column(name = "product_image_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "image_url")
    private String imageUrl;


    public ProductImage(Product product, @NotNull String imageUrl) {
        this.product = product;
        this.imageUrl = imageUrl;
    }

    public ProductImageDto toDto() {
        return new ProductImageDto(imageUrl);
    }
}
