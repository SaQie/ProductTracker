package pl.saqie.producttracker.app.services.store.validator;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;
import pl.saqie.producttracker.app.error.product.PriceReadingErrorException;
import pl.saqie.producttracker.app.services.store.Store;

@Component
public class ReadPriceChain implements StoreValidatorChain {

    @Override
    public void chain(Store store) throws PriceReadingErrorException {
        Document document = store.getDocument();
        Element element = document.selectFirst(store.getPricePath());
        if (element == null) {
            throw new PriceReadingErrorException("Nie udało nam się odczytać ceny ze sklepu " + store.getShopName() + " sprawdz czy sklep jest poprawny.");
        } else {
            store.setPrice(element.text());
        }
    }
}
