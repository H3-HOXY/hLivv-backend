package hoxy.hLivv.service;


import hoxy.hLivv.dto.product.ProductDto;
import hoxy.hLivv.dto.review.ReviewDto;
import hoxy.hLivv.dto.review.ReviewImageDto;
import hoxy.hLivv.dto.review.WriteReview;
import hoxy.hLivv.entity.Member;
import hoxy.hLivv.entity.Product;
import hoxy.hLivv.entity.Review;
import hoxy.hLivv.entity.ReviewImage;
import hoxy.hLivv.repository.MemberRepository;
import hoxy.hLivv.repository.ProductRepository;
import hoxy.hLivv.repository.ReviewRepository;
import hoxy.hLivv.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
        return ProductDto.from(productRepository.save(productDto.toEntity()));
    }


    // R
    public ProductDto getProductWith(Long id) {
        return ProductDto.from(productRepository.getReferenceById(id));
    }

    public List<ProductDto> getAllProduct(int pageNo, int pageSize) {
        pageSize = Math.min(pageSize, 100);
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        return productRepository.findAll(pageable)
                                .stream()
                                .map(ProductDto::from)
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
        var loginId = SecurityUtil.getCurrentUsername()
                                  .orElseThrow(() -> new RuntimeException("로그인이 필요합니다."));
        var member = memberRepository.findByLoginId(loginId)
                                     .orElseThrow(() -> new RuntimeException("회원정보를 찾을 수 없습니다."));

        var product = productRepository.findById(productId)
                                       .orElseThrow(() -> new RuntimeException("상품이 존재하지 않습니다."));

        reviewRepository.getReviewByProductAndMember(product, member)
                        .ifPresent(review -> {
                            throw new RuntimeException("이미 리뷰가 존재합니다.");
                        });

        Date now = Calendar.getInstance()
                           .getTime();
        return new WriteReview.Response(reviewRepository.save(toReview(request, member, product, now)));
    }


    @Transactional
    public WriteReview.Response updateReview(Long productId, WriteReview.Request request) {
        var loginId = SecurityUtil.getCurrentUsername()
                                  .orElseThrow(() -> new RuntimeException("로그인이 필요합니다."));

        var member = memberRepository.findByLoginId(loginId)
                                     .orElseThrow(() -> new RuntimeException("회원정보를 찾을 수 없습니다."));

        var product = productRepository.findById(productId)
                                       .orElseThrow(() -> new RuntimeException("상품이 존재하지 않습니다."));

        var review = reviewRepository.getReviewByProductAndMember(product, member)
                                     .orElseThrow(() -> new RuntimeException("리뷰가 존재하지 않습니다."));


        review.updateImage(request.getReviewImages()
                                  .stream()
                                  .map(ReviewImageDto::getReviewImageUrl)
                                  .toList());

        review.setReviewText(request.getReviewText());
        review.setUpdatedDate(Calendar.getInstance()
                                      .getTime());

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

    private static Review toReview(WriteReview.Request request, Member member, Product product, Date now) {
        var review = new Review(member, product, request.getReviewImages()
                                                        .stream()
                                                        .map(img -> {
                                                            var reviewImage = new ReviewImage();
                                                            reviewImage.setReviewImageUrl(img.getReviewImageUrl());
                                                            return reviewImage;
                                                        })
                                                        .toArray(ReviewImage[]::new));
        review.setReviewDate(now);
        review.setUpdatedDate(now);
        review.setReviewText(request.getReviewText());
        review.setStar(request.getStar());
        return review;
    }
}
