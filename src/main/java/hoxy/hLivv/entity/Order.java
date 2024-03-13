package hoxy.hLivv.entity;

import hoxy.hLivv.entity.enums.OrderStatus;
import hoxy.hLivv.entity.enums.ProductType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.util.Pair;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static hoxy.hLivv.entity.Delivery.prepareDelivery;

/**
 * @author 이상원, 반정현
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "orders")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_seq")
    @SequenceGenerator(name = "order_seq", sequenceName = "order_seq", allocationSize = 1)
    @Column(name = "order_id")
    private Long orderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne
    @JoinColumn(name = "address_id")
    private Address address;

    @Column(name = "order_date")
    @CreatedDate
    private LocalDateTime orderDate;

    @Column(name = "request_msg")
    private String requestMsg;

    @Column(name = "sub_total")
    private Long subTotal;

    @Column(name = "order_total")
    private Long orderTotal;

    @Column(name = "order_cash")
    private Long orderCash;

    @Column(name = "order_point")
    private Long orderPoint;

    @Column(name = "order_status")
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @ManyToOne
    @JoinColumn(name = "coupon_id")
    private Coupon orderCoupon; // 쿠폰 아이디

    @Column(name = "request_date")
    private LocalDate requestDate;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderProduct> products;

    @Column(length = 50, unique = true)
    private String impUid;

    /**
     * @author 반정현
     */
    public void applyCoupon(MemberCoupon memberCoupon) {
        memberCoupon.use();
        Coupon coupon = memberCoupon.getCoupon();
        this.orderCoupon = coupon;
        calculateTotal(coupon.getDiscountRate());
    }

    /**
     * @author 반정현
     */
    public void calculateTotal(int discountRate) {
        Pair<Long, Long> totalPrices = this.products.stream()
                                                    .map(orderProduct -> {
                                                        long originalPrice = 0L;
                                                        long discountedPrice = 0L;
                                                        Product product = orderProduct.getProduct();
                                                        if (ProductType.getProductType(
                                                                product) == ProductType.COLLABO) {
                                                            if (product instanceof Collabo collabo) {
                                                                for (ProductCollabo productCollabo : collabo.getProductCollabo()) {
                                                                    if (productCollabo.getProduct() != null && productCollabo.getQuantity() != null) {
                                                                        Product product1 = productCollabo.getProduct();
                                                                        originalPrice += (long) product1.getPrice() * productCollabo.getQuantity() * orderProduct.getOrderProductQty();
                                                                    }
                                                                }
                                                                discountedPrice += (long) (originalPrice * (1 - (collabo.getDiscountPercent() * 0.01)));
                                                            }
                                                        } else {
                                                            originalPrice = (long) product.getPrice() * orderProduct.getOrderProductQty();
                                                            discountedPrice = (long) (originalPrice * (1 - (product.getDiscountPercent() * 0.01)));
                                                        }

                                                        return Pair.of(originalPrice, discountedPrice);
                                                    })
                                                    .reduce(Pair.of(0L, 0L),
                                                            (a, b) -> Pair.of(a.getFirst() + b.getFirst(),
                                                                              a.getSecond() + b.getSecond()));

        long subTotal = totalPrices.getFirst();
        long discountedTotal = totalPrices.getSecond();

        this.subTotal = subTotal;
        this.orderTotal = (long) (discountedTotal * (1 - (discountRate * 0.01)));
        this.orderCash = this.orderTotal - this.orderPoint;
    }

    /**
     * @author 반정현
     */
    public void addProduct(Product product, Integer quantity) {
        OrderProduct orderProduct = OrderProduct.builder()
                                                .order(this)
                                                .product(product)
                                                .orderProductQty(quantity)
                                                .delivery(prepareDelivery())
                                                .build();
        this.products.add(orderProduct);
    }

    /**
     * @author 반정현
     */
    public void updateOrderCompletedStatus(){
        this.orderStatus= OrderStatus.구매확정;
    }

    /**
     * @author 반정현
     */
    public void updatePaymentCompleteStatus(){
        this.orderStatus= OrderStatus.주문접수;
    }

    /**
     * @author 반정현
     */
    public void updatePaymentImUid(String impUid){
        this.impUid=impUid;
    }

    /**
     * @author 반정현
     */
    public void updatePaymentCancelStatus(){
        this.orderStatus= OrderStatus.결제실패;
    }
}