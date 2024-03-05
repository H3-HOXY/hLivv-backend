package hoxy.hLivv.dto.product;

import org.springframework.data.domain.Sort;

public enum ProductSortCriteria {
    NEWEST("NEWEST"), POPULAR("POPULAR"), PRICE_ASC("PRICE_ASC"), PRICE_DESC("PRICE_DESC"), REVIEWS("REVIEWS");
    private String value;

    ProductSortCriteria(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    public Sort toOrder() {
        switch (this) {
            case NEWEST, REVIEWS, POPULAR:
                return Sort.unsorted();
            case PRICE_ASC:
                return Sort.by(Sort.Direction.ASC, "price");
            case PRICE_DESC:
                return Sort.by(Sort.Direction.DESC, "price");
            default:
                return Sort.by(Sort.Direction.DESC, "price");
        }
    }
}
