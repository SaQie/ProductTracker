package pl.saqie.producttracker.app.services.store.validator;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import pl.saqie.producttracker.app.error.product.NotASingleProductException;
import pl.saqie.producttracker.app.services.store.Store;

@Component
public class ProductListChain implements StoreValidatorChain {

    @Override
    public void chain(Store store) throws NotASingleProductException {
        Document document = store.getDocument();
        Elements element = document.select(store.getPricePath());
        if (element.size() > store.getMaximumNumberOfElements()) {
            throw new NotASingleProductException("Wykryliśmy, że sklep " + store.getShopName() + " nie odnosi się do jednego produktu");
        }
    }
}
