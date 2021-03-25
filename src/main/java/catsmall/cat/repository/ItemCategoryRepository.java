package catsmall.cat.repository;

import catsmall.cat.entity.ItemCategory;
import catsmall.cat.repository.query.ItemCategoryQueryRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface ItemCategoryRepository extends JpaRepository<ItemCategory, Long>, ItemCategoryQueryRepository {
    List<ItemCategory> findAllByCategoryId(Long categoryId);
}
