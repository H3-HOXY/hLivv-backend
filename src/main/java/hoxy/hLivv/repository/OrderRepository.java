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
            "YEAR(o.orderDate) AS year, " +
            "MONTH(o.orderDate) AS month, " +
            "0 AS day, " +
            "SUM(o.orderTotal) AS orderTotal, " +
            "COUNT(*) AS cnt) " +
            "FROM Order o " +
            "where YEAR(o.orderDate) = 2024 " +
            "GROUP BY YEAR(o.orderDate), MONTH(o.orderDate) " +
            "ORDER BY YEAR(o.orderDate), MONTH(o.orderDate)")
    List<MonthlyOrderSummaryDto> findMonthlyOrderSummaries();


    @Query("SELECT new hoxy.hLivv.dto.order.MonthlyOrderSummaryDto(" +
            "YEAR(o.orderDate) AS year, " +
            "MONTH(o.orderDate) AS month, " +
            "DAY(o.orderDate) AS day, " +
            "SUM(o.orderTotal) AS orderTotal, " +
            "COUNT(*) AS cnt) " +
            "FROM Order o " +
            "where o.orderDate >= CURRENT_DATE " +
            "GROUP BY YEAR(o.orderDate), MONTH(o.orderDate), DAY(o.orderDate) " +
            "ORDER BY YEAR(o.orderDate), MONTH(o.orderDate), DAY(o.orderDate)")
    MonthlyOrderSummaryDto findTodayOrderSummaries();
}