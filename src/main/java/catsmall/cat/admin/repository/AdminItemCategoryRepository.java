package catsmall.cat.admin.repository;

import catsmall.cat.entity.ItemCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminItemCategoryRepository extends JpaRepository<ItemCategory, Long> {
}
