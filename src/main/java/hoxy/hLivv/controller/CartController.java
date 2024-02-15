package hoxy.hLivv.controller;

import hoxy.hLivv.dto.CartDto;
import hoxy.hLivv.service.CartService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class CartController {
    private final CartService cartService;


    // 장바구니 상품 추가
    @PostMapping("/cart/{productId}")
    public ResponseEntity<CartDto> addProductToCart(@PathVariable Long productId, @RequestParam Integer qty, HttpServletRequest request) {
        CartDto cartDto=cartService.addProductToCart(productId,qty);
        return ResponseEntity.ok(cartDto);
    }

    // 장바구니 상품 수량 갱신
    @PutMapping("/cart/{productId}")
    public ResponseEntity<CartDto> updateCart(@PathVariable Long productId, @RequestParam Integer qty, HttpServletRequest request) {
        CartDto cartDto=cartService.updateCart(productId, qty);
        return ResponseEntity.ok(cartDto);
    }

    // 장바구니 상품 삭제
    @DeleteMapping("/cart/{productId}")
    public ResponseEntity<Void> deleteFromCart(@PathVariable Long productId,HttpServletRequest request) {
        cartService.deleteFromCart(productId);
        return ResponseEntity.noContent().build();
    }
}
