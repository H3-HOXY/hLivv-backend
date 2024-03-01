package hoxy.hLivv.controller;

import hoxy.hLivv.dto.product.CollaboDto;
import hoxy.hLivv.service.CollaboService;
import jakarta.annotation.security.PermitAll;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class CollaboController {
    private final CollaboService collaboService;

    @PostMapping("/collabo")
    public ResponseEntity<CollaboDto> createCollaboProduct(@RequestBody CollaboDto collaboDto) {
        var savedProduct = collaboService.saveCollaboProduct(collaboDto);
        return ResponseEntity.ok(savedProduct);
    }

    @GetMapping("/collabo/{productId}")
    @PermitAll
    public ResponseEntity<CollaboDto> getCollaboProduct(@PathVariable Long productId) {
        var product = collaboService.getCollaboProductWith(productId);
        return ResponseEntity.ok(product);
    }

    @GetMapping("/collabo")
    @PermitAll
    public ResponseEntity<List<CollaboDto>> getCollaboProducts() {
        return ResponseEntity.ok(collaboService.getAllCollaboProduct());
    }

    @PutMapping("/collabo")
    public ResponseEntity<CollaboDto> updateCollaboProduct(@RequestBody CollaboDto collaboDto) {
        var savedProduct = collaboService.saveCollaboProduct(collaboDto);
        return ResponseEntity.ok(savedProduct);
    }

}
