package catsmall.cat.repository.query;

import catsmall.cat.entity.ItemCategory;
import catsmall.cat.entity.dto.item.ItemListDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ItemCategoryQueryRepository {
    Page<ItemCategory> findItemList(Pageable pageable, String category);

    Page<ItemListDto> findEventItems(Pageable pageable, String categoryName, String typeName);
}
