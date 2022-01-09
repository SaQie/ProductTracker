package pl.saqie.producttracker.app.services.product.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductListDto {

    private Long productId;
    private String productName;
    private String productDescription;
    private BigDecimal mediaExpertStore;
    private BigDecimal mediaMarktStore;
    private BigDecimal moreleStore;

    public ProductListDto(Long productId, String productName, String productDescription) {
        this.productId = productId;
        this.productName = productName;
        this.productDescription = productDescription;
    }
}
