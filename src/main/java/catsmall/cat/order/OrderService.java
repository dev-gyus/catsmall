package catsmall.cat.order;

import catsmall.cat.alert.AlertKinds;
import catsmall.cat.alert.AlertUtils;
import catsmall.cat.cart.Cart;
import catsmall.cat.cart.CartController;
import catsmall.cat.cart.CartOrderDto;
import catsmall.cat.cart.CartRepository;
import catsmall.cat.delivery.DeliveryRepository;
import catsmall.cat.delivery.DeliveryStatus;
import catsmall.cat.delivery.Delivery;
import catsmall.cat.entity.Order;
import catsmall.cat.entity.OrderItem;
import catsmall.cat.entity.item.Item;
import catsmall.cat.entity.item.ItemStatus;
import catsmall.cat.member.Member;
import catsmall.cat.member.MemberRepository;
import catsmall.cat.order.dto.OrderDto;
import catsmall.cat.repository.ItemRepository;
import catsmall.cat.repository.OrderItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {
    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ItemRepository itemRepository;
    private final DeliveryRepository deliveryRepository;
    private final CartRepository cartRepository;
    private final AlertUtils alertUtils;

    @Transactional
    public void cancelOrder(Member member, Long orderId){
        List<Order> findOrder = orderRepository.findOrderItemItemFetchByOrderId(orderId);
        findOrder.get(0).getOrderItems().forEach(oi -> oi.cancelOrder());
        findOrder.get(0).setOrderStatus(OrderStatus.CANCEL);
        List<OrderItem> orderItems = findOrder.get(0).getOrderItems();
        for (OrderItem orderItem : orderItems) {
            Item item = orderItem.getItem();
            if(item.getItemStatus() == ItemStatus.SOLDOUT && item.getQuantity() >= 0){
                item.setItemStatus(ItemStatus.NOWSALE);
                alertUtils.makeInStockAlert(item);
            }
        }
    }

    @Transactional
    public boolean order(Member member, Long itemId, OrderDto orderDto, Model model) throws IllegalAccessException {
        Item findItem = itemRepository.findById(itemId).orElseThrow();
        if(findItem.getItemStatus() != ItemStatus.NOWSALE){
            throw new IllegalAccessException("잘못된 접근입니다.");
        }
        if(orderDto.getCount() <= 0 || orderDto.getCount() > findItem.getQuantity()){
            model.addAttribute("item", findItem);
            return true;
        }
        Member findMember = memberRepository.findMemberByEmail(member.getEmail());
        Delivery delivery = new Delivery(findMember.getAddress(), DeliveryStatus.READY);
        deliveryRepository.save(delivery);
        Order order = new Order(findMember, delivery);
        orderSettingProcess(findMember, order, findItem, orderDto.getCount());
        orderRepository.save(order);
        if(findItem.getQuantity() <= 0){
            findItem.setItemStatus(ItemStatus.SOLDOUT);
        }
        return false;
    }

    @Transactional
    public boolean cartOrderAndHasErrors(Member member, List<CartOrderDto> cartOrderDtoList, Model model) throws IllegalAccessException {
        Member findMember = memberRepository.findCartFetchByMemberId(member.getId());
        Delivery delivery = new Delivery(findMember.getAddress(), DeliveryStatus.READY);
        deliveryRepository.save(delivery);
        Order order = new Order(findMember, delivery);
        for (int i=0; i < cartOrderDtoList.size(); i++) {
            CartOrderDto cartOrderDto = cartOrderDtoList.get(i);
            Item findItem = itemRepository.findById(cartOrderDto.getItemId()).orElseThrow();

            if(findItem.getItemStatus() != ItemStatus.NOWSALE){
                throw new IllegalAccessException("잘못된 접근입니다.");
            }

            // 구매수량 0개 혹은 재고보다 많은수량 구매요청 들어왔을경우 예외처리
            if(cartOrderDto.getCount() <= 0 || cartOrderDto.getCount() > findItem.getQuantity()) {
                List<Cart> findCart = cartRepository.findAllCartItemAndItemByMemberId(member.getId());
                CartController.findCartAndCheck(findCart, model);
                return true;
            }
            orderSettingProcess(findMember, order, findItem, cartOrderDto.getCount());
            if(findItem.getQuantity() <= 0){
                findItem.setItemStatus(ItemStatus.SOLDOUT);
            }
        }
        findMember.setCart(null);
        orderRepository.save(order);
        return false;
    }

    private void orderSettingProcess(Member findMember, Order order, Item findItem, int count) {
        OrderItem orderItem = new OrderItem(findItem, count);
        orderItemRepository.save(orderItem);
        order.setMember(findMember);
        order.addItem(orderItem);
    }
}
