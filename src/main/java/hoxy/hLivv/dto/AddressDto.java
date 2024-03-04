package hoxy.hLivv.dto;

import hoxy.hLivv.entity.Address;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressDto {
    private Long addressId;
    private Long memberId;
    private String streetAddress;
    private String detailedAddress;
    private String zipCode;
    private String telephoneNumber;
    private String mobilePhoneNumber;
    private String requestMsg;
    private String order_id;
    private boolean defaultYn;

    public static AddressDto from(Address address){
        return AddressDto.builder()
                .streetAddress(address.getStreetAddress())
                .detailedAddress(address.getDetailedAddress())
                .zipCode(address.getZipCode())
                .telephoneNumber(address.getTelephoneNumber())
                .mobilePhoneNumber(address.getMobilePhoneNumber())
                .requestMsg(address.getRequestMsg())
                .defaultYn(address.isDefaultYn())
                .build();
    }

}
