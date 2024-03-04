package hoxy.hLivv.controller;

import com.siot.IamportRestClient.exception.IamportResponseException;
import hoxy.hLivv.dto.order.MonthlyOrderSummaryDto;
import hoxy.hLivv.dto.order.OrderReqDto;
import hoxy.hLivv.dto.order.OrderResDto;
import hoxy.hLivv.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
@Tag(name = "주문 API", description = "주문 처리와 관련된 작업들")
public class OrderController {
    private final OrderService orderService;

    @Operation(summary = "결제 요청", security = @SecurityRequirement(name = "bearerAuth"))
    @PutMapping("/order/payment/{orderId}/{impUid}")
    public ResponseEntity<OrderResDto> validatePayment(@PathVariable String orderId, @PathVariable String impUid) {
        try {
            OrderResDto orderResDto = orderService.requestPayment(orderId, impUid);
            return ResponseEntity.ok(orderResDto);
        } catch (IamportResponseException | IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "주문 생성", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/order")
    public ResponseEntity<OrderResDto> createOrder(@RequestBody OrderReqDto orderReqDto) {
        OrderResDto orderResDto = orderService.saveOrder(orderReqDto);
        return ResponseEntity.ok(orderResDto);
    }

    @Operation(summary = "결제 취소 요청", security = @SecurityRequirement(name = "bearerAuth"))
    @PutMapping("/order/payment/cancel/{orderId}/{impUid}")
    public ResponseEntity<OrderResDto> requestCancelPayment(@PathVariable String orderId, @PathVariable String impUid) {
        try {
            OrderResDto orderResDto = orderService.requestCancelPayment(orderId, impUid);
            return ResponseEntity.ok(orderResDto);
        } catch (IamportResponseException | IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "주문의 결제 취소 요청", security = @SecurityRequirement(name = "bearerAuth"))
    @PutMapping("/order/payment/cancel/{orderId}")
    public ResponseEntity<OrderResDto> requestCancelPaymentByOrder(@PathVariable String orderId) {
        OrderResDto orderResDto = orderService.requestCancelPaymentByOrder(orderId);
        return ResponseEntity.ok(orderResDto);
    }

    @Operation(summary = "월별 주문 통계 조회", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/order/total/month")
    public ResponseEntity<List<MonthlyOrderSummaryDto>> getMonthlyOrder() {
        return ResponseEntity.ok(orderService.getMonthlyOrder());
    }

    @Operation(summary = "오늘의 주문 통계 조회", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/order/total/today")
    public ResponseEntity<MonthlyOrderSummaryDto> getTodayOrder() {
        return ResponseEntity.ok(orderService.getTodayOrder());
    }
}