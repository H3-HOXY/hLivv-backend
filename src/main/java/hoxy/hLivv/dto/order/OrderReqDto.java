package hoxy.hLivv.dto.order;

import hoxy.hLivv.entity.*;
import hoxy.hLivv.entity.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author 반정현
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderReqDto {
    private Long addressId;
    private String requestMsg;
    private Long orderPoint;
    private LocalDate requestDate;
    private List<OrderProductReqDto> productList;
    private Long couponId;

    /**
     * @author 반정현
     */
    public Order prepareOrder(Member member, Address address, Coupon coupon, String impUid){
        Order order = Order.builder()
                           .member(member)
                           .address(address)
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

    /**
     * @author 반정현
     */
    public Order testPrepareOrder(Member member, Address address, Coupon coupon){
        Order order = Order.builder()
                           .member(member)
                           .address(address)
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