package hoxy.hLivv.dto.order;

import hoxy.hLivv.entity.Coupon;
import hoxy.hLivv.entity.Member;
import hoxy.hLivv.entity.Order;
import hoxy.hLivv.entity.OrderProduct;
import hoxy.hLivv.entity.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderReqDto {
    /**
     * 주문 배송지 관련 정보
     */
    private String streetAddress;
    private String detailedAddress;
    private String zipCode;
    private String telephoneNumber;
    private String mobilePhoneNumber;
    private String requestMsg;
    private String order_id;

    /**
     * 결제 금액 관련 정보
     */
    private Long orderPoint;
    private LocalDate requestDate;
    private List<OrderProductReqDto> productList;
    private Long couponId;

    public Order prepareOrder(Member member, Coupon coupon, String impUid) {
        Order order = Order.builder()
                           .member(member)
                           .streetAddress(this.getStreetAddress())
                           .detailedAddress(this.getDetailedAddress())
                           .zipCode(this.getZipCode())
                           .telephoneNumber(this.getTelephoneNumber())
                           .mobilePhoneNumber(this.getMobilePhoneNumber())
                           .requestMsg(this.requestMsg)
                           .orderPoint(this.orderPoint)
                           .orderStatus(OrderStatus.결제대기)
                           .orderCoupon(coupon)
                           .requestDate(this.requestDate)
                           .products(new ArrayList<OrderProduct>())
                           .impUid(impUid)
                           .build();

        return order;
    }

    public Order testPrepareOrder(Member member, Coupon coupon) {
        Order order = Order.builder()
                           .member(member)
                           .streetAddress(this.getStreetAddress())
                           .detailedAddress(this.getDetailedAddress())
                           .zipCode(this.getZipCode())
                           .telephoneNumber(this.getTelephoneNumber())
                           .mobilePhoneNumber(this.getMobilePhoneNumber())
                           .requestMsg(this.requestMsg)
                           .orderPoint(this.orderPoint)
                           .orderStatus(OrderStatus.결제대기)
                           .orderCoupon(coupon)
                           .requestDate(this.requestDate)
                           .products(new ArrayList<OrderProduct>())
                           //.impUid(String.format("%-50s", UUID.randomUUID()).replace(' ', '*'))
                           .build();

        return order;
    }

}