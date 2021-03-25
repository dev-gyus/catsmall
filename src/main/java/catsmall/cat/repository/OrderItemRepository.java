package catsmall.cat.repository;

import catsmall.cat.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
