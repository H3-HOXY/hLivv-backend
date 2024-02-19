package hoxy.hLivv.controller;

import hoxy.hLivv.dto.product.ProductDto;
import hoxy.hLivv.dto.review.ReviewDto;
import hoxy.hLivv.dto.review.WriteReview;
import hoxy.hLivv.service.ProductService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class ProductController {
    private final ProductService productService;

    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "성공", content = {@Content(schema = @Schema(implementation = ProductDto.class))})})
    @PostMapping("/product")
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productDto) {
        var savedProduct = productService.saveProduct(productDto);
        return ResponseEntity.ok(savedProduct);
    }

    @PutMapping("/product/{productId}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable Long productId, @RequestBody ProductDto productDto) {
        var savedProduct = productService.saveProduct(productDto);
        return ResponseEntity.ok(savedProduct);
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<ProductDto> getProduct(@PathVariable Long productId) {
        var product = productService.getProductWith(productId);
        return ResponseEntity.ok(product);
    }

    @GetMapping("/product")
    public ResponseEntity<List<ProductDto>> getProduct(@RequestParam(required = false, defaultValue = "0", value = "page") int pageNo
            , @RequestParam(required = false, defaultValue = "20", value = "pageSize") int pageSize) {
        return ResponseEntity.ok(productService.getAllProduct(pageNo, pageSize));
    }

    @PostMapping("/product/{productId}/review")
    public ResponseEntity<WriteReview.Response> writeReviewToProduct(@PathVariable(name = "productId") Long productId, @RequestBody WriteReview.Request writeReviewRequest) {
        return ResponseEntity.ok(productService.writeReviewToProduct(productId, writeReviewRequest));
    }

    @PutMapping("/product/{productId}/review")
    public ResponseEntity<WriteReview.Response> updateProductReview(@PathVariable(name = "productId") Long productId, @RequestBody WriteReview.Request writeReviewRequest) {
        return ResponseEntity.ok(productService.updateReview(productId, writeReviewRequest));
    }

    @GetMapping("/product/{productId}/review")
    public ResponseEntity<List<ReviewDto>> getReviewsByProductId(@PathVariable(name = "productId") Long productId) {
        return ResponseEntity.ok(productService.getReviewsByProductId(productId));
    }

}
