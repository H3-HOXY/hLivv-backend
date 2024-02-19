package hoxy.hLivv.entity;

import hoxy.hLivv.entity.enums.DeliveryStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "delivery")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Delivery {
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="delivery_seq")
    @SequenceGenerator(name="delivery_seq", sequenceName="delivery_seq", allocationSize=1)
    @Column(name = "delivery_id")
    private Long deliveryId;

    @Column(name = "delivery_status")
    @Enumerated(EnumType.STRING)
    private DeliveryStatus deliveryStatus;

    @Column(name = "delivery_start")
    @Temporal(TemporalType.DATE)
    private Date deliveryStart;

    @Column(name = "delivery_end")
    @Temporal(TemporalType.DATE)
    private Date deliveryEnd;

}
