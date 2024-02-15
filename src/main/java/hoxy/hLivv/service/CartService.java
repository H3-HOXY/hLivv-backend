package hoxy.hLivv.service;

import hoxy.hLivv.dto.CartDto;
import hoxy.hLivv.entity.Cart;
import hoxy.hLivv.entity.Member;
import hoxy.hLivv.entity.Product;
import hoxy.hLivv.entity.compositekey.CartId;
import hoxy.hLivv.exception.CartItemRemovedException;
import hoxy.hLivv.exception.NotFoundCartItemException;
import hoxy.hLivv.exception.NotFoundMemberException;
import hoxy.hLivv.repository.CartRepository;
import hoxy.hLivv.repository.MemberRepository;
import hoxy.hLivv.repository.ProductRepository;
import hoxy.hLivv.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class CartService {
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;


    @Transactional
    public CartDto addProductToCart(Long productId, Integer qty) {
        Member member = SecurityUtil.getCurrentUsername()
                .flatMap(memberRepository::findOneWithAuthoritiesByLoginId)
                .orElseThrow(() -> new NotFoundMemberException("Member not found"));
        Product product = productRepository.findById(productId).orElseThrow(() -> new NotFoundCartItemException("상품을 찾을 수 없습니다."));
        Cart cart = cartRepository.findById(new CartId(member.getMemberId(), product.getId()))
                .map(existingCart -> updateCart(existingCart, qty))
                .orElseGet(() -> member.addProductToCart(product, qty));
        return CartDto.from(cart);
    }

    // 장바구니 상품 수량 갱신
    private Cart updateCart(Cart cart, Integer qty) {
        cart.updateQuantity(cart.getCartQty() + qty);
        return cart;
    }

    @Transactional
    public CartDto updateCart(Long productId, Integer qty) {
        Member member = SecurityUtil.getCurrentUsername()
                .flatMap(memberRepository::findOneWithAuthoritiesByLoginId)
                .orElseThrow(() -> new NotFoundMemberException("Member not found"));
        Cart cart = cartRepository.findById(new CartId(member.getMemberId(), productId))
                .orElseThrow(() -> new NotFoundCartItemException("장바구니에 해당 상품이 없습니다."));
        cart.updateQuantity(qty);
        if (cart.isEmpty()) {
            member.removeCart(cart);
            throw new CartItemRemovedException("장바구니에서 해당 상품이 삭제되었습니다.");
        }
        return CartDto.from(cart);
    }

    // 장바구니 상품 삭제
    @Transactional
    public void deleteFromCart(Long productId) {
        Member member = SecurityUtil.getCurrentUsername()
                .flatMap(memberRepository::findOneWithAuthoritiesByLoginId)
                .orElseThrow(() -> new NotFoundMemberException("Member not found"));
        Cart cart = cartRepository.findById(new CartId(member.getMemberId(), productId))
                .orElseThrow(() -> new NotFoundCartItemException("장바구니에 해당 상품이 없습니다."));
        member.removeCart(cart);
    }

}
