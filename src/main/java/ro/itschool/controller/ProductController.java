package ro.itschool.controller;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ro.itschool.entity.Product;
import ro.itschool.exception.ProductException;
import ro.itschool.service.ProductService;

import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/products")
public class ProductController {

    public static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductService productService;

    @GetMapping()
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> list = productService.getAllProducts();
        return ResponseEntity.ok(list);
        // return new ResponseEntity<List<Product>>(list, HttpStatus.OK);
    }

    @PostMapping("/find-product-name/product")
    public ResponseEntity<?> findProductByName(@RequestParam("product") String name) throws ProductException {
        Product product = productService.findProductByName(name);
        if (product == null) {
            logger.error("Product with name {} not found.", name);
            throw new ProductException("Product with name " + name + " not found", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(product);
    }

    @GetMapping("/find-product-id/{id}")
    public ResponseEntity<?> findProductById(@PathVariable Long id) throws ProductException {
        Optional<Product> product = productService.findProductById(id);
        if (product.isEmpty()) {
            logger.error("Product with id {} not found.", id);
            throw new ProductException("Product with id " + id + " not found", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(product);
    }

    @PostMapping()
    public ResponseEntity<?> addProduct(@Valid @RequestBody Product product, Errors errors) throws ProductException {

        if (errors.hasErrors()) {
            throw new ProductException(Objects.requireNonNull(errors.getFieldError()).getDefaultMessage(), HttpStatus.BAD_REQUEST);
        }

        boolean productExists = productService.findProductByName(product.getName()) != null;
        if (productExists) {
            logger.error("Unable to create. A product with name {} already exist", product.getName());
            throw new ProductException("Unable to create. A product with name " + product.getName() + " already exist.",
                    HttpStatus.CONFLICT);

        }

        productService.addProduct(product);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(product.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @PutMapping("/update-product/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Long id, @Valid @RequestBody Product product, Errors errors)
            throws ProductException {
        if (errors.hasErrors()) {
            throw new ProductException(Objects.requireNonNull(errors.getFieldError()).getDefaultMessage(), HttpStatus.BAD_REQUEST);
        }

        Optional<Product> currentProduct = productService.findProductById(id);
        if (currentProduct.isEmpty()) {
            logger.error("Unable to update. User with id {} not found.", id);
            throw new ProductException("Unable to update. Product with id " + id + " not found", HttpStatus.NOT_FOUND);
        }
        productService.updateProduct(id, product);

        return ResponseEntity.ok(product);
    }

    @DeleteMapping("/delete-product/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) throws ProductException {
        Optional<Product> currentProduct = productService.findProductById(id);
        if (currentProduct.isEmpty()) {
            logger.error("Unable to delete. Product with id {} not found.", id);
            throw new ProductException("Unable to delete. Product with id " + id + " not found", HttpStatus.NOT_FOUND);
        }
        productService.deleteProduct(id);
        return new ResponseEntity<Product>(HttpStatus.NO_CONTENT);
    }
}
