package catsmall.cat.entity;

import catsmall.cat.entity.item.CatFood;
import catsmall.cat.entity.item.Item;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.awt.print.Book;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem {
    @Id @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    private int orderPrice;
    private int count;

    public OrderItem(Item item, int count) {
            setItem(item);
            this.count = count;
            if(item.isEvent()){
                orderPrice = item.getEventPrice() * this.count;
            }else {
                orderPrice = item.getPrice() * this.count;
            }
    }

    // business method
    private void setItem(Item item){

        this.item = item;
    }
    private Class<? extends Item> convertItemClass(){
        CatFood catFood = (CatFood) item;
        return catFood.getClass();
    }

    // 나중에 서비스클래스로 옮길것
    public void subtractItem(){
        item.subtractQuantity(count);

    }
    // 나중에 서비스클래스로 옮길것
    public void cancelOrder(){
        item.addQuantity(count);
    }

}
