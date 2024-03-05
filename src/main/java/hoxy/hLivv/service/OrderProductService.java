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

}
