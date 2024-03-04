package hoxy.hLivv.controller;

import hoxy.hLivv.dto.product.ProductDto;
import hoxy.hLivv.dto.review.ReviewDto;
import hoxy.hLivv.dto.review.WriteReview;
import hoxy.hLivv.service.ProductService;
import hoxy.hLivv.service.S3Service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
@AllArgsConstructor
@Tag(name = "상품 API", description = "상품 관리와 관련된 작업들")
public class ProductController {
    private final ProductService productService;
    private final S3Service s3Service;

    @Operation(summary = "상품 생성", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "성공", content = {@Content(schema = @Schema(implementation = ProductDto.class))})})
    @PostMapping("/product")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productDto) {
        var savedProduct = productService.saveProduct(productDto);
        return ResponseEntity.ok(savedProduct);
    }

    @Operation(summary = "상품 업데이트", security = @SecurityRequirement(name = "bearerAuth"))
    @PutMapping("/product/{productId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable Long productId, @RequestBody ProductDto productDto) {
        var savedProduct = productService.saveProduct(productDto);
        return ResponseEntity.ok(savedProduct);
    }

    @Operation(summary = "상품 조회", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/product/{productId}")
    public ResponseEntity<ProductDto> getProduct(@PathVariable Long productId) {
        var product = productService.getProductWith(productId);
        return ResponseEntity.ok(product);
    }

    @Operation(summary = "전체 상품 조회", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/product")
    public ResponseEntity<List<ProductDto>> getProduct(@RequestParam(required = false, defaultValue = "0", value = "page") int pageNo, @RequestParam(required = false, defaultValue = "20", value = "pageSize") int pageSize) {
        return ResponseEntity.ok(productService.getAllProduct(pageNo, pageSize));
    }

    @Operation(summary = "상품에 리뷰 작성", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping(value = "/product/{productId}/review")
    @PreAuthorize("hasAnyRole('USER','ADMIN', 'MANAGER')")
    public ResponseEntity<WriteReview.Response> writeReviewToProduct(@PathVariable(name = "productId") Long productId,
                                                                     WriteReview.Request writeReviewRequest,
                                                                     @RequestParam("imageFiles") List<MultipartFile> imageFiles) {
        return ResponseEntity.ok(productService.writeReviewToProduct(productId, writeReviewRequest, imageFiles));
    }

    @Operation(summary = "상품 리뷰 조회", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/product/{productId}/review")
    public ResponseEntity<List<ReviewDto>> getReviewsByProductId(@PathVariable(name = "productId") Long productId) {
        return ResponseEntity.ok(productService.getReviewsByProductId(productId));
    }
}