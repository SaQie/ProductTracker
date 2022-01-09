package pl.saqie.producttracker.app.services.product;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.saqie.producttracker.app.config.AppProperties;
import pl.saqie.producttracker.app.config.DateFormatter;
import pl.saqie.producttracker.app.domain.Product;
import pl.saqie.producttracker.app.domain.ProductLink;
import pl.saqie.producttracker.app.domain.ProductPrice;
import pl.saqie.producttracker.app.domain.User;
import pl.saqie.producttracker.app.error.product.*;
import pl.saqie.producttracker.app.repository.ProductRepository;
import pl.saqie.producttracker.app.repository.UserRepository;
import pl.saqie.producttracker.app.services.mapper.product.ProductMapper;
import pl.saqie.producttracker.app.services.product.dto.ProductHistoryDto;
import pl.saqie.producttracker.app.services.product.dto.ProductListDto;

import java.io.IOException;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.ExecutionException;

@Service
@AllArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final SaveProductService productService;
    private final AppProperties appProperties;

    public List<ProductListDto> getProductList(User user) {
        List<Product> products = productRepository.customFindProductsByUserId(user.getId());
        return getProductListDtos(products);
    }

    public boolean refreshPrice(Long productId, User user) throws EmptyUrlsException, InvalidStoreException, IOException, NotASingleProductException, NameReadingErrorException, PriceReadingErrorException, ExecutionException, InterruptedException, CooldownException {
        checkUserLastUpdatedDate(user);
        return checkProductOwnerAndProcessRefreshPrice(productRepository.customFindProduct(productId), user);
    }

    private boolean checkProductOwnerAndProcessRefreshPrice(Optional<Product> productOptional, User user) throws EmptyUrlsException, NotASingleProductException, NameReadingErrorException, InvalidStoreException, IOException, PriceReadingErrorException {
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            if (product.getUser().equals(user)) {
                Set<String> urls = new HashSet<>();
                for (ProductLink productLink : product.getProductLinks()) {
                    urls.add(productLink.getLink());
                }
                productService.processRefreshPrice(urls, product);
                updateUserLastUpdatedDate(user);
                return true;
            }
        }
        return false;
    }

    private void updateUserLastUpdatedDate(User user) {
        user.setLastUpdatedDate(LocalTime.now());
        userRepository.save(user);
    }

    private void checkUserLastUpdatedDate(User user) throws CooldownException {
        if (user.getLastUpdatedDate() != null) {
            if (LocalTime.now().isBefore(user.getLastUpdatedDate().plusMinutes(appProperties.getRefreshCooldownInMinutes()))) {
                throw new CooldownException("Poczekaj " + appProperties.getRefreshCooldownInMinutes() + " minut przed kazdym kolejnym odswiezeniem produktu, ostatnie odswiezenie: " + user.getLastUpdatedDate());
            }
        }
    }

    private List<ProductListDto> getProductListDtos(List<Product> products) {
        List<ProductListDto> productListDtos = new ArrayList<>();
        for (Product product : products) {
            ProductListDto productDto = ProductMapper.fromEntityToProductListDto(product);
            for (ProductPrice productPrice : product.getPriceList()) {
                if (productPrice.getStoreName().contains("www.mediaexpert.pl")) {
                    productDto.setMediaExpertStore(productPrice.getPrice());
                }
                if (productPrice.getStoreName().contains("www.mediamarkt.pl")) {
                    productDto.setMediaMarktStore(productPrice.getPrice());
                }
                if (productPrice.getStoreName().contains("www.morele.net")) {
                    productDto.setMoreleStore(productPrice.getPrice());
                }
            }
            productListDtos.add(productDto);
        }
        return productListDtos;
    }

    public List<ProductHistoryDto> getProductHistory(Long id, User user, String order) {
        if (order == null) {
            Optional<LinkedList<ProductPrice>> optionalProduct = productRepository.customFindProductOrderByUpdatedDate(id);
            if (optionalProduct.isPresent()) {
                LinkedList<ProductPrice> productPrices = optionalProduct.get();
                if (productPrices.get(0).getProduct().getUser().getId().equals(user.getId())) {
                    return ProductMapper.fromProductPriceListToProductHistoryDto(productPrices);
                }
            }
        }else {
            Optional<LinkedList<ProductPrice>> optionalProduct = productRepository.customFindProductOrderByShopName(id);
            if (optionalProduct.isPresent()) {
                LinkedList<ProductPrice> productPrices = optionalProduct.get();
                if (productPrices.get(0).getProduct().getUser().getId().equals(user.getId())) {
                    return ProductMapper.fromProductPriceListToProductHistoryDto(productPrices);
                }
            }
        }
        return null;
    }

    public void deleteProduct(Long id, User user) {
        Optional<Product> productOptional = productRepository.findById(id);
        if (productOptional.isPresent()){
            Product product = productOptional.get();
            if (product.getUser().getId().equals(user.getId())){
                productRepository.deleteById(product.getId());
            }
        }
    }
}
