package hoxy.hLivv.service;

import hoxy.hLivv.dto.MemberDto;
import hoxy.hLivv.entity.Authority;
import hoxy.hLivv.entity.Member;
import hoxy.hLivv.entity.MemberAuthority;
import hoxy.hLivv.exception.DuplicateMemberException;
import hoxy.hLivv.exception.NotFoundMemberException;
import hoxy.hLivv.repository.AuthorityRepository;
import hoxy.hLivv.repository.MemberRepository;
import hoxy.hLivv.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final AuthorityRepository authorityRepository;
    private final PasswordEncoder passwordEncoder;

//    public MemberService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
//        this.userRepository = userRepository;
//        this.memberRepository =
//        this.passwordEncoder = passwordEncoder;
//    }

    @Transactional
    public MemberDto signup(MemberDto memberDto) {
        if (memberRepository.findOneWithAuthoritiesByLoginId(memberDto.getLoginId()).orElse(null) != null) {
            throw new DuplicateMemberException("이미 가입되어 있는 유저입니다.");
        }

        Authority authority = Authority.builder()
                .authorityName("ROLE_USER")// 유저로만 가입되게 되어있음
                .build();



        Member member = Member.builder()
                .loginId(memberDto.getLoginId())
                .loginPw(passwordEncoder.encode(memberDto.getLoginPw()))
                .name(memberDto.getName())
//                .authorities(Collections.singleton(memberAuthority))
                .build();

        MemberAuthority memberAuthority = MemberAuthority.builder()
                .authority(authority)
                .member(member)
                .build();
        member.setAuthorities(Collections.singleton(memberAuthority));
        authority.setMemberAuthorities(Collections.singleton(memberAuthority));
        authorityRepository.save(authority);
        return MemberDto.from(memberRepository.save(member));
    }

    @Transactional(readOnly = true)
    public MemberDto getMemberWithAuthorities(String loginId) {
        return MemberDto.from(memberRepository.findOneWithAuthoritiesByLoginId(loginId).orElse(null));
    }

    @Transactional(readOnly = true)
    public MemberDto getMyMemberWithAuthorities() {
        return MemberDto.from(
                SecurityUtil.getCurrentUsername()
                        .flatMap(memberRepository::findOneWithAuthoritiesByLoginId)
                        .orElseThrow(() -> new NotFoundMemberException("Member not found"))
        );
    }
}
