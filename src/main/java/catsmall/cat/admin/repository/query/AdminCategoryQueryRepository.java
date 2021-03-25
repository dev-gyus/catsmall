package catsmall.cat.admin.repository.query;

import catsmall.cat.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AdminCategoryQueryRepository {
    List<Category> findTopCategories();
    Category findChildrenFetchByParentName(String parentName);
    Category findChildWithItemCatByChildName(String childName);

    Page<Category> findChildItemsFetchByParentName(String parentName, Pageable pageable);

    List<Category> findAllIgnoreTopCategories();
}
