package catsmall.cat.entity.dto;

import catsmall.cat.entity.item.CatFood;
import catsmall.cat.entity.item.CatToilet;
import catsmall.cat.entity.item.Item;
import catsmall.cat.util.ItemUtils;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ItemTypeDto {
    private String type;
    private String categoryName;
    private String originType;

    public ItemTypeDto(String type, String categoryName, String originType) {
        this.type = type;
        this.categoryName = categoryName;
        this.originType = originType;
    }

    public Item selectItemByType(String parentName, String type){
        return ItemUtils.getSubClassByParentName(parentName, type);
    }
}
