package hoxy.hLivv.service;

import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.CancelData;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import hoxy.hLivv.dto.order.MonthlyOrderSummaryDto;
import hoxy.hLivv.dto.order.OrderProductReqDto;
import hoxy.hLivv.dto.order.OrderReqDto;
import hoxy.hLivv.dto.order.OrderResDto;
import hoxy.hLivv.entity.*;
import hoxy.hLivv.exception.*;
import hoxy.hLivv.repository.*;
import hoxy.hLivv.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;
    private final CouponRepository couponRepository;
    private final MemberCouponRepository memberCouponRepository;
    private final AddressRepository addressRepository;
    private final IamportClient iamportClient;


    @Transactional
    public OrderResDto saveOrder(OrderReqDto orderReqDto) {
        // 1. DTO로부터 정보 추출
        Member member = getMember();
        Coupon coupon = getCoupon(orderReqDto.getCouponId());

        // 2. 상품과 수량 조회 및 재고 감소
        List<Product> products = decreaseProductsStock(orderReqDto.getProductList());

        // 3. 주문 생성
        Order order = createOrder(orderReqDto, member, coupon, products);

        // 4. 주문 저장
        orderRepository.save(order);

        // 5. 생성된 주문 반환
        return OrderResDto.from(order);
    }

    @Transactional
    public Order createOrder(OrderReqDto orderReqDto, Member member, Coupon coupon, List<Product> products) {
        Order order = orderReqDto.testPrepareOrder(member, coupon);

        for (int i = 0; i < orderReqDto.getProductList()
                                       .size(); i++) {
            order.addProduct(products.get(i), orderReqDto.getProductList()
                                                         .get(i)
                                                         .getProductQty());

        }

        if (coupon != null) {
            MemberCoupon memberCoupon = memberCouponRepository.findByMemberAndCoupon(member, coupon)
                                                              .orElseThrow(() -> new NotFoundCouponException(
                                                                      "The member does not have the coupon."));
            // 멤버 쿠폰 사용 처리
            order.applyCoupon(memberCoupon);
        } else {
            order.calculateTotal(0);
        }

        // 포인트 사용 처리
        member.usePoints(orderReqDto.getOrderPoint());
        return order;
    }


    @Transactional
    public OrderResDto testSaveOrder(String impUid, OrderReqDto orderReqDto) {
        // 1. DTO로부터 정보 추출
        Member member = getMember();
        Coupon coupon = getCoupon(orderReqDto.getCouponId());

        // 2. 상품과 수량 조회 및 재고 감소
        List<Product> products = decreaseProductsStock(orderReqDto.getProductList());

        // 3. 주문 생성
        Order order = testCreateOrder(orderReqDto, member, coupon, products, impUid);

        // 4. 주문 저장
        orderRepository.save(order);

        // 5. 생성된 주문 반환
        return OrderResDto.from(order);
    }


    @Transactional
    public OrderResDto requestPayment(String orderId, String impUid) throws IamportResponseException, IOException {
        IamportResponse<Payment> response = getPaymentInfo(impUid);
        Long amount = response.getResponse()
                              .getAmount()
                              .longValue();

        Order order = orderRepository.getReferenceById(Long.valueOf(orderId));
        OrderResDto orderResDto = OrderResDto.from(order);

        if (!amount.equals(orderResDto.getOrderCash())) {
            order.updatePaymentCancelStatus();
            throw new InvalidPaymentException("Payment validation failed");
        } else {
            order.updatePaymentImUid(impUid);
            order.updatePaymentCompleteStatus();
        }
        return orderResDto;
    }

    @Transactional
    public OrderResDto paymentValidation(String impUid) throws IamportResponseException, IOException {
        IamportResponse<Payment> response = getPaymentInfo(impUid);
        Long amount = response.getResponse()
                              .getAmount()
                              .longValue();

        Order order = getOrder(impUid);
        OrderResDto orderResDto = OrderResDto.from(order);

        if (!amount.equals(orderResDto.getOrderCash())) {
            cancelPayment(impUid);
            order.updatePaymentCancelStatus();
            throw new InvalidPaymentException("Payment validation failed");
        } else {
            order.updatePaymentCompleteStatus();
        }
        return orderResDto;
    }

    @Transactional(readOnly = true)
    public IamportResponse getPaymentInfo(String impUid) throws IamportResponseException, IOException {
        IamportResponse<Payment> response = iamportClient.paymentByImpUid(impUid);
        return response;
    }

    @Transactional
    public void cancelPayment(String impUid) throws IamportResponseException, IOException {
        CancelData cancelData = new CancelData(impUid, true);
        iamportClient.cancelPaymentByImpUid(cancelData);
    }

    @Transactional
    public OrderResDto requestCancelPayment(String orderId,
                                            String impUid) throws IamportResponseException, IOException {
        Order order = orderRepository.getById(Long.valueOf(orderId));
        OrderResDto orderResDto = OrderResDto.from(order);
        order.updatePaymentCancelStatus();
        CancelData cancelData = new CancelData(impUid, true);
        iamportClient.cancelPaymentByImpUid(cancelData);
        return orderResDto;
    }

    @Transactional
    public OrderResDto requestCancelPaymentByOrder(String orderId) {
        Order order = orderRepository.getById(Long.valueOf(orderId));
        OrderResDto orderResDto = OrderResDto.from(order);
        order.updatePaymentCancelStatus();
        return orderResDto;
    }


    private Member getMember() {
        return SecurityUtil.getCurrentUsername()
                           .flatMap(memberRepository::findOneWithAuthoritiesByLoginId)
                           .orElseThrow(() -> new NotFoundMemberException("Member not found"));
    }


    private Address getAddress(Long addressId) {
        return addressRepository.findById(addressId)
                                .orElseThrow(() -> new IllegalArgumentException("Invalid address ID"));
    }

