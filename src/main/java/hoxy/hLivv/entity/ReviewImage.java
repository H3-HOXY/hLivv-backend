package hoxy.hLivv.entity;

import jakarta.persistence.*;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "ReviewImage")
@Getter
@Setter
public class ReviewImage {
    @Id
    @GeneratedValue
    @JoinColumn(name = "review_image_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private Review review;

    @Column(name = "review_image_url")
    private String reviewImageUrl;

}
