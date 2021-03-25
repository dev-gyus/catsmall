package catsmall.cat.util;

import catsmall.cat.entity.item.CatFood;
import catsmall.cat.entity.item.CatToilet;
import catsmall.cat.entity.item.CatTower;
import catsmall.cat.entity.item.Item;

public class ItemUtils {

    public static Item getSubClassByParentName(String category, String type) {
        if (type.equals("CatFood")) {
            return new CatFood(type);
        } else if (type.equals("CatToilet")) {
            return new CatToilet(type);
        } else if (type.equals("CatTower")) {
            return new CatToilet(type);
        }
        return null;
    }

    public static void changeItemType(Item item, String originType, String type) {
        if (item instanceof CatFood) {
            CatFood catFood = (CatFood) item;
            if(catFood.getType().equals(originType))
            catFood.setType(type);
        } else if (item instanceof CatToilet) {
            CatToilet catToilet = (CatToilet) item;
            if(catToilet.getType().equals(originType))
            catToilet.setType(type);
        } else if (item instanceof CatTower) {
            CatTower catTower = (CatTower) item;
            if(catTower.getType().equals(originType))
            catTower.setType(type);
        }
    }
}
