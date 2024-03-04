package hoxy.hLivv.service;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import hoxy.hLivv.dto.AddressDto;
import hoxy.hLivv.dto.address.AddressReqDto;
import hoxy.hLivv.dto.address.AddressResDto;
import hoxy.hLivv.dto.product.ProductDto;
import hoxy.hLivv.entity.Address;
import hoxy.hLivv.entity.Member;
import hoxy.hLivv.entity.Order;
import hoxy.hLivv.exception.NotFoundMemberException;
import hoxy.hLivv.exception.NotFoundOrderException;
import hoxy.hLivv.repository.AddressRepository;
import hoxy.hLivv.repository.MemberRepository;
import hoxy.hLivv.repository.OrderRepository;
import hoxy.hLivv.util.SecurityUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class AddressService {
	private final AddressRepository addressRepository;
	private final MemberRepository memberRepository;
	private final OrderRepository orderRepository;

	@Transactional
	public AddressDto saveAddress(AddressReqDto addressReqDto) {
		// 	1. DTO로부터 정보 추출
		Member member = getMember();

		// 	2. 주소록 생성
		Address address = createAddress(addressReqDto, member);

		//  3. 주소록 저장
		addressRepository.save(address);

		// 	4. 생성된 주소 반환
		return AddressDto.from(address);
	}

	@Transactional
	public Address createAddress(AddressReqDto addressReqDto, Member member) {
		Address address = addressReqDto.prepareAddress(member);

		return address;
	}

	@Transactional
	public List<AddressDto> getOrderAddress(long orderId) {
		Order order = orderRepository.findById(orderId).orElseThrow(() -> new NotFoundOrderException("Order not found"));
		return addressRepository.findByOrder(order).stream().map(AddressDto::from).toList();
	}

	private Member getMember() {
		return SecurityUtil.getCurrentUsername()
			.flatMap(memberRepository::findOneWithAuthoritiesByLoginId)
			.orElseThrow(() -> new NotFoundMemberException("Member not found"));
	}

	public List<AddressDto> getAllAddress(int pageNo, int pageSize) {
		pageSize = Math.min(pageSize, 100);
		Pageable pageable = PageRequest.of(pageNo, pageSize);
		return addressRepository.findAll(pageable)
			.stream()
			.map(AddressDto::from)
			.toList();
	}

	public AddressDto getAddressWith(Long id) {
		return AddressDto.from(addressRepository.getReferenceById(id));
	}
}
