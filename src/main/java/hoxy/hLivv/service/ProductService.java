package hoxy.hLivv.service;


import hoxy.hLivv.dto.product.ProductDto;
import hoxy.hLivv.dto.product.ProductSortCriteria;
import hoxy.hLivv.dto.review.ReviewDto;
import hoxy.hLivv.dto.review.ReviewImageDto;
import hoxy.hLivv.dto.review.WriteReview;
import hoxy.hLivv.entity.Member;
import hoxy.hLivv.entity.Product;
import hoxy.hLivv.entity.Review;
import hoxy.hLivv.entity.ReviewImage;
import hoxy.hLivv.exception.NotFoundProductException;
import hoxy.hLivv.repository.MemberRepository;
import hoxy.hLivv.repository.ProductRepository;
import hoxy.hLivv.repository.ReviewRepository;
import hoxy.hLivv.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    private final CategoryService categoryService;

    private final S3Service s3Service;

    // C
    @Transactional
    public ProductDto saveProduct(ProductDto productDto) {
        String categoryId = productDto.getCategory()
                                      .getId();
        try {
            if (categoryId == null) throw new RuntimeException("카테고리를 찾을 수 없습니다.");
            categoryService.getCategory(categoryId);
            return ProductDto.from(productRepository.save(productDto.toEntity()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }




    // R
    public ProductDto getProductWith(Long id) {
        return ProductDto.from(productRepository.getReferenceById(id));
    }


    public List<ProductDto> getAllProduct(int pageNo, int pageSize, ProductSortCriteria sortCriteria) {
        Sort sort = sortCriteria.toOrder();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        return productRepository.findAll(pageable)
                                .stream()
                                .map(ProductDto::from)
                                .toList();
    }

    public Page<ProductDto> getAllProductsWithPagination(Pageable pageable) {
        return productRepository.findAll(pageable)
                                .map(ProductDto::from);
    }


    // U
    @Transactional
    public ProductDto updateProduct(Long productId, ProductDto productDto) {
        return null;
    }

    @Transactional
    public Product updateProduct(ProductDto productDto) {
        Product product = productRepository.findById(productDto.getId())
                                           .orElseThrow(() -> new NotFoundProductException("Product not found"));

        product.setName(productDto.getName());
        product.setPrice(productDto.getPrice());
        product.setDiscountPercent(productDto.getDiscountPercent());
        product.setStockQuantity(productDto.getStockQuantity());

        return productRepository.save(product);
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
    public WriteReview.Response writeReviewToProduct(Long productId, WriteReview.Request request, List<MultipartFile> imageFiles) {
        var loginId = SecurityUtil.getCurrentUsername()
                                  .orElseThrow(() -> new RuntimeException("로그인이 필요합니다."));
        var member = memberRepository.findByLoginId(loginId)
                                     .orElseThrow(() -> new RuntimeException("회원정보를 찾을 수 없습니다."));

        var product = productRepository.findById(productId)
                                       .orElseThrow(() -> new RuntimeException("상품이 존재하지 않습니다."));

        List<ReviewImageDto> uploadedFiles = List.of();

        try {
            // 이미 리뷰가 있다면 업데이트를 시도합니다.
            var currentReview = reviewRepository.getReviewByProductAndMember(product, member);
            if (currentReview.isPresent()) {
                return updateReview(productId, request, imageFiles);
            }

            uploadedFiles = imageFiles.stream()
                                      .map(multipartFile -> {
                                          try {
                                              return s3Service.uploadImage(S3Service.ImagePath.REVIEW, multipartFile);
                                          } catch (IOException ignored) {
                                              log.error("Failed to upload image to S3");
                                              return "";
                                          }
                                      })
                                      .filter(s -> !s.isEmpty())
                                      .map(ReviewImageDto::new)
                                      .toList();

            request.setReviewImages(uploadedFiles);
            // 없다면 새로운 리뷰를 작성합니다.
            var saved = reviewRepository.save(toReview(request, member, product, Calendar.getInstance()
                                                                                         .getTime()));
            return new WriteReview.Response(saved);
        } catch (Exception e) {
            log.error("리뷰를 저장하지 못했습니다. 이미지를 삭제합니다", e);
            uploadedFiles.stream()
                         .map(ReviewImageDto::getReviewImageUrl)
                         .forEach(s3Service::deleteImage);
            return null;
        }
    }


    @Transactional
    public WriteReview.Response updateReview(Long productId, WriteReview.Request request, List<MultipartFile> imageFiles) {
        var loginId = SecurityUtil.getCurrentUsername()
                                  .orElseThrow(() -> new RuntimeException("로그인이 필요합니다."));

        var member = memberRepository.findByLoginId(loginId)
                                     .orElseThrow(() -> new RuntimeException("회원정보를 찾을 수 없습니다."));

        var product = productRepository.findById(productId)
                                       .orElseThrow(() -> new RuntimeException("상품이 존재하지 않습니다."));

        var review = reviewRepository.getReviewByProductAndMember(product, member)
                                     .orElseThrow(() -> new RuntimeException("리뷰가 존재하지 않습니다."));


        var reviewImages = review.getReviewImages();

        // 기존의 이미지 중 사용되지 않는 이미지를 제거합니다.
        var removedList = reviewImages.stream()
                                      .filter(image -> request.getReviewImages()
                                                              .stream()
                                                              .noneMatch(img -> img.getReviewImageUrl()
                                                                                   .equals(image.getReviewImageUrl())))
                                      .toList();
        reviewImages.removeAll(removedList);

        // 새로운 이미지를 업로드합니다.
        var uploadedImage = imageFiles.stream()
                                      .map(multipartFile -> {
                                          try {
                                              return s3Service.uploadImage(S3Service.ImagePath.REVIEW, multipartFile);
                                          } catch (IOException ignored) {
                                              log.error("Failed to upload image to S3");
                                              return "";
                                          }
                                      })
                                      .filter(s -> !s.isEmpty())
                                      .map(image -> {
                                          var reviewImage = new ReviewImage();
                                          reviewImage.setReview(review);
                                          reviewImage.setReviewImageUrl(image);
                                          reviewImages.add(reviewImage);
                                          return reviewImage;
                                      })
                                      .toList();

        review.setReviewText(request.getReviewText());
        review.setUpdatedDate(Calendar.getInstance()
                                      .getTime());

        review.setStar(request.getStar());

        try {
            var saved = reviewRepository.save(review);
            // 리뷰 업데이트에 성공한 경우 사용되지 않는 이미지를 삭제합니다.
            removedList.stream()
                       .forEach(image -> {
                           image.setReview(null);
                           s3Service.deleteImage(image.getReviewImageUrl());
                       });
            return new WriteReview.Response(saved);
        } catch (Exception e) {
            // 리뷰 업데이트에 실패한 경우 새로 업로드한 이미지가 있다면 삭제합니다.
            uploadedImage.stream()
                         .map(ReviewImage::getReviewImageUrl)
                         .forEach(s3Service::deleteImage);
            throw new RuntimeException("리뷰를 업데이트하지 못했습니다.");
        }
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
