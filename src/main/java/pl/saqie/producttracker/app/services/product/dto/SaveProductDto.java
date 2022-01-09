package pl.saqie.producttracker.app.services.product.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SaveProductDto {

    private String productName;
    private String shopName;
    private BigDecimal price;

}
