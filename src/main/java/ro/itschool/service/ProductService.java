package ro.itschool.service;

import ro.itschool.entity.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    Product saveProduct(Product product);

    List<Product> getAllProducts();

    Optional<Product> findProductById(Long id);

    Product findProductByName(String name);

    void addProduct(Product product);

    void updateProduct(Long id, Product product);

    void deleteProduct(Long id);

}
