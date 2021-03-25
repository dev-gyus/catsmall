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
public class CatToilet extends Item{
    private String type;

    public CatToilet(String name, int price, int quantity, String type) {
        super.setName(name);
        super.setPrice(price);
        super.setQuantity(quantity);
        this.type = type;
    }

    public CatToilet(String type) {
        this.type = type;
    }
}
