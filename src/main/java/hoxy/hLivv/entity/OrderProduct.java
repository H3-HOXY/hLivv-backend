package hoxy.hLivv.entity;

import hoxy.hLivv.entity.enums.DeliveryStatus;
import jakarta.persistence.*;
import lombok.*;

/**
 * @author 이상원, 반정현
 */
@Entity
@Table(name = "order_product")
@Getter
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

    @Column(name="order_product_qty")
    private Integer orderProductQty;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    /**
     * @author 반정현
     */
    public void confirmPurchase() {
        if (this.delivery.getDeliveryStatus() == DeliveryStatus.배송완료) {
            this.order.updateOrderCompletedStatus();
        }
    }

}
