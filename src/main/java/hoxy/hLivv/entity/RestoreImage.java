package hoxy.hLivv.entity;
import hoxy.hLivv.dto.restore.RestoreDto;
import hoxy.hLivv.dto.restore.RestoreImageDto;
import jakarta.persistence.*;
import lombok.*;
@Entity
@Table(name = "restore_image")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RestoreImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ri_id")
    private Long riId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restore_id", nullable = false)
    private Restore restore;

    @Column(name = "ri_url")
    private String riUrl;



    // 연관 관계 설정이 필요한 경우 여기에 추가
}
