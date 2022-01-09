package pl.saqie.producttracker.app.controller;

import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.saqie.producttracker.app.domain.User;
import pl.saqie.producttracker.app.services.product.ProductService;
import pl.saqie.producttracker.app.services.product.dto.ProductHistoryDto;

import java.util.List;

@Controller
@AllArgsConstructor
public class ProductHistoryController {

    private final ProductService productService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/product_history")
    public String getProductHistoryPageOrderByDate(@RequestParam(name = "id", defaultValue = "error") Long id, @RequestParam(name = "shopname", required = false) String order, Model model, @AuthenticationPrincipal User user){
        List<ProductHistoryDto> productHistory = productService.getProductHistory(id, user, order);
        model.addAttribute("productPriceList", productHistory);
        return "product_list_price_history";
    }
}
