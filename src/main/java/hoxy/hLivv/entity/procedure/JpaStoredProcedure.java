package hoxy.hLivv.entity.procedure;

import jakarta.persistence.*;

/**
 * @author 이상원
 */
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
})
public class JpaStoredProcedure {
    @Id
    private Long dummy;
}
