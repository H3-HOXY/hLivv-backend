package hoxy.hLivv.dto.address;

import hoxy.hLivv.entity.Address;
import hoxy.hLivv.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @since 2024. 03. 04
 * @author 최정윤
 */

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddressReqDto {
	private String streetAddress;
	private String detailedAddress;
	private String zipCode;
	private String telephoneNumber;
	private String mobilePhoneNumber;
	private String requestMsg;
	private Boolean defaultYn;


	public Address prepareAddress(Member member){
		Address address = Address.builder()
			.member(member)
			.streetAddress(this.streetAddress)
			.detailedAddress(this.detailedAddress)
			.zipCode(this.zipCode)
			.telephoneNumber(this.telephoneNumber)
			.mobilePhoneNumber(this.mobilePhoneNumber)
			.requestMsg(this.requestMsg)
			.defaultYn(this.defaultYn)
			.build();
		return address;
	}
}
