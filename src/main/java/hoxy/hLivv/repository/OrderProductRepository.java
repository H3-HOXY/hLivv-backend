package hoxy.hLivv.repository;

import hoxy.hLivv.entity.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;
public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {

}
