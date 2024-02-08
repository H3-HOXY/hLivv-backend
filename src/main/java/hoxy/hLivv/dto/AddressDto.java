package hoxy.hLivv.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressDto {
    private Long addressId;
    private Long memberId; // 엔티티 관계 대신 ID를 사용
    private String streetAddress;
    private String detailedAddress;
    private String zipCode;
    private String telephoneNumber;
    private String mobilePhoneNumber;
    private String requestMsg;

    // 필요에 따라 추가 메소드 구현
}
