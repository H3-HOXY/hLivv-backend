package hoxy.hLivv.repository;

import hoxy.hLivv.entity.Address;
import hoxy.hLivv.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @since 2024. 03. 04
 * @author 최정윤
 */

public interface AddressRepository extends JpaRepository<Address, Long> {
    //	List<Address> findByOrder(Order order);
    List<Address> findByMember(Member member);
}
