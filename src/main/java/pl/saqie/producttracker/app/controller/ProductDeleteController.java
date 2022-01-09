package pl.saqie.producttracker.app.controller;

import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.saqie.producttracker.app.domain.User;
import pl.saqie.producttracker.app.services.product.ProductService;

@Controller
@AllArgsConstructor
public class ProductDeleteController {

    private final ProductService productService;


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/product_delete")
    public String deleteProduct(@RequestParam(name = "id", defaultValue = "error") Long id, @AuthenticationPrincipal User user){
        productService.deleteProduct(id, user);
        return "redirect:/menu";
    }
}
