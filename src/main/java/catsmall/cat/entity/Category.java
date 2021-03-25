package catsmall.cat.entity;

import catsmall.cat.entity.item.Item;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category {
    @Id @GeneratedValue
    @Column(name = "category_id")
    private Long id;

    @Column(unique = true)
    private String name;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<ItemCategory> itemCategory = new ArrayList<>();

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Category> children = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    public Category(String name) {
        this.name = name;
    }

    public void addChild(Category child){
        child.setParent(this);
        this.children.add(child);
    }


    public void deleteItem(ItemCategory itemCategory){
        this.itemCategory.removeIf(ic -> {
            if(ic.getId() == itemCategory.getId()){
                return true;
            }
            return false;
        });
    }
}
