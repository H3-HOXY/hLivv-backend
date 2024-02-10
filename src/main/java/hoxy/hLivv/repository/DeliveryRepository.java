package hoxy.hLivv.repository;

import hoxy.hLivv.entity.Delivery;
import hoxy.hLivv.entity.MemberCoupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
}
