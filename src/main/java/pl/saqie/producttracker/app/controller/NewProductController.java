package pl.saqie.producttracker.app.controller;

import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import pl.saqie.producttracker.app.domain.User;
import pl.saqie.producttracker.app.error.product.*;
import pl.saqie.producttracker.app.services.product.SaveProductService;
import pl.saqie.producttracker.app.services.product.dto.NewProductDto;

import javax.validation.Valid;
import java.io.IOException;
import java.text.ParseException;

@Controller
@AllArgsConstructor
public class NewProductController {

    private final SaveProductService saveProductService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/new_product")
    public String getNewProductForm(Model model) {
        model.addAttribute(new NewProductDto());
        return "add_product";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/new_product")
    public String processSaveNewProduct(@ModelAttribute @Valid NewProductDto productDto, BindingResult bindingResult, Model model, @AuthenticationPrincipal User user) {
        if (!bindingResult.hasErrors()) {
            try {
                saveProductService.processSaveNewProduct(productDto, user);
                model.addAttribute("saveSuccess", "Pomyślnie zapisano produkt, znajdziesz go w swojej liście produktów.");
            } catch (PriceReadingErrorException | NameReadingErrorException | NotASingleProductException | InvalidStoreException | IncompatibleProductName | CooldownException | ParseException | EmptyUrlsException e) {
                model.addAttribute("error", e.getMessage());
            } catch (IOException e) {
                model.addAttribute("error", "Sprawdz, czy wprowadzony link jest poprawny.");
            }
        }
        model.addAttribute(new NewProductDto());
        return "add_product";
    }
}
