package catsmall.cat.cart;

import catsmall.cat.entity.item.Item;
import catsmall.cat.member.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Cart {
    @Id @GeneratedValue
    @Column(name = "cart_id")
    private Long id;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> cartItems = new ArrayList<>();

    @OneToOne(mappedBy = "cart")
    private Member member;

    public Cart(Member member) {
        this.member = member;
        member.setCart(this);
    }

    public Cart(Member member, CartItem cartItem){
        this.member = member;
        addItem(cartItem);
        member.setCart(this);
    }

    public void addItem(CartItem cartItem){
        this.cartItems.add(cartItem);
        cartItem.setCart(this);
    }
    public void deleteItem(CartItem cartItem){
        this.cartItems.remove(cartItem);
    }
}
