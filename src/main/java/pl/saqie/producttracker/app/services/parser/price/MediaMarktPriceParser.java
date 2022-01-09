package pl.saqie.producttracker.app.services.parser.price;

import java.math.BigDecimal;

public class MediaMarktPriceParser implements PriceParser {
    @Override
    public BigDecimal parsePrice(String priceFromWebPage) {
        String priceToParse = priceFromWebPage.replaceAll(" ", "").replaceFirst(",", ".").replaceFirst("-", "");
        return BigDecimal.valueOf(Double.parseDouble(priceToParse));
    }
}
