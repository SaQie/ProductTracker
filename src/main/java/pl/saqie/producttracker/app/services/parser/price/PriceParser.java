package pl.saqie.producttracker.app.services.parser.price;

import java.math.BigDecimal;

public interface PriceParser {
    BigDecimal parsePrice(String price);
}
