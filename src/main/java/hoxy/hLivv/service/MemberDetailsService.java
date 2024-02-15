package hoxy.hLivv.service;

import hoxy.hLivv.entity.Member;
import hoxy.hLivv.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

//@Component("userDetailsService")
@Service
@Slf4j
@RequiredArgsConstructor
public class MemberDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(final String loginId) {
        // 로그인시 DB에서 유저 정보와 권한정보를 가저오고 해당 정보를 기반으로 User 객체를 생성해서 리턴
        return memberRepository.findOneWithAuthoritiesByLoginId(loginId)
                .map(this::createUser)
                .orElseThrow(() -> new UsernameNotFoundException(loginId + " -> 데이터베이스에서 찾을 수 없습니다."));
    }

    private User createUser(Member member) {


        List<GrantedAuthority> grantedAuthorities = member.getAuthorities()
                .stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getAuthority()
                        .getAuthorityName()))
                .collect(Collectors.toList());

        return new org.springframework.security.core.userdetails.User(member.getLoginId(),
                member.getLoginPw(),
                grantedAuthorities);
    }
}
