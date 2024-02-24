package hoxy.hLivv.entity;

import hoxy.hLivv.entity.compositekey.CartId;
import hoxy.hLivv.entity.enums.MemberGrade;
import hoxy.hLivv.exception.InvalidPointException;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;
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

    @Column(name = "points")
    private Long points;

    @Column(name = "grade")
    @Enumerated(EnumType.STRING)
    private MemberGrade grade;

    @Column(name = "oauth_token")
    private String oauthToken;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<MemberAuthority> authorities;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Address> addresses;
    //
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Order> orders;
//
//    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<RaffleMember> raffles;
//
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Restore> restores;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemberCoupon> coupons;
//
//    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Review> reviews;
//
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Cart> carts;

    public Cart addProductToCart(Product product, Integer qty) {
        Cart cart = Cart.builder()
                .cartId(new CartId(this.getMemberId(), product.getId()))
                .member(this)
                .product(product)
                .cartQty(qty)
                .build();
        this.carts.add(cart);
        return cart;
    }


    public void removeCart(Cart cart) {
        this.carts.remove(cart);
    }

    public Long usePoints(Long pointsToUse) {
        if (this.points!=0 && this.points < 100) {
            throw new InvalidPointException("Minimum 100 points are required to use points.");
        }
        if (pointsToUse > this.points) {
            pointsToUse = this.points;
        }
        this.points -= pointsToUse;
        return pointsToUse;
    }

    public void increasePoints(Long amount) {
        this.points += amount;
    }
}