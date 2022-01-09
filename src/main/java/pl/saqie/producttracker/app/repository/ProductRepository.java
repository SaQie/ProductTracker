package pl.saqie.producttracker.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.saqie.producttracker.app.domain.Product;
import pl.saqie.producttracker.app.domain.ProductPrice;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("select distinct p from Product p join fetch p.priceList where p.user.id = :id")
    List<Product> customFindProductsByUserId(Long id);

    @Query("select distinct p from Product p join fetch p.priceList where p.id = :id")
    Optional<Product> customFindProduct(Long id);

    @Query("select distinct p from ProductPrice p join fetch p.product where p.product.id = :id order by p.updatedDate desc")
    Optional<LinkedList<ProductPrice>> customFindProductOrderByUpdatedDate(Long id);

    @Query("select distinct p from ProductPrice p join fetch p.product where p.product.id = :id order by p.storeName desc")
    Optional<LinkedList<ProductPrice>> customFindProductOrderByShopName(Long id);
}
