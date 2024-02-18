package hoxy.hLivv.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "address")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private Long addressId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(name = "street_address")
    private String streetAddress;

    @Column(name = "detailed_address")
    private String detailedAddress;

    @Column(name = "zip_code")
    private String zipCode;

    @Column(name = "telephone_number")
    private String telephoneNumber;

    @Column(name = "mobile_phone_number")
    private String mobilePhoneNumber;

    @Column(name = "request_msg")
    private String requestMsg;

    @Column(name = "default_yn")
    private boolean defaultYn;

}
