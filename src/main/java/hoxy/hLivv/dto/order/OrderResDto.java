package hoxy.hLivv.dto.order;


import hoxy.hLivv.dto.AddressDto;
import hoxy.hLivv.entity.Coupon;
import hoxy.hLivv.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderResDto {
    private Long orderId;
    private OrderRequesterDto orderRequesterDto;
    private AddressDto addressDto;
    private LocalDateTime orderDate;
    private String requestMsg;
    private Long subTotal;
    private Long orderTotal;
    private Long orderCash;
    private Long orderPoint;
    private String couponName;
    private LocalDate requestDate;
    private List<OrderProductResDto> products;


    public static OrderResDto from(Order order) {
        List<OrderProductResDto> productResDtoList = order.getProducts()
                                                          .stream()
                                                          .map(OrderProductResDto::from)
                                                          .collect(Collectors.toList());
        Coupon coupon = order.getOrderCoupon();

        var addressDto = AddressDto.builder()
                                   .streetAddress(order.getStreetAddress())
                                   .detailedAddress(order.getDetailedAddress())
                                   .zipCode(order.getZipCode())
                                   .telephoneNumber(order.getTelephoneNumber())
                                   .mobilePhoneNumber(order.getMobilePhoneNumber())
                                   .requestMsg(order.getRequestMsg())
                                   .build();

        String couponDesc = coupon != null ? coupon.getCouponDesc() : "No coupon applied";
        return OrderResDto.builder()
                          .orderId(order.getOrderId())
                          .orderRequesterDto(
                                  OrderRequesterDto.from(order.getMember(), (long) (order.getOrderCash() * 0.001)))
                          .addressDto(addressDto)
                          .orderDate(order.getOrderDate())
                          .requestMsg(order.getRequestMsg())
                          .subTotal(order.getSubTotal())
                          .orderTotal(order.getOrderTotal())
                          .orderCash(order.getOrderCash())
                          .orderPoint(order.getOrderPoint())
                          .couponName(couponDesc)
                          .requestDate(order.getRequestDate())
                          .products(productResDtoList)
                          .build();
    }

}