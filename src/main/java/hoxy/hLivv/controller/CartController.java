package hoxy.hLivv.controller;

import hoxy.hLivv.dto.CartDto;
import hoxy.hLivv.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @since 2024.02.15
 * @author 반정현
 */
@RestController
@RequestMapping("/api")
@AllArgsConstructor
@Tag(name = "장바구니 API", description = "장바구니 관리와 관련된 API 목록")
public class CartController {
    private final CartService cartService;


    // 장바구니 상품 추가
    @Operation(summary = "장바구니에 상품 추가")
    @PostMapping("/cart/{productId}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<CartDto> addProductToCart(@PathVariable Long productId, @RequestParam Integer qty) {
        CartDto cartDto = cartService.addProductToCart(productId, qty);
        return ResponseEntity.ok(cartDto);
    }

    // 장바구니 상품 수량 갱신
    @Operation(summary = "장바구니 상품 수량 변경")
    @PutMapping("/cart/{productId}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<CartDto> updateCart(@PathVariable Long productId, @RequestParam Integer qty) {
        CartDto cartDto = cartService.updateCart(productId, qty);
        return ResponseEntity.ok(cartDto);
    }

    // 장바구니 상품 삭제
    @Operation(summary = "장바구니 상품 삭제")
    @DeleteMapping("/cart/{productId}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<Void> deleteFromCart(@PathVariable Long productId) {
        cartService.deleteFromCart(productId);
        return ResponseEntity.noContent().build();
    }


     //장바구니 상품 목록 삭제
    @DeleteMapping("/cart")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<Void> deleteListFromCart(@RequestBody List<Long> productIds) {
        cartService.deleteListFromCart(productIds);
        return ResponseEntity.noContent().build();
    }
}
