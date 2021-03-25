package catsmall.cat.search;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SearchDto {
    private Long itemId;
    private String itemName;
    private String thumbnailName;
    private int price;
    private boolean event;
    private int discount;
    private int eventPrice;
    private String content;

    public SearchDto(Long itemId, String itemName, String thumbnailName, int price, boolean event, int discount, int eventPrice) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.thumbnailName = thumbnailName;
        this.price = price;
        this.event = event;
        this.discount = discount;
        this.eventPrice = eventPrice;
    }
}
