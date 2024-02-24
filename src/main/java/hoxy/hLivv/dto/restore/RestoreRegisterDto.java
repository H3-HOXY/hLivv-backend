package hoxy.hLivv.dto.restore;

import hoxy.hLivv.entity.Restore;
import hoxy.hLivv.entity.RestoreImage;
import hoxy.hLivv.entity.enums.RestoreProductStatus;
import hoxy.hLivv.entity.enums.RestoreStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RestoreRegisterDto {
    @NotNull
    private Long productId;
    private Date pickUpDate;
    @NotNull
    private RestoreProductStatus requestGrade;
    private String restoreDesc;
    private String requestMsg;
    @NotNull
    private Boolean whenRejected;
    private List<String> restoreImageUrls;
}
