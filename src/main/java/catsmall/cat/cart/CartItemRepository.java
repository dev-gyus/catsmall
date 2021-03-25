package catsmall.cat.cart;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    @Query("select ci from CartItem ci join fetch ci.item i where ci.Id = :id")
    CartItem findItemFetchByCartItemId(@Param("id") Long cartItemId);

}
