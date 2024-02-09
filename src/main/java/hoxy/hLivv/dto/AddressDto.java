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
    private String streetAddress;
    private String detailedAddress;
    private String zipCode;
    private String telephoneNumber;
    private String mobilePhoneNumber;
    private String requestMsg;

}
