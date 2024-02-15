package hoxy.hLivv.dto;

import hoxy.hLivv.entity.Cart;
import hoxy.hLivv.entity.Collabo;
import hoxy.hLivv.entity.CollaboProduct;
import hoxy.hLivv.entity.Product;
import hoxy.hLivv.entity.enums.ProductType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Getter
@Builder
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
public class CartDto {
    private String productName;
    private Long totalPrice;
    private Integer cartQty;
    private Integer stockQuantity;


    public static CartDto from(Cart cart) {
        Product product = cart.getProduct();
        Long price = 0L;
        Integer stock = Integer.MAX_VALUE;
        if (ProductType.getProductType(product) == ProductType.COLLABO) {
            if (product instanceof Collabo) {
                Collabo collabo = (Collabo) product;
                for (CollaboProduct collaboProduct : collabo.getCollaboProduct()) {
                    if (collaboProduct.getProduct() != null && collaboProduct.getQuantity() != null) {
                        Product product1 = collaboProduct.getProduct();
                        price += product1.getPrice() * collaboProduct.getQuantity();
                        stock = Math.min(stock, product1.getStockQuantity());
                    }
                }
            }
        } else {
            price = (long) product.getPrice();
            stock = product.getStockQuantity();
        }
        return CartDto.builder()
                .productName(product.getName())
                .totalPrice(price * cart.getCartQty())
                .cartQty(cart.getCartQty())
                .stockQuantity(stock)
                .build();
    }
}
