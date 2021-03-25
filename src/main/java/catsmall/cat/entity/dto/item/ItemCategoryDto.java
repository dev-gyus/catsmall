package catsmall.cat.entity.dto.item;

import catsmall.cat.entity.Category;
import catsmall.cat.entity.ItemCategory;
import catsmall.cat.entity.item.CatFood;
import catsmall.cat.entity.item.CatToilet;
import catsmall.cat.entity.item.CatTower;
import catsmall.cat.entity.item.Item;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

@Data
public class ItemCategoryDto {
    private List<Category> categories = new ArrayList<>();
    private List<CatFood> catFood = new ArrayList<>();
    private List<CatToilet> catToilet = new ArrayList<>();
    private List<CatTower> catTower = new ArrayList<>();

    public static ItemCategoryDto bindingItemCategory(List<ItemCategory> itemCategories) {
        ItemCategoryDto itemCategoryDto = new ItemCategoryDto();

        for (ItemCategory itemCategory : itemCategories) {
            if(!itemCategoryDto.categories.contains(itemCategory.getCategory())) {
                itemCategoryDto.categories.add(itemCategory.getCategory());
            }

            itemCategoryDto.selectAndBinding(itemCategory);
        }
        return itemCategoryDto;
    }

    private void selectAndBinding(ItemCategory itemCategory){
        Item item = itemCategory.getItem();
        if (itemCategory.getItem() instanceof CatFood) {
            if(!this.catFood.contains(itemCategory.getItem()))
            this.catFood.add((CatFood) item);
        } else if (itemCategory.getItem() instanceof CatToilet) {
            if(!this.catToilet.contains(itemCategory.getItem()))
            this.catToilet.add((CatToilet) item);
        } else if (itemCategory.getItem() instanceof CatTower) {
            if(!this.catTower.contains(itemCategory.getItem()))
            this.catTower.add((CatTower) item);
        }
    }
}
