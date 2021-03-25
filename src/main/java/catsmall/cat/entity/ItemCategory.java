package catsmall.cat.entity;

import catsmall.cat.entity.item.Item;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;

import javax.persistence.*;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemCategory {
    @Id @GeneratedValue
    @Column(name = "item_category_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category = new Category();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    public ItemCategory(Category category, Item item) {
        this.category = category;
        this.item = item;
        category.getItemCategory().add(this);
        item.getItemCategories().add(this);
    }

    public ItemCategory(Category category) {
        setCategory(category);
        category.getItemCategory().add(this);
    }

}
