package pl.saqie.producttracker.app.controller;

import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.saqie.producttracker.app.domain.User;
import pl.saqie.producttracker.app.error.product.*;
import pl.saqie.producttracker.app.services.product.ProductService;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

@Controller
@AllArgsConstructor
public class ProductRefreshController {

    private final ProductService productService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/product_price_refresh")
    public String refreshProductPrice(@RequestParam(name = "id", defaultValue = "error") Long productId, @AuthenticationPrincipal User user) {
        try {
            if (productService.refreshPrice(productId, user)) {
                return "redirect:/product_price_list?r=y&productId=" + productId;
            }
        } catch (EmptyUrlsException | InterruptedException | ExecutionException | PriceReadingErrorException | NameReadingErrorException | NotASingleProductException | IOException | InvalidStoreException | CooldownException e) {
            return "redirect:/product_price_list?e=y&error=" + e.getMessage();
        }
        return "redirect:/product_price_list";
    }
}
