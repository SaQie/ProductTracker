package pl.saqie.producttracker.app.services.store.validator;

import pl.saqie.producttracker.app.error.product.NameReadingErrorException;
import pl.saqie.producttracker.app.error.product.NotASingleProductException;
import pl.saqie.producttracker.app.error.product.PriceReadingErrorException;
import pl.saqie.producttracker.app.services.store.Store;

public interface StoreValidatorChain {
    void chain(Store store) throws PriceReadingErrorException, NameReadingErrorException, NotASingleProductException;
}
