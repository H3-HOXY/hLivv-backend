package hoxy.hLivv.entity.procedure;

import jakarta.persistence.*;

@Entity
@NamedStoredProcedureQueries({
        @NamedStoredProcedureQuery(
                name = "UpdateRestoreCompleteAndPoints",
                procedureName = "update_restore_complete_and_points",
                parameters = {
                        // @StoredProcedureParameter(mode = ParameterMode.IN, type = Long.class, name = "parameterName")
                }
        ),
        @NamedStoredProcedureQuery(
                name = "UpdateRestoreCompleteAndPointsByRestoreId",
                procedureName = "update_restore_complete_and_points_for_id",
                parameters = {
                        @StoredProcedureParameter(mode = ParameterMode.IN, type = Long.class, name = "p_restore_id")
                }
        )

        // 회원 가입하면 쿠폰 발급하는 pl/sql 추가!todo
})
public class JpaStoredProcedure {
    @Id
    private Long dummy;
}
