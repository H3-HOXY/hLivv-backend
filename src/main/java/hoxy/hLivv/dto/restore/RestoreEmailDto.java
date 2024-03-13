package hoxy.hLivv.dto.restore;


import hoxy.hLivv.entity.enums.RestoreProductStatus;
import lombok.*;

/**
 * @author 이상원
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RestoreEmailDto {

    private String rejectMsg;
    private String productName;
    private RestoreProductStatus requestGrade;
    private Long payback;
    private RestoreProductStatus inspectedGrade;
    private String subject;
    private String toEmail;
}
