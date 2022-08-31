package ro.itschool.controller;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.servlet.view.RedirectView;
import ro.itschool.entity.Product;
import ro.itschool.exception.ProductException;
import ro.itschool.repository.ProductRepository;
import ro.itschool.service.ProductService;
import ro.itschool.util.FileUploadUtil;

import java.io.IOException;
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

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/product/{id}")
    public String product(@PathVariable Long id, Model model) {
        model.addAttribute("movie", productRepository.findById(id).get());
        return "product";
    }

    @PostMapping("/saveProduct")
    public RedirectView saveProduct(@ModelAttribute("product") Product product,
                                  @RequestParam("product") MultipartFile multipartFile) throws IOException {
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));

        Product saveProduct = productService.saveProduct(product);

        String uploadDir = "product-photos/" + saveProduct.getId();

        FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        return new RedirectView("/", true);
    }


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

    @GetMapping("/addProduct")
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

    @DeleteMapping("/deleteProduct/{id}")
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
