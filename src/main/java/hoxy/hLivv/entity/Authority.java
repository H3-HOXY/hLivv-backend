package hoxy.hLivv.entity;

//import jakarta.persistence.Column;
//import jakarta.persistence.Entity;
//import jakarta.persistence.Id;
//import jakarta.persistence.Table;
import lombok.*;




import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "authority")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Authority {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "authority_id")
   private Long authorityId;

   @Column(name = "authority_name")
   private String authorityName;

   @OneToMany(mappedBy = "authority", cascade = CascadeType.ALL, orphanRemoval = true)
   private Set<MemberAuthority> memberAuthorities = new HashSet<>();

}

//@Entity
//@Table(name = "authority")
//@Getter
//@Setter
//@Builder
//@AllArgsConstructor
//@NoArgsConstructor
//public class Authority {
//
//   @Id
//   @Column(name = "authority_name", length = 50)
//   private String authorityName;
//}
