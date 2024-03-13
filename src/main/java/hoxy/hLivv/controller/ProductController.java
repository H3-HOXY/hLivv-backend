package hoxy.hLivv.controller;

import hoxy.hLivv.dto.product.ProductDto;
import hoxy.hLivv.dto.product.ProductSortCriteria;
import hoxy.hLivv.dto.review.ReviewDto;
import hoxy.hLivv.dto.review.ReviewImageDto;
import hoxy.hLivv.dto.review.WriteReview;
import hoxy.hLivv.service.ProductService;
import hoxy.hLivv.service.S3Service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
@AllArgsConstructor
@Tag(name = "상품 API", description = "상품 관리와 관련된 작업들")
public class ProductController {
    private final ProductService productService;
    private final S3Service s3Service;

    @Operation(summary = "상품 생성")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "성공", content = {@Content(schema = @Schema(implementation = ProductDto.class))})})
    @PostMapping("/product")
    @PreAuthorize("hasAnyRole('USER','ADMIN', 'MANAGER')")
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productDto) {
        var savedProduct = productService.saveProduct(productDto);
        return ResponseEntity.ok(savedProduct);
    }

    @Operation(summary = "상품 업데이트")
    @PutMapping("/product/{productId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable Long productId, @RequestBody ProductDto productDto) {
        var savedProduct = productService.saveProduct(productDto);
        return ResponseEntity.ok(savedProduct);
    }

    @Operation(summary = "상품 조회")
    @GetMapping("/product/{productId}")
    public ResponseEntity<ProductDto> getProduct(@PathVariable Long productId) {
        var product = productService.getProductWith(productId);
        return ResponseEntity.ok(product);
    }

    @Operation(summary = "전체 상품 조회")
    @GetMapping("/product")
    public ResponseEntity<List<ProductDto>> getProduct(
            @RequestParam(required = false, defaultValue = "1") @Min(0) int pageNo,
            @RequestParam(required = false, defaultValue = "20") @Min(10) @Max(40) int pageSize,
            @RequestParam(required = false, defaultValue = "PRICE_DESC") ProductSortCriteria sortCriteria) {
        return ResponseEntity.ok(productService.getAllProduct(pageNo, pageSize, sortCriteria));
    }

    @Operation(summary = "상품에 리뷰 작성")
    @PostMapping(value = "/product/{productId}/review", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('USER','ADMIN', 'MANAGER')")
    public ResponseEntity<WriteReview.Response> writeReviewToProduct(@PathVariable(name = "productId") Long productId,
                                                                     @RequestParam(name = "reviewText") String reviewText,
                                                                     @RequestParam(name = "star") Integer star,
                                                                     @RequestParam(name = "reviewImages", required = false) List<ReviewImageDto> reviewImages,
                                                                     @RequestPart(value = "imageFiles", required = false) MultipartFile[] imageFiles) {
        var request = new WriteReview.Request();
        request.setReviewText(reviewText);
        request.setStar(star);
        if (reviewImages == null) {
            reviewImages = List.of();
        }
        request.setReviewImages(reviewImages);
        return ResponseEntity.ok(productService.writeReviewToProduct(productId, request,
                                                                     Arrays.stream(imageFiles)
                                                                           .toList()));
    }


    @Operation(summary = "상품 리뷰 조회")
    @GetMapping("/product/{productId}/review")
    public ResponseEntity<List<ReviewDto>> getReviewsByProductId(@PathVariable(name = "productId") Long productId) {
        return ResponseEntity.ok(productService.getReviewsByProductId(productId));
    }
}