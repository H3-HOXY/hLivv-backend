package hoxy.hLivv.controller;

import hoxy.hLivv.dto.CategoryDto;
import hoxy.hLivv.dto.product.ProductDto;
import hoxy.hLivv.dto.product.ProductSortCriteria;
import hoxy.hLivv.service.CategoryService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.PermitAll;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api")
@AllArgsConstructor
@Tag(name = "카테고리 API", description = "카테고리 관련 API 목록")
public class CategoryController {
    private final CategoryService categoryService;

    @Operation(summary = "카테고리 항목 추가", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/category")
    @PreAuthorize("hasAnyRole('USER','ADMIN', 'MANAGER')")
    public ResponseEntity<CategoryDto> addCategory(@RequestBody CategoryDto categoryDto) {
        return ResponseEntity.ok(categoryService.addCategory(categoryDto));
    }

    @Operation(summary = "모든 카테고리 조회")
    @GetMapping("/category")
    public ResponseEntity<List<CategoryDto>> getCategories() {
        return ResponseEntity.ok(categoryService.getCategories());
    }

    @Operation(summary = "categoryId로 카테고리 조회")
    @GetMapping("/category/{categoryId}/products")
    public ResponseEntity<List<ProductDto>> getProductsWithCategory(@PathVariable("categoryId") String categoryId,
                                                                    @RequestParam(required = false, defaultValue = "1") @Min(0) int pageNo,
                                                                    @RequestParam(required = false, defaultValue = "20") @Min(10) @Max(20) int pageSize,
                                                                    @RequestParam(required = false, defaultValue = "PRICE_DESC") ProductSortCriteria sortCriteria) throws Exception {
        return ResponseEntity.ok(categoryService.getProductsByCategory(categoryId, pageNo, pageSize, sortCriteria));
    }
}
