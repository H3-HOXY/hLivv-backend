package hoxy.hLivv.repository;

import hoxy.hLivv.dto.order.MonthlyOrderSummaryDto;
import hoxy.hLivv.dto.restore.RestoreStatusDto;
import hoxy.hLivv.entity.Member;
import hoxy.hLivv.entity.Restore;
import hoxy.hLivv.entity.enums.RestoreStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface RestoreRepository extends JpaRepository<Restore, Long> {
    List<Restore> findByMember(Member member);

    List<Restore> findByRestoreStatus(RestoreStatus restoreStatus);


    @Query("SELECT new hoxy.hLivv.dto.restore.RestoreStatusDto(" +
            "r.restoreStatus as restoreStatus, " +
            "COUNT(*) as cnt) " +
            "FROM Restore r " +
            "where r.restoreStatus in ('접수완료','검수중','리스토어완료')" +
            "group by r.restoreStatus ")
    List<RestoreStatusDto> findRestoreGroupByRestoreStatus();
}
