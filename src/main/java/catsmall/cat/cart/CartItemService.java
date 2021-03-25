package catsmall.cat.cart;

import catsmall.cat.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CartItemService {
    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;

    @Transactional
    public void modifyCartItem(Member member, Long cartItemId, int count) {
        CartItem cartItem = cartItemRepository.findItemFetchByCartItemId(cartItemId);
        int quantity = cartItem.getItem().getQuantity();
        if(count <= quantity && count > 0) {
            cartItem.modifyItemQuantity(count);
        }else{
            throw new IllegalArgumentException("잘못된 요청입니다.");
        }
    }

    @Transactional
    public void deleteCartItem(Member member, Long cartItemId){
        List<Cart> byId = cartRepository.findAllCartItemAndItemByMemberId(member.getId());
        Cart memberCart = byId.get(0);
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow();

        if(!memberCart.getCartItems().contains(cartItem)){  // 해당 멤버의 장바구니 아이템인지 확인
            throw new IllegalArgumentException("잘못된 요청입니다.");
        }
        memberCart.deleteItem(cartItem);
    }
}
