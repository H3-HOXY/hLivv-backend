package hoxy.hLivv.service;

import hoxy.hLivv.dto.DeliveryDto;
import hoxy.hLivv.entity.Delivery;
import hoxy.hLivv.exception.NotFoundDeliveryException;
import hoxy.hLivv.repository.DeliveryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeliveryService {
    private final DeliveryRepository deliveryRepository;
    @Transactional
    public DeliveryDto updateDeliveryToInProgress(Long deliveryId) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new NotFoundDeliveryException("해당 배송 정보를 찾을 수 없습니다."));
        delivery.updateDeliveryProgressStatus();
        return DeliveryDto.from(delivery);
    }

    @Transactional
    public DeliveryDto updateDeliveryToCompleted(Long deliveryId) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new NotFoundDeliveryException("해당 배송 정보를 찾을 수 없습니다."));
        delivery.updateDeliveryCompletedStatus();
        return DeliveryDto.from(delivery);
    }
}
