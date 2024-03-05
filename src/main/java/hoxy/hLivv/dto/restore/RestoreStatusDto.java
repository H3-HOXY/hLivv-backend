package hoxy.hLivv.dto.restore;


import hoxy.hLivv.entity.enums.RestoreStatus;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor // JPA가 요구하는 기본 생성자
@AllArgsConstructor // 모든 필드를 매개변수로 받는 생성자
public class RestoreStatusDto {
    private RestoreStatus restoreStatus;
    private Long cnt;

}
