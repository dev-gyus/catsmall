package catsmall.cat.cart.query;

import catsmall.cat.cart.Cart;
import catsmall.cat.cart.CartItem;

import java.util.List;

public interface CartQueryRepository {
    List<Cart> findAllCartItemAndItemByMemberId(Long id);
}
