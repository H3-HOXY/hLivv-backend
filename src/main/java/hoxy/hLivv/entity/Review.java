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

    public Review(Long memberId, Long productId, ReviewImage... images) {
        member = Member.builder()
                       .memberId(memberId)
                       .build();
        product = Product.builder()
                         .id(productId)
                         .build();
        for (var image : images) {
            image.setReview(this);
            reviewImages.add(image);
        }
    }
}
