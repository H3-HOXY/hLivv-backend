package hoxy.hLivv.dto.review;

import hoxy.hLivv.entity.Review;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ReviewDto {
    private String writer;
    private Date reviewDate;
    private Date updatedDate;
    private List<ReviewImageDto> reviewImages = new ArrayList<>();
    private String reviewText;
    private Integer star;

    public ReviewDto(Review review) {
        this.writer = review.getMember()
                            .getLoginId();
        this.reviewDate = review.getReviewDate();
        this.updatedDate = review.getUpdatedDate();
        this.reviewImages = review.getReviewImages()
                                  .stream()
                                  .map(img -> new ReviewImageDto(img.getReviewImageUrl()))
                                  .toList();
        this.reviewText = review.getReviewText();
        this.star = review.getStar();
    }
}
