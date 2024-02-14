package hoxy.hLivv.service;


import hoxy.hLivv.dto.product.ProductDto;
import hoxy.hLivv.dto.review.ReviewDto;
import hoxy.hLivv.dto.review.WriteReview;
import hoxy.hLivv.entity.Product;
import hoxy.hLivv.entity.Review;
import hoxy.hLivv.entity.ReviewImage;
import hoxy.hLivv.repository.MemberRepository;
import hoxy.hLivv.repository.ProductRepository;
import hoxy.hLivv.repository.ReviewRepository;
import hoxy.hLivv.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;

    // C
    @Transactional
    public ProductDto saveProduct(ProductDto productDto) {
        var product = productRepository.save(productDto.toEntity());
        return product.toDto();
    }


    // R
    public ProductDto getProductWith(Long id) {
        return productRepository.getReferenceById(id)
                                .toDto();
    }

    public List<ProductDto> getAllProduct() {
        return productRepository.findAll()
                                .stream()
                                .map(Product::toDto)
                                .toList();
    }


    // U
    @Transactional
    public ProductDto updateProduct(Long productId, ProductDto productDto) {
        return null;
    }

    // D
    @Transactional
    public void removeProduct() {

    }

    /*
     리뷰 관련 기능들
     */

    /***
     *
     * @param productId
     * @param request
     * @return
     */
    @Transactional
    public WriteReview.Response writeReviewToProduct(Long productId, WriteReview.Request request) {

        var loginId = SecurityUtil.getCurrentUsername();
        if (loginId.isEmpty()) return null;

        var memberId = memberRepository.getMemberByLoginId(loginId.get());
        if (memberId.isEmpty()) return null;

        Date now = Calendar.getInstance()
                           .getTime();
        var review = new Review(memberId.get(), productId, request.getReviewImages()
                                                                  .stream()
                                                                  .map(img -> {
                                                                      var reviewImage = new ReviewImage();
                                                                      reviewImage.setReviewImageUrl(img.getReviewImageUrl());
                                                                      return reviewImage;
                                                                  })
                                                                  .toArray(ReviewImage[]::new));
        review.getMember()
              .setLoginId(loginId.get());
        review.setReviewDate(now);
        review.setUpdatedDate(now);
        review.setReviewText(request.getReviewText());
        review.setStar(request.getStar());
        return new WriteReview.Response(reviewRepository.save(review));
    }

    @Transactional(readOnly = true)
    public List<ReviewDto> getReviewsByProductId(Long productId) {
        var reviewList = reviewRepository.getReviewByProductId(productId);
        return reviewList.stream()
                         .map(ReviewDto::new)
                         .toList();
    }
}
