package hoxy.hLivv.entity;

import hoxy.hLivv.entity.compositekey.CartId;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "cart")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Cart {
    @EmbeddedId
    private CartId cartId;

    @MapsId("memberId")
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @MapsId("id")
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;


    @Column(name = "cart_qty")
    private Integer cartQty;

    @CreatedDate
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime lastModifiedDate;


    public void updateQuantity(Integer qty) {
        this.cartQty = qty;
    }

    public boolean isEmpty() {
        return this.cartQty == 0;
    }
}
