package hoxy.hLivv.service;

import hoxy.hLivv.dto.*;
import hoxy.hLivv.entity.Authority;
import hoxy.hLivv.entity.Member;
import hoxy.hLivv.entity.MemberAuthority;
import hoxy.hLivv.entity.enums.MemberGrade;
import hoxy.hLivv.exception.DuplicateMemberException;
import hoxy.hLivv.exception.NotFoundMemberException;
import hoxy.hLivv.repository.AuthorityRepository;
import hoxy.hLivv.repository.CartRepository;
import hoxy.hLivv.repository.MemberCouponRepository;
import hoxy.hLivv.repository.MemberRepository;
import hoxy.hLivv.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final MemberCouponRepository memberCouponRepository;
    private final CartRepository cartRepository;
    private final AuthorityRepository authorityRepository;
    private final PasswordEncoder passwordEncoder;

//    public MemberService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
//        this.userRepository = userRepository;
//        this.memberRepository =
//        this.passwordEncoder = passwordEncoder;
//    }

    @Transactional
    public MemberDto signup(SignupDto signupDto) {
        if (memberRepository.findOneWithAuthoritiesByLoginId(signupDto.getLoginId())
                .orElse(null) != null) {
            throw new DuplicateMemberException("이미 가입되어 있는 멤버입니다.");
        }

        Authority auth = authorityRepository.findByAuthorityName("ROLE_USER")
                .orElseGet(() -> authorityRepository.save(Authority.builder()
                        .authorityName("ROLE_USER")
                        .memberAuthorities(new HashSet<>())
                        .build()));

        Member member = Member.builder()
                .loginId(signupDto.getLoginId())
                .loginPw(passwordEncoder.encode(signupDto.getLoginPw()))
                .phone(signupDto.getPhone())
                .email(signupDto.getEmail())
                .signupDate(new Date())
                .points(0L)
                .grade(MemberGrade.FLOWER)

                .name(signupDto.getName())
                .build();

        MemberAuthority memberAuthority = MemberAuthority.builder()
                .authority(auth)
                .member(member)
                .build();
        auth.getMemberAuthorities()
                .add(memberAuthority);
        member.setAuthorities(Collections.singleton(memberAuthority));

        return MemberDto.from(memberRepository.save(member));
    }

    @Transactional
    public void signupDataGen(List<SignupDataGenDto> signupDtos) {
        for (SignupDataGenDto signupDto : signupDtos) {
            if (memberRepository.findOneWithAuthoritiesByLoginId(signupDto.getLoginId())
                    .orElse(null) != null) {
                continue;
//                throw new DuplicateMemberException("이미 가입되어 있는 멤버입니다.");
            }

            Authority auth = authorityRepository.findByAuthorityName("ROLE_USER")
                    .orElseGet(() -> authorityRepository.save(Authority.builder()
                            .authorityName("ROLE_USER")
                            .memberAuthorities(new HashSet<>())
                            .build()));

            Member member = Member.builder()
                    .loginId(signupDto.getLoginId())
                    .loginPw(passwordEncoder.encode(signupDto.getLoginPw()))
                    .phone(signupDto.getPhone())
                    .email(signupDto.getEmail())
                    .signupDate(signupDto.getSignupDate())
                    .points(0L)
                    .grade(MemberGrade.FLOWER)

                    .name(signupDto.getName())
                    .build();

            MemberAuthority memberAuthority = MemberAuthority.builder()
                    .authority(auth)
                    .member(member)
                    .build();
            auth.getMemberAuthorities()
                    .add(memberAuthority);
            member.setAuthorities(Collections.singleton(memberAuthority));
            memberRepository.save(member);
        }

    }

    @Transactional
    public MemberDto signupAdmin(SignupDto signupDto) {
        if (memberRepository.findOneWithAuthoritiesByLoginId(signupDto.getLoginId())
                .orElse(null) != null) {
            throw new DuplicateMemberException("이미 가입되어 있는 멤버입니다.");
        }

        Authority auth = authorityRepository.findByAuthorityName("ROLE_ADMIN")
                .orElseGet(() -> authorityRepository.save(Authority.builder()
                        .authorityName("ROLE_ADMIN")
                        .memberAuthorities(new HashSet<>())
                        .build()));

        Member member = Member.builder()
                .loginId(signupDto.getLoginId())
                .loginPw(passwordEncoder.encode(signupDto.getLoginPw()))
                .phone(signupDto.getPhone())
                .email(signupDto.getEmail())
                .signupDate(new Date())
                .points(0L)
                .grade(MemberGrade.FLOWER)
                .name(signupDto.getName())
                .build();

        MemberAuthority memberAuthority = MemberAuthority.builder()
                .authority(auth)
                .member(member)
                .build();
        auth.getMemberAuthorities()
                .add(memberAuthority);
        member.setAuthorities(Collections.singleton(memberAuthority));

        return MemberDto.from(memberRepository.save(member));
    }


    @Transactional(readOnly = true)
    public MemberDto getMemberWithAuthorities(String loginId) {
        return MemberDto.from(memberRepository.findOneWithAuthoritiesByLoginId(loginId)
                .orElseThrow(() -> new NotFoundMemberException("Member not found")));
    }

    @Transactional(readOnly = true)
    public MemberDto getMemberById(Long id) {
        return MemberDto.from(memberRepository.findById(id)
                .orElseThrow(() -> new NotFoundMemberException("Member not found")));
    }

    public Page<MemberDto> getAllMembersWithPagination(Pageable pageable) {
        return memberRepository.findAll(pageable).map(MemberDto::from);
    }

    public Map<String, Object> getAllMembers(int start, int length, int draw) {
        int page = start / length; // DataTables의 시작 위치를 페이지 번호로 변환
        length = Math.min(length, 100); // 최대 페이지 크기 제한
        Pageable pageable = PageRequest.of(page, length);

        Page<Member> membersPage = memberRepository.findAll(pageable);
        List<MemberDto> data = membersPage.getContent()
                .stream()
                .map(MemberDto::from)
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("draw", draw); // 클라이언트에서 전달받은 draw 값 반환 (보안을 위해)
        response.put("recordsTotal", membersPage.getTotalElements()); // 전체 멤버 수
        response.put("recordsFiltered", membersPage.getTotalElements()); // 필터링 후의 멤버 수, 여기서는 전체와 동일
        response.put("data", data); // 페이지 데이터

        return response;
    }



    @Transactional(readOnly = true)
    public MemberDto getMyMemberWithAuthorities() {
        return MemberDto.from(
                SecurityUtil.getCurrentUsername()
                            .flatMap(memberRepository::findOneWithAuthoritiesByLoginId)
                            .orElseThrow(() -> new NotFoundMemberException("Member not found"))
        );
    }

    @Transactional(readOnly = true)
    public Page<MemberCouponDto> getUnusedCoupons(Pageable pageable) {
        Member member= SecurityUtil.getCurrentUsername()
                .flatMap(memberRepository::findOneWithAuthoritiesByLoginId)
                .orElseThrow(() -> new NotFoundMemberException("Member not found"));
        return memberCouponRepository.findByMemberAndIsUsedFalse(member, pageable)
                .map(MemberCouponDto::from);
    }

    @Transactional(readOnly = true)
    public Page<CartDto> getCartsByMember(Pageable pageable) {
        Member member= SecurityUtil.getCurrentUsername()
                .flatMap(memberRepository::findOneWithAuthoritiesByLoginId)
                .orElseThrow(() -> new NotFoundMemberException("Member not found"));
        return cartRepository.findByMember(member, pageable)
                .map(CartDto::from);
    }

    @Transactional
    public Member updateMember(MemberDto memberDto) {
        Member member = memberRepository.findByLoginId(memberDto.getLoginId())
                .orElseThrow(() -> new NotFoundMemberException("Member not found"));

        member.setName(memberDto.getName());
        member.setPhone(memberDto.getPhone());
        member.setEmail(memberDto.getEmail());
        member.setPoints(memberDto.getPoints());
        member.setGrade(memberDto.getGrade());


        return memberRepository.save(member);
    }



