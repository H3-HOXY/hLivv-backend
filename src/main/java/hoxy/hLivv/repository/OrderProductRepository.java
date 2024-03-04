package hoxy.hLivv.repository;

import hoxy.hLivv.entity.OrderProduct;
import hoxy.hLivv.entity.enums.DeliveryStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {

    @Query("SELECT op FROM OrderProduct op WHERE op.delivery.deliveryStatus = :status")
    List<OrderProduct> findAllByDeliveryStatus(@Param("status") DeliveryStatus status);

    List<OrderProduct> findOrderProductByOrderOrderId(Long orderId);
}
