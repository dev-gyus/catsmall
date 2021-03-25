package catsmall.cat.order;

import catsmall.cat.entity.Order;
import catsmall.cat.order.query.OrderQueryRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface OrderRepository extends JpaRepository<Order, Long>, OrderQueryRepository {
    List<Order> findByMemberId(Long id);
}
