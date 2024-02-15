package hoxy.hLivv.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "Review")
@Getter
@Setter
public class Review {
    @Id
    @GeneratedValue
    @Column(name = "review_id")
    Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "review_date")
    private Date reviewDate;

    @Column(name = "updated_date")
    private Date updatedDate;

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL)
    private List<ReviewImage> reviewImages = new ArrayList<>();

    @Column(name = "review_text")
    private String reviewText;

    @Column(name = "star")
    private Integer star;

    public Review(Member member, Product product, ReviewImage... images) {
        this.member = member;
        this.product = product;
        for (var image : images) {
            image.setReview(this);
            reviewImages.add(image);
        }
    }

    public void updateImage(List<String> newImageList) {
        var removedList = reviewImages.stream()
                                      .filter(image -> !newImageList.contains(image.getReviewImageUrl()))
                                      .peek(reviewImage -> reviewImage.setReview(null))
                                      .toList();
        reviewImages.removeAll(removedList);

        var filteredList = reviewImages.stream()
                                       .map(ReviewImage::getReviewImageUrl)
                                       .toList();
        newImageList.stream()
                    .filter(image -> !filteredList.contains(image))
                    .forEach(image -> {
                        var reviewImage = new ReviewImage();
                        reviewImage.setReview(this);
                        reviewImage.setReviewImageUrl(image);
                        reviewImages.add(reviewImage);
                    });
    }
}
