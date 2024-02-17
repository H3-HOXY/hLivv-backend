package hoxy.hLivv.repository;

import hoxy.hLivv.entity.Cart;
import hoxy.hLivv.entity.Member;
import hoxy.hLivv.entity.compositekey.CartId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CartRepository extends JpaRepository<Cart, CartId> {
    @Query("SELECT c FROM Cart c WHERE c.member = :member ORDER BY COALESCE(c.lastModifiedDate, c.createdDate) DESC")
    Page<Cart> findByMember(@Param("member") Member member, Pageable pageable);
}
