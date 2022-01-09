package pl.saqie.producttracker.app.services.store;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import pl.saqie.producttracker.app.services.parser.price.MediaMarktPriceParser;
import pl.saqie.producttracker.app.services.parser.store.JSoupHtmlParser;

import java.io.IOException;

@Component
@NoArgsConstructor
public class MediaMarktStore extends Store {

    public MediaMarktStore(String url) throws IOException {
        setShopName("www.mediamarkt.pl");
        setProductUrl(url);
        setPricePath("div.main-price");
        setNamePath("h1.is-heading");
        setHtmlParser(new JSoupHtmlParser());
        setPriceParser(new MediaMarktPriceParser());
        setDocument(getHtmlParser().getDocumentFromUrl(url));
        setMaximumNumberOfElements(15);
    }
}
