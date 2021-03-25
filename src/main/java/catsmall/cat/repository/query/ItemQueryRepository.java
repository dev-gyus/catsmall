package catsmall.cat.repository.query;

import catsmall.cat.entity.dto.ItemTypeDto;
import catsmall.cat.entity.dto.item.ItemListDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ItemQueryRepository {

    List<ItemTypeDto> findItemTypesByCategory(String category);

    Page<ItemListDto> findAllCategoryItemByType(String category, String type, Pageable pageable, Boolean isEvent);
}
