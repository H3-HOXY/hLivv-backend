package hoxy.hLivv.repository;

import hoxy.hLivv.entity.Member;
import hoxy.hLivv.entity.Order;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @EntityGraph(attributePaths = "products") // lazy가 아닌 eager 조회
    List<Order> findWithProductsByMemberId(Long MemberId);//멤버 아이디로 주문목록 가져올 때 상품도 가져오기
}