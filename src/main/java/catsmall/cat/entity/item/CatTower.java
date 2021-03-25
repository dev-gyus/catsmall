package catsmall.cat.entity.item;

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
public class CatTower extends Item{
    private String type;

    public CatTower(String name, int price, int quantity, String type) {
        super.setName(name);
        super.setPrice(price);
        super.setQuantity(quantity);
        this.type = type;
    }

    public CatTower(String type) {
        this.type = type;
    }
}
