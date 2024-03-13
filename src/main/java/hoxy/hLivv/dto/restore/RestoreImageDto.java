package hoxy.hLivv.dto.restore;

import hoxy.hLivv.entity.Restore;
import hoxy.hLivv.entity.RestoreImage;
import lombok.*;
/**
 * @author 이상원
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RestoreImageDto {

    private Long riId;
    private Long restoreId;
    private String riUrl;

    public static RestoreImageDto from(RestoreImage restoreImage) {
        if (restoreImage == null) return null;
        return RestoreImageDto.builder()
                .restoreId(restoreImage.getRestore().getRestoreId())
                .riUrl(restoreImage.getRiUrl())
                .build();
    }
}