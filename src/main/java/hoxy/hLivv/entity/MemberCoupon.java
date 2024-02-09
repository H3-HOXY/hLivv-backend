package hoxy.hLivv.entity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "member_coupon")
@Getter
@Setter
public class MemberCoupon {
    @Id
    private Long mcId;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;

    @Temporal(TemporalType.DATE)
    private Date expireDate;

    // 생성자, 게터, 세터는 Lombok 어노테이션으로 대체
}
