package pl.saqie.producttracker.app.services.store;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import pl.saqie.producttracker.app.services.parser.price.MoreleNetPriceParser;
import pl.saqie.producttracker.app.services.parser.store.JSoupHtmlParser;

import java.io.IOException;

@Component
@NoArgsConstructor
public class MoreleNetStore extends Store {

    public MoreleNetStore(String url) throws IOException {
        setShopName("www.morele.net");
        setProductUrl(url);
        setPricePath("div.product-price");
        setNamePath("h1.prod-name");
        setHtmlParser(new JSoupHtmlParser());
        setPriceParser(new MoreleNetPriceParser());
        setDocument(getHtmlParser().getDocumentFromUrl(url));
        setMaximumNumberOfElements(4);
    }
}
