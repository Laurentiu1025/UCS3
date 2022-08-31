package ro.itschool.service;

import ro.itschool.entity.Product;
import ro.itschool.exception.NotEnoughProductsInStockException;

import java.math.BigDecimal;
import java.util.Map;

public interface ShoppingCartService {

    void addProduct(Product product);

    void removeProduct(Product product);

    Map<Product, Integer> getProductsInCart();

    void checkout() throws NotEnoughProductsInStockException;

    Long getTotal();
}
