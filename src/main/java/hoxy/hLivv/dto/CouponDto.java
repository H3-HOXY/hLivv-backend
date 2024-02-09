package hoxy.hLivv.dto;


import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CouponDto {
    private Integer couponDuration;
    private String couponDesc;
    private Double discountRate;
}