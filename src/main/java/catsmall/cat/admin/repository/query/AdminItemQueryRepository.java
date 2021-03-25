package catsmall.cat.admin.repository.query;

import catsmall.cat.entity.ItemCategory;
import catsmall.cat.entity.item.Item;
import com.querydsl.core.Tuple;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface AdminItemQueryRepository {
    List<ItemCategory> findAllCategoryAndTypes();
    List<ItemCategory> findTypesByCategoryName(String category);

    Item findCategoryFetchByItemId(Long itemId);

    Page<ItemCategory> findItemPagingFetchByCategory(String categoryName, Pageable pageable);
}