//    private Coupon getCoupon(Long couponId) {
//        return couponRepository.findById(couponId)
//                .orElseThrow(() -> new NotFoundCouponException("Coupon not found"));
//    }

    private Coupon getCoupon(Long couponId) {
        if (couponId == null) {
            return null;
        }
        return couponRepository.findById(couponId)
                               .orElseThrow(() -> new NotFoundCouponException("Coupon not found"));
    }

    private Order getOrder(String impUid) {
        return orderRepository.findByImpUid(impUid)
                              .orElseThrow(() -> new NotFoundOrderException("Order not found"));
    }


    @Transactional
    public List<Product> decreaseProductsStock(List<OrderProductReqDto> orderProductReqDtoList) {
        return orderProductReqDtoList.stream()
                                     .map(this::getProductAndDecreaseStock)
                                     .collect(Collectors.toList());
    }

    @Transactional
    public Product getProductAndDecreaseStock(OrderProductReqDto orderProductReqDto) {
        Product product = (Product) productRepository.findByIdWithLock(orderProductReqDto.getProductId())
                                                     .orElseThrow(() -> new NotFoundProductException(
                                                             "Product not found with id: " + orderProductReqDto.getProductId()));
        product.decreaseStock(orderProductReqDto.getProductQty());
        return product;
    }

    @Transactional
    public Order testCreateOrder(OrderReqDto orderReqDto, Member member, Coupon coupon, List<Product> products,
                                 String imUid) {
        Order order = orderReqDto.prepareOrder(member, coupon, imUid);

        for (int i = 0; i < orderReqDto.getProductList()
                                       .size(); i++) {
            order.addProduct(products.get(i), orderReqDto.getProductList()
                                                         .get(i)
                                                         .getProductQty());

        }

        if (coupon != null) {
            MemberCoupon memberCoupon = memberCouponRepository.findByMemberAndCoupon(member, coupon)
                                                              .orElseThrow(() -> new NotFoundCouponException(
                                                                      "The member does not have the coupon."));
            // 멤버 쿠폰 사용 처리
            order.applyCoupon(memberCoupon);
        } else {
            order.calculateTotal(0);
        }
        // 포인트 사용 처리
        member.usePoints(orderReqDto.getOrderPoint());
        return order;
    }

    @Transactional
    public List<MonthlyOrderSummaryDto> getMonthlyOrder() {
        return orderRepository.findMonthlyOrderSummaries();
    }

    @Transactional
    public MonthlyOrderSummaryDto getTodayOrder() {
        return orderRepository.findTodayOrderSummaries();
    }
}