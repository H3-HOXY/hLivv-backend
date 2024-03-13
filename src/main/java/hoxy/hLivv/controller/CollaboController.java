package hoxy.hLivv.controller;

import hoxy.hLivv.dto.product.CollaboDto;
import hoxy.hLivv.dto.product.ProductDto;
import hoxy.hLivv.dto.product.ProductSortCriteria;
import hoxy.hLivv.service.CollaboService;
import hoxy.hLivv.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "콜라보 API", description = "콜라보 관리와 관련된 API 목록")
public class CollaboController {
    private final CollaboService collaboService;
    private final ProductService productService;

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
    public ResponseEntity<List<CollaboDto>> getCollaboProducts(
            @Parameter(description = "몇번째 페이지", example = "1") @RequestParam(required = false, defaultValue = "1") @Min(0) int pageNo,
            @Parameter(description = "한번에 조회할 항목의 개수", example = "20") @RequestParam(required = false, defaultValue = "20") @Min(10) @Max(20) int pageSize,
            @RequestParam(required = false, defaultValue = "PRICE_DESC") ProductSortCriteria sortCriteria) {
        return ResponseEntity.ok(collaboService.getAllCollaboProduct(pageNo, pageSize, sortCriteria));
    }

    @GetMapping("/collabo/season/{categoryId}")
    @PermitAll
    public ResponseEntity<List<CollaboDto>> getCollaboProductsByCategoryId(
            @PathVariable(name = "categoryId") String categoryId,
            @Parameter(description = "몇번째 페이지", example = "1") @RequestParam(required = false, defaultValue = "1") @Min(0) int pageNo,
            @Parameter(description = "한번에 조회할 항목의 개수", example = "20") @RequestParam(required = false, defaultValue = "20") @Min(10) @Max(20) int pageSize,
            @RequestParam(required = false, defaultValue = "PRICE_DESC") ProductSortCriteria sortCriteria) {
        return ResponseEntity.ok(
                collaboService.getCollaboProductsWithCategoryId(categoryId, pageNo, pageSize, sortCriteria));
    }


    @Operation(summary = "콜라보 상품 수정")
    @PutMapping("/collabo")
    public ResponseEntity<CollaboDto> updateCollaboProduct(@RequestBody CollaboDto collaboDto) {
        var savedProduct = collaboService.saveCollaboProduct(collaboDto);
        return ResponseEntity.ok(savedProduct);
    }

}
