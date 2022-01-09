package pl.saqie.producttracker.app.services.parser.price;

import java.math.BigDecimal;

public class MoreleNetPriceParser implements PriceParser {
    @Override
    public BigDecimal parsePrice(String priceFromWebPage) {
        String priceToParse = priceFromWebPage.replaceAll("zł", "").replaceAll(" ", "").replaceFirst(",", ".");
        return BigDecimal.valueOf(Double.parseDouble(priceToParse));
    }
}
