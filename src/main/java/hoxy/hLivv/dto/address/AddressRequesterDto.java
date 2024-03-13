package hoxy.hLivv.dto.address;

import hoxy.hLivv.entity.Member;
import hoxy.hLivv.entity.enums.MemberGrade;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @since 2024. 03. 04
 * @author 최정윤
 */

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AddressRequesterDto {
	private String name;
	private String phone;
	private String email;
	private MemberGrade grade;
	
	public static AddressRequesterDto from(Member member) {
		if(member == null) return null;
		return AddressRequesterDto.builder()
			.name(member.getName())
			.phone(member.getPhone())
			.email(member.getEmail())
			.grade(member.getGrade())
			.build();
	}

}
