package pl.saqie.producttracker.app.services.store;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import pl.saqie.producttracker.app.services.parser.price.MediaExpertPriceParser;
import pl.saqie.producttracker.app.services.parser.store.JSoupHtmlParser;

import java.io.IOException;

@Component
@NoArgsConstructor
public class MediaExpertStore extends Store {

    public MediaExpertStore(String url) throws IOException {
        setShopName("www.mediaexpert.pl");
        setProductUrl(url);
        setPricePath("div.main-price");
        setNamePath("h1.is-title");
        setHtmlParser(new JSoupHtmlParser());
        setPriceParser(new MediaExpertPriceParser());
        setDocument(getHtmlParser().getDocumentFromUrl(url));
        setMaximumNumberOfElements(4);
    }
}
