package hoxy.hLivv.dto.member;


import hoxy.hLivv.entity.enums.MemberGrade;
import hoxy.hLivv.entity.enums.RestoreStatus;
import lombok.*;

/**
 * @author 이상원
 */
@Getter
@Setter
@Builder
@NoArgsConstructor // JPA가 요구하는 기본 생성자
@AllArgsConstructor // 모든 필드를 매개변수로 받는 생성자
public class MemberGradeDto {
    private MemberGrade grade;
    private Long cnt;

}
