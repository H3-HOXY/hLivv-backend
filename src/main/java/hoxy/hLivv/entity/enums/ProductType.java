package hoxy.hLivv.entity.enums;

import hoxy.hLivv.entity.Collabo;
import hoxy.hLivv.entity.Product;

public enum ProductType {
    PRODUCT("PRODUCT"), COLLABO("COLLABO");

    private String value;

    ProductType(String value) {
        this.value = value;
    }

    public static ProductType getProductType(Product product) {
        if (product instanceof Collabo) return COLLABO;
        else return PRODUCT;
    }

    public String getValue() {
        return value;
    }
}
