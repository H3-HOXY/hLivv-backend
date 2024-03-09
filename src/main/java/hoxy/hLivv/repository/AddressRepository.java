package hoxy.hLivv.repository;

import hoxy.hLivv.entity.Address;
import hoxy.hLivv.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long> {
    //	List<Address> findByOrder(Order order);
    List<Address> findByMember(Member member);
}
