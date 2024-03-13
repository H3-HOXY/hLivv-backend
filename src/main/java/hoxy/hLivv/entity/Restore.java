package hoxy.hLivv.entity;

import hoxy.hLivv.entity.enums.RestoreProductStatus;
import hoxy.hLivv.entity.enums.RestoreStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;
/**
 * @author 이상원
 */
@Entity
@Table(name = "restore")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Restore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "restore_id")
    private Long restoreId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "reg_date")
    @Temporal(TemporalType.DATE)
    private Date regDate;

    @Column(name = "pick_up_date")
    @Temporal(TemporalType.DATE)
    private Date pickUpDate;

    @Column(name = "request_grade")
    @Enumerated(EnumType.STRING)
    private RestoreProductStatus requestGrade;

    @Column(name = "inspected_grade")
    @Enumerated(EnumType.STRING)
    private RestoreProductStatus inspectedGrade;

    @Column(name = "restore_desc")
    private String restoreDesc;

    @Column(name = "rewarded")
    private Boolean rewarded;

    @Column(name = "reject_msg")
    private String rejectMsg;

    @Column(name = "payback")
    private Long payback;

    @Column(name = "when_rejected")
    private Boolean whenRejected;

    @Column(name = "restore_status")
    @Enumerated(EnumType.STRING)
    private RestoreStatus restoreStatus;

    @OneToMany(mappedBy = "restore", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RestoreImage> restoreImages;
}
