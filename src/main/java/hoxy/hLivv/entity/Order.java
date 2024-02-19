package hoxy.hLivv.entity;

import hoxy.hLivv.entity.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
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
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="order_seq")
    @SequenceGenerator(name="order_seq", sequenceName="order_seq", allocationSize=1)
    @Column(name = "order_id")
    private Long orderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(name = "address_id")
    private Long addressId;

    @Column(name = "order_date")
    private LocalDate orderDate;

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
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Column(name = "order_coupon")
    private Long orderCoupon;

    @Column(name = "request_date")
    private LocalDate requestDate;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderProduct> products;

}