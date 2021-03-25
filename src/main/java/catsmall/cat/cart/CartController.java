package catsmall.cat.cart;

import catsmall.cat.entity.item.Item;
import catsmall.cat.member.CurrentUser;
import catsmall.cat.member.Member;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/cart")
public class CartController {
    private final CartRepository cartRepository;
    private final CartService cartService;
    private final CartItemService cartItemService;

    @PostMapping("/{id}/add")
    @ResponseBody
    public ResponseEntity cart_add(@CurrentUser Member member, @RequestBody CartDto cartDto, BindingResult result){
        cartService.addItem(member, cartDto, result);
        if(result.hasErrors()){
            return ResponseEntity.badRequest().body("장바구니 수량은 재고보다 적게 입력해주세요.");
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping({"", "/"})
    public String cart_main(@CurrentUser Member member, Model model){
        List<Cart> findCart = cartRepository.findAllCartItemAndItemByMemberId(member.getId());
        findCartAndCheck(findCart, model);
        return "member/cart";
    }

    @PostMapping("/{cartItemId}/modify")
    public String cartItem_modify(@CurrentUser Member member,@PathVariable Long cartItemId, @RequestParam int count){
        // TODO 카트수정 메소드 없앨것
        cartItemService.modifyCartItem(member, cartItemId, count);
        return "redirect:/cart";
    }

    @PostMapping("/{cartItemId}/delete")
    public String cartItem_delete(@CurrentUser Member member, @PathVariable Long cartItemId){
        cartItemService.deleteCartItem(member, cartItemId);
        return "redirect:/cart";
    }

    public static void findCartAndCheck(List<Cart> findCart, Model model){
        if(findCart.isEmpty()){
            model.addAttribute("cartMainDtoList", Collections.EMPTY_LIST);
        }else{
            List<CartItem> cartItemList = findCart.get(0).getCartItems();
            List<CartMainDto> cartMainDtoList = cartItemList.stream().map(ci ->
                    new CartMainDto(ci.getId() ,ci.getItem().getId(),
                            ci.getItem().getName(), ci.getCount(), ci.getPrice(), ci.getItem().getEventPrice(),
                            ci.getItem().isEvent(),ci.getItem().getQuantity()))
                    .collect(Collectors.toList());
            model.addAttribute("cartMainDtoList", cartMainDtoList);
        }
        model.addAttribute("cartOrderDto", new CartOrderDto());
    }
}
