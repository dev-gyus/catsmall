package catsmall.cat.admin.manage.dto;

import catsmall.cat.entity.ItemCategory;
import catsmall.cat.entity.dto.item.ItemDto;
import catsmall.cat.entity.item.Item;
import catsmall.cat.entity.item.ItemStatus;
import catsmall.cat.util.ItemUtils;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@NoArgsConstructor
public class ModifyItemDto {
    private Long id;
    @NotBlank
    private String name;

    @Min(message = "최소 1이상 입력해주세요", value = 1)
    private Integer price = 0;

    @Min(message = "최소 1이상 입력해주세요", value = 1)
    private Integer quantity = 0;

    private String category;

    private String type;

    @NotBlank(message = "상품 설명을 입력해주세요.")
    private String content;

    private MultipartFile file;

    private String thumbnailName;

    private String thumbnailOriginal;

    private List<ItemCategory> categories;

    private boolean event;

    private int discount;


    public ModifyItemDto(Long id, String name, Integer price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public ModifyItemDto(String name, Integer price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }
}
