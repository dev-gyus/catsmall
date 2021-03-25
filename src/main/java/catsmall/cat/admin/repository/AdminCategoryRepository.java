package catsmall.cat.admin.repository;

import catsmall.cat.admin.repository.query.AdminCategoryQueryRepository;
import catsmall.cat.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface AdminCategoryRepository extends JpaRepository<Category, Long>, AdminCategoryQueryRepository {
    Category findByName(String name);
}
