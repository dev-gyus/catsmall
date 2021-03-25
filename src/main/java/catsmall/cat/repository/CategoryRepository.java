package catsmall.cat.repository;

import catsmall.cat.entity.Category;
import catsmall.cat.repository.query.CategoryQueryRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long>, CategoryQueryRepository {
    List<Category> findByParentName(String parentname);
    Category findByName(String name);
}
