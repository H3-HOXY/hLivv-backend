package hoxy.hLivv.repository;

import hoxy.hLivv.entity.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    @EntityGraph(attributePaths = "authorities")
        // lazy가 아닌 eager 조회
        // @EntityGraph를 사용할 때, attributePaths 속성에 지정된 연관 경로들은 메서드가 실행될 때 즉시 로드
    Optional<Member> findOneWithAuthoritiesByLoginId(String loginId);//유저네임으로 권한과 유저함께

    Optional<Member> findByLoginId(String loginId);

    Page<Member> findAll(Pageable pageable);
}
