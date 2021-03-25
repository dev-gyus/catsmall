package catsmall.cat.cart.query;

import catsmall.cat.cart.Cart;
import catsmall.cat.cart.CartItem;
import catsmall.cat.cart.QCart;
import catsmall.cat.entity.item.QItem;
import catsmall.cat.member.QMember;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.util.List;

import static catsmall.cat.cart.QCart.cart;
import static catsmall.cat.cart.QCartItem.cartItem;
import static catsmall.cat.entity.item.QItem.item;
import static catsmall.cat.member.QMember.member;

public class CartRepositoryImpl implements CartQueryRepository {
    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    public CartRepositoryImpl(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<Cart> findAllCartItemAndItemByMemberId(Long id){
        return queryFactory
                .selectFrom(cart)
                .distinct()
                .leftJoin(cart.cartItems, cartItem).fetchJoin()
                .leftJoin(cartItem.item, item).fetchJoin()
                .join(cart.member, member).fetchJoin()
                .where(member.id.eq(id))
                .fetch();
    }
}
