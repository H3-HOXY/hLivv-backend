package hoxy.hLivv.repository;

import hoxy.hLivv.dto.order.MonthlyOrderSummaryDto;
import hoxy.hLivv.entity.Member;
import hoxy.hLivv.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @EntityGraph(attributePaths = "products")
        // lazy가 아닌 eager 조회
    List<Order> findWithProductsByMember(Member member);//멤버 아이디로 주문목록 가져올 때 상품도 가져오기
    @EntityGraph(attributePaths = "products")
    Page<Order> findByMember(Member member, Pageable pageable);
    Optional<Order> findByImpUid(String impUid);


    @Query("SELECT new hoxy.hLivv.dto.order.MonthlyOrderSummaryDto(" +
            "YEAR(o.requestDate) AS year, " +
            "MONTH(o.requestDate) AS month, " +
            "0 AS day, " +
            "SUM(o.orderTotal) AS orderTotal, " +
            "COUNT(*) AS cnt) " +
            "FROM Order o " +
            "where YEAR(o.requestDate) = 2024 " +
            "GROUP BY YEAR(o.requestDate), MONTH(o.requestDate) " +
            "ORDER BY YEAR(o.requestDate), MONTH(o.requestDate)")
    List<MonthlyOrderSummaryDto> findMonthlyOrderSummaries();


    @Query("SELECT new hoxy.hLivv.dto.order.MonthlyOrderSummaryDto(" +
            "YEAR(o.requestDate) AS year, " +
            "MONTH(o.requestDate) AS month, " +
            "DAY(o.requestDate) AS day, " +
            "SUM(o.orderTotal) AS orderTotal, " +
            "COUNT(*) AS cnt) " +
            "FROM Order o " +
            "where o.requestDate >= CURRENT_DATE " +
            "GROUP BY YEAR(o.requestDate), MONTH(o.requestDate), DAY(o.requestDate) " +
            "ORDER BY YEAR(o.requestDate), MONTH(o.requestDate), DAY(o.requestDate)")
    MonthlyOrderSummaryDto findTodayOrderSummaries();
}