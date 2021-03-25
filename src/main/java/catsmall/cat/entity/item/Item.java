package catsmall.cat.entity.item;

import catsmall.cat.entity.ItemCategory;
import catsmall.cat.entity.OrderItem;
import catsmall.cat.review.Review;
import catsmall.cat.util.ItemUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class Item {
    @Id @GeneratedValue
    @Column(name = "item_id")
    private Long id;
    private String name;
    private int price;
    private int quantity;
    private String thumbnailName;
    private String thumbnailOriginal;
    @Lob @Basic(fetch = FetchType.EAGER)
    private String content;
    private int discount;
    private boolean event;
    private int eventPrice;
    @Enumerated(EnumType.STRING)
    private ItemStatus itemStatus = ItemStatus.READY;

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<ItemCategory> itemCategories = new ArrayList<>();

    /**
     * bussiness method
     */
    public void addQuantity(int count){
        quantity += count;
    }

    public void subtractQuantity(int count){
        if(count > quantity){
            throw new IllegalStateException("재고가 부족합니다.");
        }
        quantity -= count;
    }
    public Item bindingSubClassByCategory(String parentName, String type){
        Item tempItem = ItemUtils.getSubClassByParentName(parentName, type);
        tempItem.setId(this.id);
        tempItem.setName(this.name);
        tempItem.setPrice(this.price);
        tempItem.setQuantity(this.quantity);
        return tempItem;
    }
    public void changeItemType(String originType, String type){
        ItemUtils.changeItemType(this, originType, type);
    }
}
