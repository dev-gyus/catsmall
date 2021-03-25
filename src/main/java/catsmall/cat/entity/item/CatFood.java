package catsmall.cat.entity.item;

import catsmall.cat.entity.ItemCategory;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;

@Entity
@DiscriminatorColumn
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CatFood extends Item{
    private String type;

    public CatFood(String name, int price, int quantity, String type) {
        super.setName(name);
        super.setPrice(price);
        super.setQuantity(quantity);
        this.type = type;
    }

    public CatFood(String type) {
        this.type = type;
    }
}
