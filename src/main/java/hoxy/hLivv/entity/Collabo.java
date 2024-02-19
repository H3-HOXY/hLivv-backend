package hoxy.hLivv.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SuperBuilder
@Entity(name = "collabo")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Collabo extends Product {
    @OneToMany(mappedBy = "collabo", cascade = CascadeType.ALL)
    private List<ProductCollabo> productCollabo = new ArrayList<>();

    private Date startDate;
    private Date endDate;
}
