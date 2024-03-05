package hoxy.hLivv.dto.member;

import hoxy.hLivv.entity.enums.MemberGrade;
import lombok.Data;

import java.util.Date;

@Data
public class MemberResponseDto {
    private String loginId;
    private String name;
    private String phone;
    private String email;
    private Date signupDate;
    private String interiorType;
    private Long points;
    private MemberGrade grade;

    public static MemberResponseDto from(MemberDto member) {
        MemberResponseDto memberResponseDto = new MemberResponseDto();
        memberResponseDto.loginId = member.getLoginId();
        memberResponseDto.name = member.getName();
        memberResponseDto.phone = member.getPhone();
        memberResponseDto.email = member.getEmail();
        memberResponseDto.signupDate = member.getSignupDate();
        memberResponseDto.interiorType = member.getInteriorType();
        memberResponseDto.points = member.getPoints();
        memberResponseDto.grade = member.getGrade();
        return memberResponseDto;
    }
}
