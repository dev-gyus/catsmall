package catsmall.cat.cart;

import catsmall.cat.entity.item.Item;
import catsmall.cat.member.Member;
import catsmall.cat.member.MemberRepository;
import catsmall.cat.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CartService {
    private final CartRepository cartRepository;
    private final ItemRepository itemRepository;
    private final CartItemRepository cartItemRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void addItem(Member member, CartDto cartDto, BindingResult result) {
        Member findMember = memberRepository.findCartFetchByMemberId(member.getId());
        Cart memberCart = findMember.getCart();
        Item findItem = itemRepository.findById(cartDto.getItemId()).orElseThrow();
        if(cartDto.getCount() > findItem.getQuantity() || cartDto.getCount() <= 0){
            result.reject("over.count", "재고보다 많은 구매수량 혹은 0이하 수량");
            return;
        }
        CartItem cartItem = new CartItem();
        cartItem.setItem(findItem, cartDto.getCount());

        if(memberCart == null){
            Cart cart = new Cart(member, cartItem); // 멤버에 해당 카트 상태 세팅됨
            findMember.setCart(cart);
            cartItemRepository.save(cartItem);
            cartRepository.save(cart);
        }else{
            cartItemRepository.save(cartItem);
            memberCart.addItem(cartItem);
        }
    }
}
