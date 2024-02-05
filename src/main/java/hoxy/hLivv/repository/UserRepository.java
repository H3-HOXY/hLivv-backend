package hoxy.hLivv.repository;

import hoxy.hLivv.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
   @EntityGraph(attributePaths = "authorities") // lazy가 아닌 eager 조회
   Optional<User> findOneWithAuthoritiesByUsername(String username);//유저네임으로 권한과 유저함께
}
