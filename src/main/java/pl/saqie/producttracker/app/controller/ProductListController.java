package pl.saqie.producttracker.app.controller;

import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pl.saqie.producttracker.app.domain.User;
import pl.saqie.producttracker.app.services.product.ProductService;
import pl.saqie.producttracker.app.services.product.dto.ProductListDto;

import java.util.List;

@Controller
@AllArgsConstructor
public class ProductListController {

    private final ProductService productService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/product_price_list")
    public String getProductPriceListPage(@AuthenticationPrincipal User user, Model model) {
        List<ProductListDto> productListDtos = productService.getProductList(user);
        model.addAttribute("productList", productListDtos);
        return "product_list_price";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/product_list")
    public String getProductListStatusPage(@AuthenticationPrincipal User user, Model model) {
        List<ProductListDto> productListDtos = productService.getProductList(user);
        model.addAttribute("productList", productListDtos);
        return "product_list";
    }
}

