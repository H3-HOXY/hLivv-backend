package hoxy.hLivv.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "member")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "login_id")
    private String loginId;

    @Column(name = "login_pw")
    private String loginPw;

    @Column(name = "name")
    private String name;

    @Column(name = "phone")
    private String phone;

    @Column(name = "email")
    private String email;

    @Column(name = "signup_date")
    @Temporal(TemporalType.DATE)
    private Date signupDate;

    @Column(name = "interior_type")
    private String interiorType;

    @Column(name = "grade")
    private String grade;

    @Column(name = "oauth_token")
    private String oauthToken;

//    @OneToMany(mappedBy = "member")
//    private Set<Address> addresses;

//    @OneToMany(mappedBy = "member")
//    private Set<Points> points;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<MemberAuthority> authorities;

//    @OneToMany(mappedBy = "member")
//    private Set<Orders> orders;

}