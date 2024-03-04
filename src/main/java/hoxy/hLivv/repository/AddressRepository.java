package hoxy.hLivv.repository;

import java.util.List;

import hoxy.hLivv.entity.Address;
import hoxy.hLivv.entity.Order;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
	List<Address> findByOrder(Order order);
}
