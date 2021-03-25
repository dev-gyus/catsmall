package catsmall.cat.repository;


import catsmall.cat.entity.item.CatFood;
import catsmall.cat.entity.item.CatToilet;
import catsmall.cat.entity.item.CatTower;
import catsmall.cat.entity.item.Item;
import catsmall.cat.repository.query.ItemQueryRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface ItemRepository extends JpaRepository<Item, Long>, ItemQueryRepository {
    CatFood findCatFoodById(Long id);
    CatToilet findCatToiletById(Long id);
    CatTower findCatTowerById(Long id);

}
