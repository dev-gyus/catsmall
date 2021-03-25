package catsmall.cat.admin.manage.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class AdminCategoryDto {
    private String first;
    private String parent;
    private String child;

    public AdminCategoryDto(String first) {
        this.first = first;
    }

    public AdminCategoryDto(String parent, String child) {
        this.parent = parent;
        this.child = child;
    }
}
