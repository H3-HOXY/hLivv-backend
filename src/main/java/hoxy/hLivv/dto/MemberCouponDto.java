package hoxy.hLivv.dto;

import lombok.*;

import java.util.Date;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberCouponDto {
    private Long memberId;
    private Long couponId;
    private Date expireDate;
}
