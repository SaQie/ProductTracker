package pl.saqie.producttracker.app.services.parser.price;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@NoArgsConstructor
public class MediaExpertPriceParser implements PriceParser {

    public BigDecimal parsePrice(String priceFromWebPage) {
        String priceToParse = priceFromWebPage.replaceAll("zł", "").replaceAll(" ", "").replaceFirst(" ", ".");
        return BigDecimal.valueOf(Double.parseDouble(priceToParse));
    }
}
