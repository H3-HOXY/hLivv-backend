package hoxy.hLivv.entity.compositekey;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author 반정현
 */
@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CartId implements Serializable {
    private Long memberId;
    private Long id;
}