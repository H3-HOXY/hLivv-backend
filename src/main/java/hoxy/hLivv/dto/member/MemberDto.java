package hoxy.hLivv.dto.member;

import com.fasterxml.jackson.annotation.JsonProperty;
import hoxy.hLivv.entity.Member;
import hoxy.hLivv.entity.enums.MemberGrade;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MemberDto {

    @NotNull
    @Size(min = 3, max = 50)
    private String loginId;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) //쓰기 전용
    @NotNull
    @Size(min = 3, max = 100)
    private String loginPw;

    @NotNull
    @Size(min = 3, max = 50)
    private String name;

    private String phone;

    @Email
    private String email;

    private Date signupDate;
    private String interiorType;
    private Long points;
    private MemberGrade grade;

    public static MemberDto from(Member member) {
        if (member == null) return null;
        return MemberDto.builder()
                        .loginId(member.getLoginId())
                        .name(member.getName())
                .phone(member.getPhone())
                .email(member.getEmail())
                .signupDate(member.getSignupDate())
                .interiorType(member.getInteriorType())
                .points(member.getPoints())
                .grade(member.getGrade())
                        .build();
    }
}