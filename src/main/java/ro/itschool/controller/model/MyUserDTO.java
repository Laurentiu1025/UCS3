package ro.itschool.controller.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MyUserDTO {

    private Long id;
    private String username;
    private String fullName;
    private String email;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<String> roles = new ArrayList<>();
}


