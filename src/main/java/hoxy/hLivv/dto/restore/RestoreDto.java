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
public class RestoreDto {
    private Long restoreId;
//    private Long restoreId;
    private Long memberId;
    private Long productId;
    private Date regDate;
    private Date pickUpDate;
    private RestoreProductStatus requestGrade;
    private RestoreProductStatus inspectedGrade;
    private String restoreDesc;
    private Boolean rewarded;
    private String rejectMsg;
    private Long payback;
    private Boolean whenRejected;
    private RestoreStatus restoreStatus;
    private List<String> restoreImageUrls;


    public static RestoreDto from(Restore restore) {
        if (restore == null) return null;
        return RestoreDto.builder()
                .restoreId(restore.getRestoreId())
                .memberId(restore.getMember().getMemberId())
                .productId(restore.getProduct().getId())
                .regDate(restore.getRegDate())
                .pickUpDate(restore.getPickUpDate())
                .requestGrade(restore.getRequestGrade())
                .inspectedGrade(restore.getInspectedGrade())
                .restoreDesc(restore.getRestoreDesc())
                .rewarded(restore.getRewarded())
                .rejectMsg(restore.getRejectMsg())
                .payback(restore.getPayback())
                .whenRejected(restore.getWhenRejected())
                .restoreStatus(restore.getRestoreStatus())
//                .restoreImages(restore.getRestoreImages().stream().map(RestoreImage::getRiUrl).toList())
//                .restoreImages(restore.getRestoreImages().stream().map(RestoreImageDto::from).toList())
                .restoreImageUrls(restore.getRestoreImages().stream().map(RestoreImage::getRiUrl).toList())
                .build();

    }


}
