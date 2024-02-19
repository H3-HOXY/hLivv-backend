package hoxy.hLivv.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "order_product")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderProduct {
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="order_product_seq")
    @SequenceGenerator(name="order_product_seq", sequenceName="order_product_seq", allocationSize=1)
    @Column(name = "order_product_id")
    private Long orderProductId;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;


    @ManyToOne
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

}
