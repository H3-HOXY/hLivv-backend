package hoxy.hLivv.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import hoxy.hLivv.entity.Member;
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
public class SignupDataGenDto {
    @NotNull
    @Size(min = 5, max = 20)
//    @Pattern(regexp = "^[A-Za-z0-9_]{5,15}$", message = "아이디는 5글자 이상, 15글자 이하로 설정해야 하며, 영문 대소문자, 숫자, 밑줄(_)만 포함할 수 있습니다.")
    private String loginId;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) //쓰기 전용
    @NotNull
    @Size(min = 5, max = 100)
//    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$", message = "비밀번호는 8글자 이상으로 설정해야 하며, 적어도 하나의 문자와 하나의 숫자를 포함해야 합니다.")
    private String loginPw;

    @NotNull
    @Size(min = 3, max = 20)
    private String name;

    private String phone;

//    @Email//개발 단계에서는 빼고 함
    private String email;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date signupDate;

    public static SignupDataGenDto from(Member member) {
        if (member == null) return null;
        return SignupDataGenDto.builder()
                .loginId(member.getLoginId())
                .name(member.getName())
                .phone(member.getPhone())
                .email(member.getEmail())
                .build();
    }
}
