package pl.saqie.producttracker.app.services.store.validator;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;
import pl.saqie.producttracker.app.error.product.NameReadingErrorException;
import pl.saqie.producttracker.app.services.store.Store;

@Component
public class ReadNameChain implements StoreValidatorChain {

    @Override
    public void chain(Store store) throws NameReadingErrorException {
        Document document = store.getDocument();
        Element element = document.selectFirst(store.getNamePath());
        if (element == null) {
            throw new NameReadingErrorException("Nie udało nam się odczytać nazwy produktu ze sklepu " + store.getShopName() + " sprawdz czy sklep jest poprawny.");
        } else {
            store.setName(element.text());
        }
    }
}
