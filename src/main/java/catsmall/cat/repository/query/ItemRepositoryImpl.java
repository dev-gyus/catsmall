package catsmall.cat.repository.query;

import catsmall.cat.entity.QCategory;
import catsmall.cat.entity.dto.item.ItemDto;
import catsmall.cat.entity.dto.ItemTypeDto;
import catsmall.cat.entity.dto.item.ItemListDto;
import catsmall.cat.entity.item.*;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;

import java.util.List;

import static catsmall.cat.entity.QCategory.category;
import static catsmall.cat.entity.QItemCategory.itemCategory;
import static catsmall.cat.entity.item.QCatFood.catFood;
import static catsmall.cat.entity.item.QCatToilet.catToilet;
import static catsmall.cat.entity.item.QCatTower.catTower;

public class ItemRepositoryImpl implements ItemQueryRepository {

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    public ItemRepositoryImpl(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<ItemTypeDto> findItemTypesByCategory(String category) {
        return selectItemTypesByCategory(category);
    }

    private List<ItemTypeDto> selectItemTypesByCategory(String category) {
        if (category.equals("CatTower")) {
            return queryFactory
                    .select(Projections.bean(ItemTypeDto.class, catTower.type, QCategory.category.name.as("categoryName")))
                    .from(catTower)
                    .join(catTower.itemCategories, itemCategory)
                    .join(itemCategory.category, QCategory.category)
                    .where(QCategory.category.name.eq(category).and(catTower.itemStatus.notIn(ItemStatus.READY)))
                    .fetch();
        } else if (category.equals("CatToilet")) {
            return queryFactory
                    .select(Projections.bean(ItemTypeDto.class, catToilet.type, QCategory.category.name.as("categoryName")))
                    .from(catToilet)
                    .join(catToilet.itemCategories, itemCategory)
                    .join(itemCategory.category, QCategory.category)
                    .where(QCategory.category.name.eq(category).and(catToilet.itemStatus.notIn(ItemStatus.READY)))
                    .fetch();
        } else if (category.equals("CatFood")) {
            return queryFactory
                    .select(Projections.bean(ItemTypeDto.class, catFood.type, QCategory.category.name.as("categoryName")))
                    .from(catFood)
                    .join(catFood.itemCategories, itemCategory)
                    .join(itemCategory.category, QCategory.category)
                    .where(QCategory.category.name.eq(category).and(catFood.itemStatus.notIn(ItemStatus.READY)))
                    .fetch();
        }
        return null;
    }

    @Override
    public Page<ItemListDto> findAllCategoryItemByType(String category, String type, Pageable pageable, Boolean isEvent) {
        return selectQFileByType(category, type, pageable, isEvent);
    }

    private BooleanExpression catTowerEventCheck(Boolean isEvent){
        return isEvent == null ? null : catTower.event.eq(isEvent);
    }
    private BooleanExpression catToiletEventCheck(Boolean isEvent){
        return isEvent == null ? null : catToilet.event.eq(isEvent);
    }
    private BooleanExpression catFoodEventCheck(Boolean isEvent){
        return isEvent == null ? null : catFood.event.eq(isEvent);
    }

    private Page<ItemListDto> selectQFileByType(String categoryName, String typeName, Pageable pageable, Boolean isEvent) {
        if (typeName.equals("CatTower")) {
            QueryResults<ItemListDto> result = queryFactory
                    .select(Projections.bean(ItemListDto.class, catTower.id, catTower.thumbnailName, catTower.name,
                            catTower.price, catTower.discount, catTower.eventPrice, catTower.event, catTower.content))
                    .from(catTower)
                    .join(catTower.itemCategories, itemCategory)
                    .join(itemCategory.category, category)
                    .where(category.name.eq(categoryName).and(catTower.itemStatus.notIn(ItemStatus.READY)).and(catTowerEventCheck(isEvent)))
                    .limit(pageable.getPageSize())
                    .offset(pageable.getOffset())
                    .fetchResults();

            return new PageImpl<>(result.getResults(), pageable, result.getTotal());
        } else if (typeName.equals("CatToilet")) {
            QueryResults<ItemListDto> result = queryFactory
                    .select(Projections.bean(ItemListDto.class, catToilet.id, catToilet.thumbnailName, catToilet.name,
                            catToilet.price, catToilet.discount, catToilet.eventPrice, catToilet.event, catToilet.content))
                    .from(catToilet)
                    .join(catToilet.itemCategories, itemCategory)
                    .join(itemCategory.category, category)
                    .where(category.name.eq(categoryName).and(catToilet.itemStatus.notIn(ItemStatus.READY)).and(catToiletEventCheck(isEvent)))
                    .limit(pageable.getPageSize())
                    .offset(pageable.getOffset())
                    .fetchResults();

            return new PageImpl<>(result.getResults(), pageable, result.getTotal());

        } else if (typeName.equals("CatFood")) {
            QueryResults<ItemListDto> result = queryFactory
                    .select(Projections.bean(ItemListDto.class, catFood.id, catFood.thumbnailName, catFood.name,
                            catFood.price, catFood.discount, catFood.eventPrice, catFood.event, catFood.content))
                    .from(catFood)
                    .join(catFood.itemCategories, itemCategory)
                    .join(itemCategory.category, category)
                    .where(category.name.eq(categoryName).and(catFood.itemStatus.notIn(ItemStatus.READY)).and(catFoodEventCheck(isEvent)))
                    .limit(pageable.getPageSize())
                    .offset(pageable.getOffset())
                    .fetchResults();

            return new PageImpl<>(result.getResults(), pageable, result.getTotal());

        }
        return null;
    }
}
