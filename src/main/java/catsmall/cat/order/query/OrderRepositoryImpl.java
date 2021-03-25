package catsmall.cat.order.query;

import catsmall.cat.delivery.QDelivery;
import catsmall.cat.entity.Order;

import catsmall.cat.entity.QOrderItem;
import catsmall.cat.entity.item.QItem;
import catsmall.cat.member.QMember;
import catsmall.cat.order.OrderStatus;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.util.List;

import static catsmall.cat.delivery.QDelivery.delivery;
import static catsmall.cat.entity.QOrder.order;
import static catsmall.cat.entity.QOrderItem.orderItem;
import static catsmall.cat.entity.item.QItem.item;
import static catsmall.cat.member.QMember.member;

public class OrderRepositoryImpl implements OrderQueryRepository{
    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    public OrderRepositoryImpl(EntityManager em){
        this.em = em;
        queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<Order> findOrderItemItemFetchByOrderId(Long id){
        return queryFactory
                .selectFrom(order)
                .join(order.orderItems, orderItem).fetchJoin()
                .join(orderItem.item, item).fetchJoin()
                .where(order.id.eq(id))
                .fetch();
    }

    @Override
    public Order findItemDeliveryById(Long id){
        return queryFactory
                .selectFrom(order)
                .join(order.orderItems, orderItem)
                .join(order.delivery, delivery)
                .where(order.id.eq(id))
                .fetchOne();
    }

    @Override
    public List<Order> findItemDeliveryByMemberId(Long memberId){
        return queryFactory
                .selectFrom(order)
                .distinct()
                .join(order.member, member).fetchJoin()
                .join(order.orderItems, orderItem).fetchJoin()
                .join(order.delivery, delivery).fetchJoin()
                .join(orderItem.item, item).fetchJoin()
                .where(member.id.eq(memberId).and(order.orderStatus.notIn(OrderStatus.CANCEL)))
                .fetch();
    }

}
