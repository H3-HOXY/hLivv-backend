package hoxy.hLivv.entity;
import hoxy.hLivv.entity.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long orderId;

    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "address_id")
    private Long addressId;

    @Column(name = "order_date")
    private Date orderDate;

    @Column(name = "request_msg")
    private String requestMsg;

    @Column(name = "sub_total")
    private Double subTotal;

    @Column(name = "order_total")
    private Double orderTotal;

    @Column(name = "order_cash")
    private Double orderCash;

    @Column(name = "order_point")
    private Double orderPoint;

    @Column(name = "order_status")
    private OrderStatus orderStatus;

    @Column(name = "order_coupon")
    private Long orderCoupon;

    @Column(name = "request_date")
    private Date requestDate;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderProduct> products;

}