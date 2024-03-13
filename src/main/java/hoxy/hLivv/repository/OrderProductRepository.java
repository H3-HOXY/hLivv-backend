package hoxy.hLivv.repository;

import hoxy.hLivv.entity.OrderProduct;
import hoxy.hLivv.entity.enums.DeliveryStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
/**
 * @author 반정현
 */
public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {

    @Query("SELECT op FROM OrderProduct op WHERE op.delivery.deliveryStatus = :status")
    List<OrderProduct> findAllByDeliveryStatus(@Param("status") DeliveryStatus status);

    List<OrderProduct> findOrderProductByOrderOrderId(Long orderId);
    @Query("SELECT op FROM OrderProduct op JOIN op.order o WHERE o.member.memberId = :memberId AND op.delivery.deliveryStatus = :status")
    Page<OrderProduct> findWithMemberAndDeliveryStatus(@Param("memberId") Long memberId, @Param("status") DeliveryStatus status, Pageable pageable);
    List<OrderProduct> findByDelivery_DeliveryId(Long deliveryId);

    @Query("SELECT op FROM OrderProduct op JOIN op.order o WHERE o.member.memberId = :memberId")
    Page<OrderProduct> findWithMember(@Param("memberId") Long memberId,Pageable pageable);
}
