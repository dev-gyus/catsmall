package catsmall.cat.repository.query;

import catsmall.cat.entity.Category;

import java.util.List;

public interface CategoryQueryRepository {
    List<Category> findChildrenByParentName(String parentName);
}
