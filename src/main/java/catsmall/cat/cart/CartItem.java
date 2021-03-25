package catsmall.cat.cart;

import catsmall.cat.entity.item.Item;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
public class CartItem {

    @Id @GeneratedValue
    @Column(name = "cart_item_id")
    private Long Id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    private Item item;

    private int count;
    private int price;


    // TODO 장바구니에서 담아둔건 재고차감하지말고, 주문된것만 재고차감할것

    public void subtractItemQuantity(){
        item.subtractQuantity(count);
    }

    public void addItemQuantity(){
        item.addQuantity(count);
    }
    public void setItem(Item item, int count){
        this.item = item;
        this.count = count;
        this.price = item.getPrice() * count;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public void modifyItemQuantity(int count) {
        this.count = count;
    }
}
