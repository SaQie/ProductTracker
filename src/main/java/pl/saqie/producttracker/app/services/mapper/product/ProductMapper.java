package pl.saqie.producttracker.app.services.mapper.product;

import pl.saqie.producttracker.app.config.DateFormatter;
import pl.saqie.producttracker.app.domain.Product;
import pl.saqie.producttracker.app.domain.ProductPrice;
import pl.saqie.producttracker.app.services.product.dto.ProductHistoryDto;
import pl.saqie.producttracker.app.services.product.dto.ProductListDto;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class ProductMapper {

    public static ProductListDto fromEntityToProductListDto(Product product) {
        return ProductListDto.builder()
                .productId(product.getId())
                .productDescription(product.getDescription())
                .productName(product.getName())
                .build();
    }

    public static LinkedList<ProductHistoryDto> fromProductPriceListToProductHistoryDto(LinkedList<ProductPrice> productPrices) {
        return productPrices.stream().map(p -> new ProductHistoryDto(p.getStoreName(), p.getPrice(), p.getUpdatedDate().format(DateFormatter.formatDate())))
                .collect(Collectors.toCollection(LinkedList::new));
    }
}