//    @Transactional(readOnly = true)
//    public List<MemberDto> getAllMembers() {
//
//        // 서비스 또는 컨트롤러 메소드 내부
////        int page = 0; // 요청된 페이지 번호 (0부터 시작)
////        int size = 10; // 페이지 당 항목 수
////        Pageable pageable = PageRequest.of(page, size);
////        Page<Member> pageResult = memberRepository.findAll(pageable);
////        List<Member> Members = pageResult.getContent(); // 현재 페이지의 데이터
//        List<MemberDto> memberDtoList = new ArrayList<>();
//        List<Member> Members = memberRepository.findAll();
//        for (Member member : Members) {
//            if (member.getAuthorities().iterator().next().getAuthority().getAuthorityName().equals("ROLE_USER")) {
//                memberDtoList.add(MemberDto.from(member));
//            }
//
//        }
//
//        return memberDtoList;
//    }
//
//    public List<MemberDto> getAllMembers(int pageNo, int pageSize) {
//        pageSize = Math.min(pageSize, 100);
//        Pageable pageable = PageRequest.of(pageNo, pageSize);
//        return memberRepository.findAll(pageable)
//                .stream()
//                .map(MemberDto::from)
//                .toList();
//    }
//
//    public Map<String, Object> getAllMembersWithPaginationInfo(int pageNo, int pageSize) {
//        pageSize = Math.min(pageSize, 100); // 요청된 페이지 크기를 100으로 제한
//        Pageable pageable = PageRequest.of(pageNo, pageSize);
//        Page<Member> memberPage = memberRepository.findAll(pageable);
//
//        List<MemberDto> members = memberPage.getContent()
//                .stream()
//                .map(MemberDto::from)
//                .collect(Collectors.toList());
//
//        Map<String, Object> response = new HashMap<>();
//        response.put("memberPage", memberPage);
//        response.put("members", members);
//        response.put("currentPage", memberPage.getNumber());
//        response.put("totalItems", memberPage.getTotalElements());
//        response.put("totalPages", memberPage.getTotalPages());
//
//        return response;
//    }
}
