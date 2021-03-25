package catsmall.cat.repository.query;

import catsmall.cat.entity.ItemCategory;
import catsmall.cat.entity.QCategory;
import catsmall.cat.entity.QItemCategory;
import catsmall.cat.entity.dto.item.ItemListDto;
import catsmall.cat.entity.item.*;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;

import static catsmall.cat.entity.QCategory.category;
import static catsmall.cat.entity.QItemCategory.itemCategory;
import static catsmall.cat.entity.item.QCatFood.catFood;
import static catsmall.cat.entity.item.QCatToilet.catToilet;
import static catsmall.cat.entity.item.QCatTower.catTower;
import static catsmall.cat.entity.item.QItem.item;

public class ItemCategoryRepositoryImpl implements ItemCategoryQueryRepository{
    private EntityManager em;
    private JPAQueryFactory queryFactory;

    public ItemCategoryRepositoryImpl(EntityManager em) {
        this.em = em;
        queryFactory = new JPAQueryFactory(em);
    }


    @Override
    public Page<ItemCategory> findItemList(Pageable pageable, String category){
        QCategory temp = new QCategory("temp");
        QueryResults<ItemCategory> findList = queryFactory
                .selectFrom(itemCategory)
                .join(itemCategory.item, item).fetchJoin()
                .join(itemCategory.category, temp).fetchJoin()
                .where(item.itemStatus.eq(ItemStatus.NOWSALE).and(temp.name.eq(category)))
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetchResults();

        return new PageImpl<>(findList.getResults(), pageable, findList.getTotal());
    }

    @Override
    public Page<ItemListDto> findEventItems(Pageable pageable, String categoryName, String typeName){
        if(typeName.equals("CatFood")){
            QueryResults<ItemListDto> result = queryFactory
                    .select(Projections.bean(ItemListDto.class, catFood.id, catFood.thumbnailName, catFood.name,
                            catFood.price, catFood.discount, catFood.eventPrice, catFood.event, catFood.content))
                    .from(catFood)
                    .join(catFood.itemCategories, itemCategory)
                    .join(itemCategory.category, category)
                    .where(catFood.event.isTrue().and(catFood.itemStatus.eq(ItemStatus.NOWSALE))
                            .and(catFood.type.eq(typeName)).and(itemCategory.category.name.eq(categoryName)))
                    .limit(pageable.getPageSize())
                    .offset(pageable.getOffset())
                    .fetchResults();

            return new PageImpl<>(result.getResults(), pageable, result.getTotal());
        }else if(typeName.equals("CatToilet")){
            QueryResults<ItemListDto> result = queryFactory
                    .select(Projections.bean(ItemListDto.class, catToilet.id, catToilet.thumbnailName, catToilet.name,
                            catToilet.price, catToilet.discount, catToilet.eventPrice, catToilet.event, catToilet.content))
                    .from(catToilet)
                    .join(catToilet.itemCategories, itemCategory)
                    .join(itemCategory.category, category)
                    .where(catToilet.event.isTrue().and(catToilet.itemStatus.eq(ItemStatus.NOWSALE))
                            .and(catToilet.type.eq(typeName)).and(itemCategory.category.name.eq(categoryName)))
                    .limit(pageable.getPageSize())
                    .offset(pageable.getOffset())
                    .fetchResults();

            return new PageImpl<>(result.getResults(), pageable, result.getTotal());
        }else if(typeName.equals("CatTower")){
            QueryResults<ItemListDto> result = queryFactory
                    .select(Projections.bean(ItemListDto.class, catTower.id, catTower.thumbnailName, catTower.name,
                            catTower.price, catTower.discount, catTower.eventPrice, catTower.event, catTower.content))
                    .from(catTower)
                    .join(catTower.itemCategories, itemCategory).fetchJoin()
                    .join(itemCategory.category, category).fetchJoin()
                    .where(catTower.event.isTrue().and(catTower.itemStatus.eq(ItemStatus.NOWSALE))
                            .and(catTower.type.eq(typeName)).and(itemCategory.category.name.eq(categoryName)))
                    .limit(pageable.getPageSize())
                    .offset(pageable.getOffset())
                    .fetchResults();
            return new PageImpl<>(result.getResults(), pageable, result.getTotal());
        }else{
            return null;
        }
    }
}
