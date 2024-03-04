package hoxy.hLivv.controller;

import hoxy.hLivv.dto.product.CollaboDto;
import hoxy.hLivv.service.CollaboService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.PermitAll;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
@Tag(name = "콜라보 API", description = "콜라보 관리와 관련된 API 목록")
public class CollaboController {
    private final CollaboService collaboService;

    @Operation(summary = "콜라보 상품 등록")
    @PostMapping("/collabo")
    public ResponseEntity<CollaboDto> createCollaboProduct(@RequestBody CollaboDto collaboDto) {
        var savedProduct = collaboService.saveCollaboProduct(collaboDto);
        return ResponseEntity.ok(savedProduct);
    }

    @Operation(summary = "productId로 콜라보 상품 조회")
    @GetMapping("/collabo/{productId}")
    @PermitAll
    public ResponseEntity<CollaboDto> getCollaboProduct(@PathVariable Long productId) {
        var product = collaboService.getCollaboProductWith(productId);
        return ResponseEntity.ok(product);
    }

    @Operation(summary = "모든 콜라보 상품 조회")
    @GetMapping("/collabo")
    @PermitAll
    public ResponseEntity<List<CollaboDto>> getCollaboProducts() {
        return ResponseEntity.ok(collaboService.getAllCollaboProduct());
    }


    @Operation(summary = "콜라보 상품 수정")
    @PutMapping("/collabo")
    public ResponseEntity<CollaboDto> updateCollaboProduct(@RequestBody CollaboDto collaboDto) {
        var savedProduct = collaboService.saveCollaboProduct(collaboDto);
        return ResponseEntity.ok(savedProduct);
    }

}
