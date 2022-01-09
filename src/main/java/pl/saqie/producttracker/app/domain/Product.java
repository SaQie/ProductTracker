package pl.saqie.producttracker.app.domain;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private LocalDateTime createdDate;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<ProductPrice> priceList;

    @ManyToOne
    private User user;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<ProductLink> productLinks;

    public Product(String name, String description, LocalDateTime createdDate, User user) {
        this.name = name;
        this.description = description;
        this.createdDate = createdDate;
        this.user = user;
        this.productLinks = new ArrayList<>();
        this.priceList = new ArrayList<>();
    }

    public void addProductPrice(ProductPrice productPrice) {
        priceList.add(productPrice);
        productPrice.setProduct(this);
    }

    public void addProductLink(ProductLink productLink) {
        productLinks.add(productLink);
        productLink.setProduct(this);
    }

}
