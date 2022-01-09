package pl.saqie.producttracker.app.services.product;

import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import pl.saqie.producttracker.app.config.AppProperties;
import pl.saqie.producttracker.app.domain.Product;
import pl.saqie.producttracker.app.domain.ProductLink;
import pl.saqie.producttracker.app.domain.ProductPrice;
import pl.saqie.producttracker.app.domain.User;
import pl.saqie.producttracker.app.error.product.*;
import pl.saqie.producttracker.app.repository.ProductRepository;
import pl.saqie.producttracker.app.repository.UserRepository;
import pl.saqie.producttracker.app.services.parser.store.StoreParser;
import pl.saqie.producttracker.app.services.product.dto.NewProductDto;
import pl.saqie.producttracker.app.services.product.dto.SaveProductDto;
import pl.saqie.producttracker.app.services.store.Store;
import pl.saqie.producttracker.app.services.store.validator.StoreValidatorChain;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class SaveProductService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final List<StoreValidatorChain> storeChain;
    private final StoreParser storeParser;
    private final AppProperties appProperties;

    public void processSaveNewProduct(NewProductDto newProduct, User user) throws IOException, IncompatibleProductName, InvalidStoreException, NotASingleProductException, NameReadingErrorException, PriceReadingErrorException, CooldownException, ParseException, EmptyUrlsException {
        checkUserLastAddedDate(user);
        startSaveProcess(newProduct, user);
    }

    private void checkUserLastAddedDate(User user) throws CooldownException {
        if (user.getLastAddDate() != null) {
            if (LocalTime.now().isBefore(user.getLastAddDate().plusMinutes(appProperties.getSaveCooldownInMinutes()))) {
                throw new CooldownException("Poczekaj " + appProperties.getSaveCooldownInMinutes() + " minut/y przed każdym ponownym zapisem produktu");
            }
        }
    }

    @Async
    public void startSaveProcess(NewProductDto newProduct, User user) throws IOException, IncompatibleProductName, InvalidStoreException, NotASingleProductException, NameReadingErrorException, PriceReadingErrorException, EmptyUrlsException {
        Set<URL> storeUrls = convertStringsToUrls(newProduct.getLinks());
        List<Store> storeList = assignTheRightStore(storeUrls);
        storeValidation(storeList);
        List<SaveProductDto> productDtos = convertStoreListToSaveProductDtos(storeList);
        validateProductName(productDtos, newProduct.getName());
        Product productToSave = createProductToSave(newProduct.getName(), newProduct, user);
        addProductPriceListToProductToSave(productDtos, productToSave);
        addProductLinksListToProductToSave(newProduct.getLinks(), productToSave);
        saveProduct(productToSave);
        updateUserLastAddDateAndNumberOfProducts(user);
    }

    private void updateUserLastAddDateAndNumberOfProducts(User user) {
        user.setNumberOfProducts(user.getNumberOfProducts() + 1);
        user.setLastAddDate(LocalTime.now());
        userRepository.save(user);
    }

    private void saveProduct(Product productToSave) {
        productRepository.save(productToSave);
    }

    private void addProductLinksListToProductToSave(Set<String> links, Product productToSave) {
        for (String link : links) {
            if (!link.isEmpty() && !link.isBlank()) {
                ProductLink productLink = new ProductLink(link);
                productToSave.addProductLink(productLink);
            }
        }
    }

    private void addProductPriceListToProductToSave(List<SaveProductDto> productDtos, Product productToSave) {
        for (SaveProductDto productDto : productDtos) {
            ProductPrice productPrice = new ProductPrice(productDto.getShopName(), productDto.getPrice(), LocalDateTime.now());
            productToSave.addProductPrice(productPrice);
        }
    }

    private Product createProductToSave(String productName, NewProductDto newProduct, User user) {
        return new Product(productName, newProduct.getDescription(), LocalDateTime.now(), user);
    }

    private void validateProductName(List<SaveProductDto> productDtos, String productName) throws IncompatibleProductName {
        productDtosNameValidation(productDtos, productName);
    }


    private void productDtosNameValidation(List<SaveProductDto> productDtos, String productName) throws IncompatibleProductName {
        for (SaveProductDto product : productDtos) {
            if (!product.getProductName().toLowerCase().contains(productName.toLowerCase())) {
                throw new IncompatibleProductName("Nazwa produktów nie pokrywa się, czy napewno wprowadziłeś te same produkty ?");
            }
        }
    }

    private List<SaveProductDto> convertStoreListToSaveProductDtos(List<Store> storeList) {
        List<SaveProductDto> saveProductDtoList = new ArrayList<>();
        for (Store store : storeList) {
            BigDecimal price = store.getPriceParser().parsePrice(store.getPrice());
            saveProductDtoList.add(new SaveProductDto(store.getName(), store.getShopName(), price));
        }
        return saveProductDtoList;
    }

    private void storeValidation(List<Store> storeList) throws NotASingleProductException, NameReadingErrorException, PriceReadingErrorException {
        for (Store store : storeList) {
            for (StoreValidatorChain chain : storeChain) {
                chain.chain(store);
            }
        }
    }

    private List<Store> assignTheRightStore(Set<URL> urls) throws IOException, InvalidStoreException {
        List<Store> storeList = new ArrayList<>();
        for (URL url : urls) {
            storeList.add(storeParser.checkStore(url));
        }
        return storeList;
    }

    private Set<URL> convertStringsToUrls(Set<String> links) throws EmptyUrlsException, MalformedURLException {
        Set<URL> storeUrls = new HashSet<>();
        for (String link : links) {
            if (!link.isEmpty() && !link.isBlank()) {
                storeUrls.add(new URL(link));
            }
        }
        checkstoreUrlsSize(storeUrls);
        return storeUrls;
    }

    private void checkstoreUrlsSize(Set<URL> links) throws EmptyUrlsException {
        if (links.size() == 0) {
            throw new EmptyUrlsException("Musisz wprowadzić chociaż jeden link do produktu.");
        }
    }

    @Async
    public void processRefreshPrice(Set<String> link, Product product) throws EmptyUrlsException, IOException, InvalidStoreException, NotASingleProductException, NameReadingErrorException, PriceReadingErrorException {
        Set<URL> urls = convertStringsToUrls(link);
        List<Store> storeList = assignTheRightStore(urls);
        storeValidation(storeList);
        List<SaveProductDto> productDtos = convertStoreListToSaveProductDtos(storeList);
        addProductPriceListToProductToSave(productDtos, product);
        productRepository.save(product);
    }
}
