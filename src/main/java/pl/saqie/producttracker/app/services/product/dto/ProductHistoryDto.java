package pl.saqie.producttracker.app.services.product.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductHistoryDto {

    private String shopName;
    private BigDecimal productPrice;
    private String updatedDate;

}
