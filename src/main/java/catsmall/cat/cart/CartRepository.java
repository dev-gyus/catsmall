package catsmall.cat.cart;

import catsmall.cat.cart.query.CartQueryRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long>, CartQueryRepository {
    Cart findByMemberId(Long memberId);

    boolean existsByMemberId(Long memberId);


}
