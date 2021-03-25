package catsmall.cat.entity.dto.item;

import catsmall.cat.entity.ItemCategory;
import catsmall.cat.entity.item.CatFood;
import catsmall.cat.entity.item.CatToilet;
import catsmall.cat.entity.item.Item;
import catsmall.cat.util.ItemUtils;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.web.multipart.MultipartFile;


import javax.annotation.RegEx;
import javax.validation.constraints.*;
import java.util.List;

@Data
@NoArgsConstructor
public class ItemDto {
    private Long id;
    @NotBlank
    private String name;

    @Min(message = "최소 1이상 입력해주세요", value = 1)
    private Integer price = 0;

    @Min(message = "최소 1이상 입력해주세요", value = 1)
    private Integer quantity = 0;

    @NotBlank(message = "카테고리를 선택해주세요.")
    private String category;

    @NotBlank(message = "제품타입을 선택해주세요.")
    private String type;

    @NotBlank(message = "상품 설명을 입력해주세요.")
    private String content;

    private MultipartFile thumbnail;

    private String thumbnailName;

    private String thumbnail_origin;

    private List<ItemCategory> categories;

    private boolean event;

    private int discount;

    private int eventPrice;

    public ItemDto(Long id, String name, Integer price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public ItemDto(String name, Integer price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public Item transformItem(ItemDto itemDto){
        Item tempItem = ItemUtils.getSubClassByParentName(itemDto.getCategory(), itemDto.getType());
        tempItem.setName(itemDto.getName());
        tempItem.setQuantity(itemDto.getQuantity());
        tempItem.setPrice(itemDto.getPrice());
        tempItem.setContent(itemDto.getContent());
        tempItem.setThumbnailOriginal(itemDto.getThumbnail().getOriginalFilename());
        tempItem.setThumbnailName(itemDto.getThumbnailName());
        return tempItem;
    }
}
