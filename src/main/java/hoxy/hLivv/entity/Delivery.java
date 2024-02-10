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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "delivery_id")
    private Long deliveryId;

    @Column(name = "delivery_status")
    private DeliveryStatus deliveryStatus;

    @Column(name = "delivery_start")
    @Temporal(TemporalType.DATE)
    private Date deliveryStart;

    @Column(name = "delivery_end")
    @Temporal(TemporalType.DATE)
    private Date deliveryEnd;

}
