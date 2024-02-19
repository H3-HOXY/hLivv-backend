package hoxy.hLivv.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "category")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    @Id
    @Column(name = "category_id")
    private String id;

    @Column(name = "category_name")
    private String name;

    @OneToMany(mappedBy = "category")
    List<Product> products = new ArrayList<>();

    public Category(String id, String name) {
        this.id = id;
        this.name = name;
    }
}
