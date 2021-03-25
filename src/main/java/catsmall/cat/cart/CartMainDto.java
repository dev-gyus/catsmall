package catsmall.cat.cart;

import lombok.Data;

@Data
public class CartMainDto{
    private Long cartItemId;
    private Long itemId;
    private String itemName;
    private int count;
    private int price;
    private int eventPrice;
    private boolean event;
    private int quantity;

    public CartMainDto(Long cartItemId, Long itemId, String itemName, int count, int price, int eventPrice, boolean event, int quantity) {
        this.cartItemId = cartItemId;
        this.itemId = itemId;
        this.itemName = itemName;
        this.count = count;
        this.price = price;
        this.eventPrice = eventPrice;
        this.event = event;
        this.quantity = quantity;
    }
}