package hoxy.hLivv.controller;

import hoxy.hLivv.dto.DeliveryDto;
import hoxy.hLivv.service.DeliveryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
@Tag(name = "배송 API", description = "배송 관리와 관련된 작업들")
public class DeliveryController {
    private final DeliveryService deliveryService;

    @Operation(summary = "배송중 업데이트")
    @PutMapping("/delivery/{deliveryId}/progress")
    public ResponseEntity<DeliveryDto> updateDeliveryToInProgress(@PathVariable Long deliveryId) {
        DeliveryDto deliveryDto=deliveryService.updateDeliveryToInProgress(deliveryId);
        return ResponseEntity.ok(deliveryDto);
    }

    @Operation(summary = "배송완료 업데이트")
    @PutMapping("/delivery/{deliveryId}/completed")
    public ResponseEntity<DeliveryDto> updateDeliveryToCompleted(@PathVariable Long deliveryId) {
        DeliveryDto deliveryDto=deliveryService.updateDeliveryToCompleted(deliveryId);
        return ResponseEntity.ok(deliveryDto);
    }

}
