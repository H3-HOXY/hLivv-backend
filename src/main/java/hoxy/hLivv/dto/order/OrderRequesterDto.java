package hoxy.hLivv.dto.order;

import hoxy.hLivv.entity.Member;
import hoxy.hLivv.entity.enums.MemberGrade;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderRequesterDto {

    private String name;
    private String phone;
    private String email;

    private Long expectedPoints;
    private MemberGrade grade;

    public static OrderRequesterDto from(Member member, Long plusPoints) {
        if (member == null) return null;
        return OrderRequesterDto.builder()
                        .name(member.getName())
                .phone(member.getPhone())
                .email(member.getEmail())
                .expectedPoints(member.getPoints()+plusPoints)
                .grade(member.getGrade())
                        .build();
    }
}