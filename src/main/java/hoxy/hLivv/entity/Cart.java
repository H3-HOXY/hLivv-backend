package hoxy.hLivv.entity;

import hoxy.hLivv.entity.compositekey.CartId;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cart")
@Getter
@Setter
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

    @Column(name = "cart_qty")
    private Integer cartQty;

}
