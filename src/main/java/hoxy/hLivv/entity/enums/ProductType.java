package hoxy.hLivv.entity.enums;

import hoxy.hLivv.entity.Collabo;
import hoxy.hLivv.entity.Product;
import org.hibernate.Hibernate;

public enum ProductType {
    PRODUCT("PRODUCT"), COLLABO("COLLABO");

    private String value;

    ProductType(String value) {
        this.value = value;
    }

    public static ProductType getProductType(Product product) {
        if (Hibernate.getClass(product) == Collabo.class) return COLLABO;
        else return PRODUCT;
    }

    public String getValue() {
        return value;
    }
}
