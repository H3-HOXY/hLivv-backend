package hoxy.hLivv.dto;

import hoxy.hLivv.entity.Cart;
import hoxy.hLivv.entity.Collabo;
import hoxy.hLivv.entity.Product;
import hoxy.hLivv.entity.ProductCollabo;
import hoxy.hLivv.entity.enums.ProductType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartDto {
    private Long productId;
    private String productName;
    private Long unitPrice;
    private Long discountUnitPrice;
    private Integer cartQty;
    private Integer stockQuantity;
    private boolean isQrSupported;
    private boolean isArSupported;
    private boolean isEco;


    public static CartDto from(Cart cart) {
        Product product = cart.getProduct();
        long price = 0L;
        int stock = Integer.MAX_VALUE;
        if (ProductType.getProductType(product) == ProductType.COLLABO) {
            if (product instanceof Collabo collabo) {
                for (ProductCollabo productCollabo : collabo.getProductCollabo()) {
                    if (productCollabo.getProduct() != null && productCollabo.getQuantity() != null) {
                        Product product1 = productCollabo.getProduct();
                        price += (long) product1.getPrice() * productCollabo.getQuantity();
                        stock = Math.min(stock, product1.getStockQuantity());
                    }
                }
            }
        } else {
            price = product.getPrice();
            stock = product.getStockQuantity();
        }
        return CartDto.builder()
                .productId(product.getId())
                .productName(product.getName())
                .unitPrice(price)
                .discountUnitPrice((long) (price * (1-product.getDiscountPercent()*0.01)))
                .cartQty(cart.getCartQty())
                .stockQuantity(stock)
                .isArSupported(product.isArSupported())
                .isQrSupported(product.isQrSupported())
                .isEco(product.isEco())
                .build();
    }
}
