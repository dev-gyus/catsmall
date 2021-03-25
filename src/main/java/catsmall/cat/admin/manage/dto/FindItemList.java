package catsmall.cat.admin.manage.dto;

import catsmall.cat.entity.item.Item;
import catsmall.cat.entity.item.ItemStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class FindItemList {
    private String category;
    private Long itemId;
    private String itemName;
    private String thumbnailName;
    private int quantity;
    private int price;
    private boolean event;
    private int eventPrice;
    private int discount;
    private ItemStatus itemStatus;

    public FindItemList(Long itemId, String itemName, String thumbnailName, int quantity,
                        int price, boolean event, int eventPrice, int discount, ItemStatus itemStatus) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.thumbnailName = thumbnailName;
        this.quantity = quantity;
        this.price = price;
        this.event = event;
        this.eventPrice = eventPrice;
        this.discount = discount;
        this.itemStatus = itemStatus;
    }
}
