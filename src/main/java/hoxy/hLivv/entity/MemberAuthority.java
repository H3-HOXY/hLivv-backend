package hoxy.hLivv.entity;

import jakarta.persistence.*;
import lombok.*;
/**
 * @author 이상원
 */
@Entity
@Table(name = "member_authority")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_authority_id")
    private Long memberAuthorityId;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "authority_id")
    private Authority authority;

}
