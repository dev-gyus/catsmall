package catsmall.cat.entity.dto.item;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ItemListDto {
    private Long id;
    private String thumbnailName;
    private String name;
    private int price;
    private int discount;
    private int eventPrice;
    private boolean event;
    private String content;

    public ItemListDto(Long id, String thumbnailName, String name, int price, int discount, int eventPrice, boolean event) {
        this.id = id;
        this.thumbnailName = thumbnailName;
        this.name = name;
        this.price = price;
        this.discount = discount;
        this.eventPrice = eventPrice;
        this.event = event;
    }

    public ItemListDto(Long id, String thumbnailName, String name, int price, int discount,
                       int eventPrice, boolean event, String content) {
        this.id = id;
        this.thumbnailName = thumbnailName;
        this.name = name;
        this.price = price;
        this.discount = discount;
        this.eventPrice = eventPrice;
        this.event = event;
    }
}
