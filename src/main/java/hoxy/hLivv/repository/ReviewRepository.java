package hoxy.hLivv.repository;

import hoxy.hLivv.entity.Member;
import hoxy.hLivv.entity.Product;
import hoxy.hLivv.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query(value = "SELECT REVIEW_ID, MEMBER_ID, PRODUCT_ID, REVIEW_DATE, UPDATED_DATE, REVIEW_TEXT , STAR FROM REVIEW WHERE PRODUCT_ID= :PRODUCT_ID", nativeQuery = true)
    List<Review> getReviewByProductId(@Param("PRODUCT_ID") Long id);

    Optional<Review> getReviewByProductAndMember(Product product, Member member);

}
