package catsmall.cat.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class CategoryDto {
    private Long id;
    @NotBlank
    @Size(message = "최소 1글자 이상, 최대 10글자까지 입니다.",min = 1, max = 10)
    private String name;
    @NotBlank
    @Size(message = "최소 1글자 이상, 최대 10글자까지 입니다.", min = 1, max = 10)
    private String itemType;
    private List<ItemCategory> itemCategory = new ArrayList<>();

    public CategoryDto(String name, List<ItemCategory> itemCategory) {
        this.name = name;
    }

    public CategoryDto(String name) {
        this.name = name;
    }

    public void setCategoryDto(Category category){
        this.id = category.getId();
        this.name = category.getName();
        this.itemCategory = category.getItemCategory();
    }
}
