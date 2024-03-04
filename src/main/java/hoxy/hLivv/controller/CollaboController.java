package hoxy.hLivv.controller;

import hoxy.hLivv.dto.product.CollaboDto;
import hoxy.hLivv.dto.product.ProductDto;
import hoxy.hLivv.dto.product.ProductSortCriteria;
import hoxy.hLivv.service.CollaboService;
import hoxy.hLivv.service.ProductService;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class CollaboController {
    private final CollaboService collaboService;
    private final ProductService productService;

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

    @GetMapping("/collabo/{productId}/items")
    @PermitAll
    public ResponseEntity<List<ProductDto>> getCollaboProductItems(@PathVariable Long productId) {
        var product = collaboService.getCollaboProductWith(productId)
                                    .getCollaboProduct()
                                    .stream()
                                    .map(item -> productService.getProductWith(item.getProductId()))
                                    .toList();
        return ResponseEntity.ok(product);
    }


    @GetMapping("/collabo")
    @PermitAll
    public ResponseEntity<List<CollaboDto>> getCollaboProducts(@RequestParam(required = false, defaultValue = "1") @Min(0) int pageNo,
                                                               @RequestParam(required = false, defaultValue = "20") @Min(10) @Max(20) int pageSize,
                                                               @RequestParam(required = false, defaultValue = "PRICE_DESC") ProductSortCriteria sortCriteria) {
        return ResponseEntity.ok(collaboService.getAllCollaboProduct(pageNo, pageSize, sortCriteria));
    }

    @PutMapping("/collabo")
    public ResponseEntity<CollaboDto> updateCollaboProduct(@RequestBody CollaboDto collaboDto) {
        var savedProduct = collaboService.saveCollaboProduct(collaboDto);
        return ResponseEntity.ok(savedProduct);
    }

}
