package pl.saqie.producttracker.app.services.product.dto;

import lombok.*;

import javax.validation.constraints.Size;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewProductDto {

    @Size(min = 1, message = "Nazwa produktu nie może być pusta.")
    private String name;
    private String description;
    private Set<String> links;
}
