package hoxy.hLivv.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressDto {
    private String streetAddress;
    private String detailedAddress;
    private String zipCode;
    private String telephoneNumber;
    private String mobilePhoneNumber;
    private String requestMsg;
    private String defaultYn;

}
