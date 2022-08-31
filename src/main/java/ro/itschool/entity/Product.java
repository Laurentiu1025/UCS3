package ro.itschool.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "product")
@Getter
@Setter
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @Column(length = 45, nullable = false)
    @Size(max = 45, message = "Name must be maximum of 45 characters.")
    @NotBlank(message = "Name cannot be blank.")
    String name;

    @NotNull
    @ColumnDefault("0")
    @Max(value = 999999999, message= "Invalid quantity.")
    @Min(value = 0, message= "Quantity should be 0 or higher.")
    int quantity;

    @Column(nullable = false)
    @DecimalMin(value = "0.00", message = "*Price has to be non negative number")
    float price;

    @Column(name = "description")
    private String description;

    public Product() {
        super();
    }

    public Product(String name, int quantity) {
        super();
        this.name = name;
        this.quantity = quantity;
    }


    @Override
    public String toString() {
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = "";
        try {
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            jsonString = mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return jsonString;
    }
}

