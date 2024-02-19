package hoxy.hLivv.dto.order;

import hoxy.hLivv.entity.Product;
import lombok.*;

import java.time.LocalDate;
import java.util.List;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderReqDto {
//    private Long orderId; // 주문번호
    //private Long memberId; // 멤버
    private Long addressId; // 주소
//    private LocalDate orderDate; // 주문일
    private String requestMsg; //배송 요청 메세지
//    private Double subTotal; // 쿠폰, 할인 등 적용 전 금액
//    private Double orderTotal; // 최종 금액
//    private Double orderCash; // 현금 결제액
    private Double orderPoint; // 포인트 결제액
//    private OrderStatus orderStatus; // 주문 상태
    private Long couponId;             // 적용하는 쿠폰 (null값 가능)
 //   private Long orderCoupon; // 쿠폰 결제액
    private LocalDate requestDate; // 배송희망일
    private List<Product> products; //주문상품목록
}