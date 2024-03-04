package hoxy.hLivv.dto.order;


import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor // JPA가 요구하는 기본 생성자
@AllArgsConstructor // 모든 필드를 매개변수로 받는 생성자
public class MonthlyOrderSummaryDto {
    private int year;
    private int month;
    private int day;
    private Long orderTotal;
    private Long cnt;

}
