package catsmall.cat.order.query;

import catsmall.cat.entity.Order;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface OrderQueryRepository {
    List<Order> findOrderItemItemFetchByOrderId(Long id);
    Order findItemDeliveryById(Long id);
    List<Order> findItemDeliveryByMemberId(Long memberId);
}
