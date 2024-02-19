package hoxy.hLivv.repository;

import hoxy.hLivv.entity.Member;
import hoxy.hLivv.entity.Restore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface RestoreRepository extends JpaRepository<Restore, Long> {
    List<Restore> findByMember(Member member);

}
