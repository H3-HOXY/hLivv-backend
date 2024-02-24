package hoxy.hLivv.controller;

import com.siot.IamportRestClient.exception.IamportResponseException;
import hoxy.hLivv.dto.order.OrderReqDto;
import hoxy.hLivv.dto.order.OrderResDto;
import hoxy.hLivv.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PutMapping("/order/payment/{orderId}/{impUid}")
    public ResponseEntity<OrderResDto> validatePayment(@PathVariable String orderId,@PathVariable String impUid) {
        try {
            OrderResDto orderResDto=orderService.requestPayment(orderId,impUid);
            return ResponseEntity.ok(orderResDto);
        } catch (IamportResponseException | IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/order")
    public ResponseEntity<OrderResDto> createOrder(@RequestBody OrderReqDto orderReqDto) {
        OrderResDto orderResDto = orderService.saveOrder(orderReqDto);
        return ResponseEntity.ok(orderResDto);
    }

    @PutMapping("/order/payment/cancel/{orderId}/{impUid}")
    public ResponseEntity<OrderResDto> requestCancelPayment(@PathVariable String orderId,@PathVariable String impUid){
        try {
            OrderResDto orderResDto=orderService.requestCancelPayment(orderId,impUid);
            return ResponseEntity.ok(orderResDto);
        } catch (IamportResponseException | IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/order/payment/cancel/{orderId}")
    public ResponseEntity<OrderResDto> requestCancelPaymentByOrder(@PathVariable String orderId){
        OrderResDto orderResDto=orderService.requestCancelPaymentByOrder(orderId);
        return ResponseEntity.ok(orderResDto);
    }

}
