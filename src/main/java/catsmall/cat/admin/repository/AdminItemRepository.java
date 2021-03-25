package catsmall.cat.admin.repository;

import catsmall.cat.admin.repository.query.AdminItemQueryRepository;
import catsmall.cat.entity.item.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface AdminItemRepository extends JpaRepository<Item, Long>, AdminItemQueryRepository {

}
