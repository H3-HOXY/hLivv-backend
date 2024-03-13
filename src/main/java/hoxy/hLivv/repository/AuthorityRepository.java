package hoxy.hLivv.repository;

import hoxy.hLivv.entity.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
/**
 * @author 이상원
 */
public interface AuthorityRepository extends JpaRepository<Authority, Long> {
    // authorityName을 사용하여 Authority 엔티티 검색
    Optional<Authority> findByAuthorityName(String authorityName);
}
