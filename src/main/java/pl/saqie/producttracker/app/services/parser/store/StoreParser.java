package pl.saqie.producttracker.app.services.parser.store;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import pl.saqie.producttracker.app.error.product.InvalidStoreException;
import pl.saqie.producttracker.app.services.store.MediaExpertStore;
import pl.saqie.producttracker.app.services.store.MediaMarktStore;
import pl.saqie.producttracker.app.services.store.MoreleNetStore;
import pl.saqie.producttracker.app.services.store.Store;

import java.io.IOException;
import java.net.URL;

@Component
@NoArgsConstructor
public class StoreParser {

    public Store checkStore(URL productUrl) throws IOException, InvalidStoreException {
        return checkHostAndSelectStore(productUrl);
    }


    private Store checkHostAndSelectStore(URL productUrl) throws IOException, InvalidStoreException {
        String host = productUrl.getHost();
        return switch (host) {
            case "www.mediaexpert.pl", "mediaexpert.pl" -> new MediaExpertStore(productUrl.toString());
            case "www.morele.net", "morele.net" -> new MoreleNetStore(productUrl.toString());
            case "www.mediamarkt.pl", "mediamarkt.pl" -> new MediaMarktStore(productUrl.toString());
            default -> throw new InvalidStoreException("Nie obsługujemy takiego sklepu.");
        };
    }
}
