package catsmall.cat.order;

import catsmall.cat.cart.*;
import catsmall.cat.entity.Order;
import catsmall.cat.entity.OrderItem;
import catsmall.cat.entity.item.Item;
import catsmall.cat.member.Address;
import catsmall.cat.member.CurrentUser;
import catsmall.cat.member.Member;
import catsmall.cat.order.dto.OrderDto;
import catsmall.cat.order.dto.OrderMainDto;
import catsmall.cat.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {
    private final OrderService orderService;
    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;
    private final CartRepository cartRepository;

    @GetMapping({"","/"})
    public String main(@CurrentUser Member member, Model model){
        List<Order> orders = orderRepository.findItemDeliveryByMemberId(member.getId());
        List<OrderMainDto> orderMainDtos = orders.stream().map(o -> new OrderMainDto(o.getId(), o.getOrderItems(), o.getOrderDate(),
                o.getOrderStatus(), o.getDelivery().getDeliveryStatus(), o.isReviewed())).collect(Collectors.toList());
        model.addAttribute("orderMainDtos", orderMainDtos);
        return "order/main";
    }


    // TODO 장바구니에서 주문들어갈때, 상품상세페이지에서 주문들어갈때 동작 별도 구현, 우선 별도구현하고 하나로 줄일수있으면 줄여볼것
    @GetMapping("/{itemId}")
    public String order(@CurrentUser Member member, @PathVariable Long itemId, @RequestParam(defaultValue = "0") int count,
                        @ModelAttribute OrderDto orderDto, Model model){
        Item item = itemRepository.findById(itemId).orElseThrow();
        if(count <= 0 || count > item.getQuantity()){
            model.addAttribute("item", item);
            model.addAttribute("hasErrors", true);
            return "board/item_detail";
        }
        Address address = member.getAddress();
        orderDto.setForOrder(address.getPostcode(), address.getRoadAddress(), address.getJibunAddress(), address.getDetailAddress(), count);
        model.addAttribute("item", item);
        model.addAttribute("orderDto",orderDto);
        return "order/order-input";
    }

    @PostMapping("/{itemId}")
    public String order_do(@CurrentUser Member member, @PathVariable Long itemId,
                        @ModelAttribute OrderDto orderDto, BindingResult result, Model model) throws IllegalAccessException {
        boolean hasErrors = orderService.order(member, itemId, orderDto, model);
        if(hasErrors){
            model.addAttribute("hasErrors", true);
            return "order/order-input";
        }
        return "redirect:/order";
    }
    @PostMapping("/add/cart")
    public String order_cart(@CurrentUser Member member, @ModelAttribute CartOrderDto cartOrderDto,
                             BindingResult result, Model model) throws IllegalAccessException {
        if(cartOrderDto.getCartOrderDtoList().isEmpty()){
            result.reject("empty", "상품은 최소 한개 이상 주문해주세요.");
            return "member/cart";
        }
        boolean hasErrors = orderService.cartOrderAndHasErrors(member, cartOrderDto.getCartOrderDtoList(), model);
        if(hasErrors){
            model.addAttribute("hasErrors", hasErrors);
            return "member/cart";
        }
        return "redirect:/order"; // TODO 주문 메인페이지로 리다이렉트시킬것
    }

    // TODO 이 주문이 지금 로그인한 멤버의 주문이 맞는건지 확인
    @PostMapping("/{orderId}/cancel")
    public String order_cancel(@CurrentUser Member member, @PathVariable Long orderId){
        orderService.cancelOrder(member, orderId);
        return "redirect:/order";
    }
}
