package hoxy.hLivv.dto.address;

import hoxy.hLivv.entity.Address;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddressResDto {
	private Long addressId;
	private AddressRequesterDto addressRequesterDto;
	private String streetAddress;
	private String detailedAddress;
	private String zipCode;
	private String telephoneNumber;
	private String mobilePhoneNumber;
	private String requestMsg;

	// public static AddressResDto from(Address address) {
	// 	return AddressResDto.builder()
	// 		.addressId(address.getAddressId())
	// 		.addressRequesterDto(AddressRequesterDto.from(address.getMember())
	// 		.streetAddress(address.getStreetAddress())
	// 		.detailedAddress(address.getDetailedAddress())
	// 		.zipCode(address.getZipCode())
	// 		.telephoneNumber(address.getTelephoneNumber())
	// 		.mobilePhoneNumber(address.getMobilePhoneNumber())
	// 		.requestMsg(address.getRequestMsg())
	// 		.build();
	// }
}
