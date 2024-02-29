package hoxy.hLivv.dto.review;

import hoxy.hLivv.entity.Review;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class WriteReview {
    @Data
    public static class Request {
        private String reviewText;
        private List<ReviewImageDto> reviewImages = new ArrayList<>();
        private Integer star;
    }

    @Data
    public static class Response {
        private String writer;
        private String reviewText;
        private Integer star;
        private Date reviewDate;
        private Date updatedDate;
        private List<ReviewImageDto> reviewImages = new ArrayList<>();

        public Response(Review review) {
            this.writer = review.getMember()
                                .getLoginId();
            this.reviewText = review.getReviewText();
            this.star = review.getStar();
            this.reviewDate = review.getReviewDate();
            this.updatedDate = review.getUpdatedDate();
            this.reviewImages = review.getReviewImages()
                                      .stream()
                                      .map(img -> new ReviewImageDto(img.getReviewImageUrl()))
                                      .toList();
        }
    }
}
