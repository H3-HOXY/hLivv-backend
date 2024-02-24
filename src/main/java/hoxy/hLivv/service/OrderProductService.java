package hoxy.hLivv.service;

import hoxy.hLivv.entity.OrderProduct;
import hoxy.hLivv.entity.enums.DeliveryStatus;
import hoxy.hLivv.repository.OrderProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderProductService {
    private final OrderProductRepository orderProductRepository;

    @Transactional
    @Scheduled(cron = "0 0 0 * * ?")  // 매일 자정에 실행
    public void updateOrderStatusAndIncreasePoints() {
        List<OrderProduct> orderProducts = orderProductRepository.findAllByDeliveryStatus(DeliveryStatus.배송완료);
        for (OrderProduct orderProduct : orderProducts) {
            if (orderProduct.getDelivery().getDeliveryEnd().plusDays(10).isBefore(ChronoLocalDate.from(LocalDateTime.now()))) {
                orderProduct.confirmPurchase();
                long point = (long) (orderProduct.getOrder().getOrderCash() * 0.001); // 현금 결제액의 0.1%
                orderProduct.getOrder().getMember().increasePoints(point);
            }
        }
    }
}
