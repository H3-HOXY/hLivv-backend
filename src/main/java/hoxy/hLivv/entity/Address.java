package hoxy.hLivv.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "address")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private Long addressId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(name = "street_address", nullable = false)
    private String streetAddress;

    @Column(name = "detailed_address")
    private String detailedAddress;

    @Column(name = "zip_code", nullable = false)
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

//