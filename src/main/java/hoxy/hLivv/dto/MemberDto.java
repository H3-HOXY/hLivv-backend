package hoxy.hLivv.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import hoxy.hLivv.entity.Member;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
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

   private Set<AuthorityDto> authorityDtoSet;

   public static MemberDto from(Member member) {
      if(member == null) return null;

      return MemberDto.builder()
              .loginId(member.getLoginId())
              .name(member.getName())
              .authorityDtoSet(member.getAuthorities().stream()
                      .map(authority -> AuthorityDto.builder().authorityName(authority.getAuthority().getAuthorityName()).build())
                      .collect(Collectors.toSet()))
              .build();
   }
}