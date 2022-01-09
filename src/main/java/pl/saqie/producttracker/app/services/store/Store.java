package pl.saqie.producttracker.app.services.store;

import lombok.Getter;
import lombok.Setter;
import org.jsoup.nodes.Document;
import pl.saqie.producttracker.app.services.parser.price.PriceParser;
import pl.saqie.producttracker.app.services.parser.store.HtmlParser;

@Getter
@Setter
public abstract class Store {

    private String shopName;
    private String pricePath;
    private String namePath;
    private String productUrl;
    private HtmlParser htmlParser;
    private PriceParser priceParser;
    private Document document;
    private int maximumNumberOfElements;
    private String name;
    private String price;

}
