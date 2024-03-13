package hoxy.hLivv.service;

import hoxy.hLivv.dto.CartDto;
import hoxy.hLivv.dto.DeliveryResDto;
import hoxy.hLivv.dto.MemberCouponDto;
import hoxy.hLivv.dto.address.AddressReqDto;
import hoxy.hLivv.dto.member.*;
import hoxy.hLivv.dto.order.OrderProductReqDto;
import hoxy.hLivv.dto.order.OrderReqDto;
import hoxy.hLivv.dto.order.OrderResDto;
import hoxy.hLivv.entity.*;
import hoxy.hLivv.entity.compositekey.CartId;
import hoxy.hLivv.entity.enums.DeliveryStatus;
import hoxy.hLivv.entity.enums.MemberGrade;
import hoxy.hLivv.exception.*;
import hoxy.hLivv.repository.*;
import hoxy.hLivv.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;
/**
 * @author 이상원
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final MemberCouponRepository memberCouponRepository;
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final AuthorityRepository authorityRepository;
    private final PasswordEncoder passwordEncoder;
    private final OrderProductRepository orderProductRepository;
    private final AddressService addressService;
    private final AddressRepository addressRepository;
    private final OrderService orderService;
    private final ProductRepository productRepository;

    @Transactional
    public MemberDto signup(SignupDto signupDto) {
        if (memberRepository.findOneWithAuthoritiesByLoginId(signupDto.getLoginId())
                            .orElse(null) != null) {
            throw new DuplicateMemberException("이미 가입되어 있는 멤버입니다.");
        }

        Authority auth = authorityRepository.findByAuthorityName("ROLE_USER")
                                            .orElseGet(() -> authorityRepository.save(Authority.builder()
                                                                                               .authorityName(
                                                                                                       "ROLE_USER")
                                                                                               .memberAuthorities(
                                                                                                       new HashSet<>())
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

        List<Long> productIds = productRepository.findAll().stream().map(Product::getId).toList();
        Random random = new Random();
        for (SignupDataGenDto signupDto : signupDtos) {
            if (memberRepository.findOneWithAuthoritiesByLoginId(signupDto.getLoginId())
                                .orElse(null) != null) {
                continue;
//                throw new DuplicateMemberException("이미 가입되어 있는 멤버입니다.");
            }

            Authority auth = authorityRepository.findByAuthorityName("ROLE_USER")
                                                .orElseGet(() -> authorityRepository.save(Authority.builder()
                                                                                                   .authorityName(
                                                                                                           "ROLE_USER")
                                                                                                   .memberAuthorities(
                                                                                                           new HashSet<>())
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
//            memberRepository.save(member);
            AddressReqDto addressReqDto = AddressReqDto.builder()
                    .defaultYn(true)
                    .streetAddress(signupDto.getStreetAddress())
                    .detailedAddress("")
                    .mobilePhoneNumber(member.getPhone())
                    .telephoneNumber(member.getPhone())
                    .requestMsg("")
                    .zipCode(signupDto.getZipCode())
                    .build();
            // 1. 멤버 데이터 생성
            Member SavedMember = memberRepository.save(member);
            // 2. 생성된 멤버에 대한 주소록 데이터 생성
            Address address = addressRepository.save(addressReqDto.prepareAddress(SavedMember));
            // 3. 주문에서 주문한 상품 Dto 생성

            int randomIndex = random.nextInt(productIds.size());
            OrderProductReqDto orderProductReqDto = OrderProductReqDto.builder()
                    .productId(productIds.get(randomIndex))//프로덕트 id 랜덤으로 받아오기
                    .productQty(1)
                    .build();
            List<OrderProductReqDto> orderProductReqDtos = new ArrayList<>();
            orderProductReqDtos.add(orderProductReqDto);
            // 4. 주문 Dto 생성
            OrderReqDto orderReqDto = OrderReqDto.builder()
                    .productList(orderProductReqDtos)
                    .orderPoint(0L)
                    .requestDate(signupDto.getSignupDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().plusDays(1))
                    .build();
            // 5. 주문 저장
            orderService.orderDataGen(orderReqDto, SavedMember,address);
            // 6. 메인 DB에서 프로시져, 트리거 만들고 실행하기
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
                                                                                               .authorityName(
                                                                                                       "ROLE_ADMIN")
                                                                                               .memberAuthorities(
                                                                                                       new HashSet<>())
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
        if (id == null) {
            return null;
        }
        return MemberDto.from(memberRepository.findById(id)
                                              .orElseThrow(() -> new NotFoundMemberException("Member not found")));
    }

    public Page<MemberDto> getAllMembersWithPagination(Pageable pageable) {
        return memberRepository.findAll(pageable)
                               .map(MemberDto::from);
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

    @Transactional(readOnly = true)
    public Page<MemberCouponDto> getUnusedCoupons(Pageable pageable) {
        Member member = getMember();
        return memberCouponRepository.findByMemberAndIsUsedFalseAndExpireDateAfter(member, LocalDate.now(), pageable)
                                     .map(MemberCouponDto::from);
    }

    /**
     * @author 반정현
     */
    @Transactional(readOnly = true)
    public Page<CartDto> getCartsByMember(Pageable pageable) {
        Member member = getMember();
        return cartRepository.findByMember(member, pageable)
                             .map(CartDto::from);
    }

    /**
     * @author 반정현
     */
    @Transactional(readOnly = true)
    public List<CartDto> getAllCartsByMember() {
        Member member = getMember();
        return cartRepository.findByMemberAll(member)
                .stream()
                .map(CartDto::from)
                .collect(Collectors.toList());
    }


    /**
     * @author 반정현
     */
    @Transactional(readOnly = true)
    public Page<OrderResDto> getOrdersByMember(Pageable pageable) {
        Member member = getMember();
        return orderRepository.findByMember(member, pageable)
                              .map(OrderResDto::from);
    }

    @Transactional
    public Season updateSeason(Season season) {
        Member member = getMember();
        member.setInteriorType(season.toString());
        memberRepository.save(member);
        return season;
    }

    /**
     * @author 반정현
     */
    @Transactional(readOnly = true)
    public List<CartDto> getSelectedItems(List<Long> productIds) {
        Member member = getMember();
        List<CartId> cartIds = productIds.stream()
                                         .map(productId -> new CartId(member.getMemberId(), productId))
                                         .collect(Collectors.toList());

        List<Cart> carts = cartRepository.findByCartIdIn(cartIds);

        return carts.stream()
                    .map(CartDto::from)
                    .collect(Collectors.toList());
    }

    /**
     * @author 반정현
     */
    private Member getMember() {
        return SecurityUtil.getCurrentUsername()
                           .flatMap(memberRepository::findOneWithAuthoritiesByLoginId)
                           .orElseThrow(() -> new NotFoundMemberException("Member not found"));
    }

    @Transactional
    public List<MemberGradeDto> getMemberGrade() {
        return memberRepository.findMemberGradeGroupBy();
    }

    @Transactional
    public List<MonthlyMemberRegisterDto> getMonthlyMemberRegi() {
        return memberRepository.findMonthlyMemberRegi();
    }

    /**
     * @author 반정현
     */
    @Transactional(readOnly = true)
    public Page<DeliveryResDto> findCompletedDeliveriesByMemberId(Pageable pageable) {
        Member member = getMember();
        return orderProductRepository.findWithMemberAndDeliveryStatus(member.getMemberId(),DeliveryStatus.배송완료, pageable)
                .map(DeliveryResDto::from);
    }

    /**
     * @author 반정현
     */
    @Transactional(readOnly = true)
    public Page<DeliveryResDto> findInProgressDeliveriesByMemberId(Pageable pageable) {
        Member member = getMember();
        return orderProductRepository.findWithMemberAndDeliveryStatus(member.getMemberId(),DeliveryStatus.배송중, pageable)
                .map(DeliveryResDto::from);
    }

    /**
     * @author 반정현
     */
    @Transactional(readOnly = true)
    public OrderResDto findOrderById(Long orderId) {
        Member member=getMember();
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundOrderException("해당 주문이 존재하지 않습니다. 주문 ID: " + orderId));

        if (!order.getMember().getMemberId().equals(member.getMemberId())) {
            throw new MismatchedMemberException("주문 ID와 회원 ID가 일치하지 않습니다.");
        }

        return OrderResDto.from(order);
    }

    /**
     * @author 반정현
     */
    @Transactional(readOnly = true)
    public List<DeliveryResDto> findDeliveryById(Long deliveryId) {
        Member member = getMember();
        List<OrderProduct> orderProducts = orderProductRepository.findByDelivery_DeliveryId(deliveryId);

        for (OrderProduct orderProduct : orderProducts) {
            Order order = orderProduct.getOrder();
            if (!order.getMember().getMemberId().equals(member.getMemberId())) {
                throw new MismatchedMemberException("배송 ID와 회원 ID가 일치하지 않습니다.");
            }
        }

        return orderProducts.stream()
                .map(DeliveryResDto::from)
                .collect(Collectors.toList());
    }

    /**
     * @author 반정현
     */
    @Transactional(readOnly = true)
    public Page<DeliveryResDto> findDeliveriesByMemberId(Pageable pageable) {
        Member member = getMember();
        return orderProductRepository.findWithMember(member.getMemberId(),pageable)
                .map(DeliveryResDto::from);
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
