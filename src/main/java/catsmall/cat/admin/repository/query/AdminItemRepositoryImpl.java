package catsmall.cat.admin.repository.query;

import catsmall.cat.entity.ItemCategory;
import catsmall.cat.entity.QCategory;
import catsmall.cat.entity.item.Item;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;

import java.util.List;

import static catsmall.cat.entity.QCategory.category;
import static catsmall.cat.entity.QItemCategory.itemCategory;
import static catsmall.cat.entity.item.QItem.item;

public class AdminItemRepositoryImpl implements AdminItemQueryRepository {
    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    public AdminItemRepositoryImpl(EntityManager em){
        this.em = em;
        queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<ItemCategory> findAllCategoryAndTypes(){
        List<ItemCategory> fetch = queryFactory
                .select(itemCategory)
                .distinct()
                .from(itemCategory)
                .join(itemCategory.category, category).fetchJoin()
                .leftJoin(itemCategory.item, item).fetchJoin()
                .fetch();
        return fetch;
    }

    @Override
    public List<ItemCategory> findTypesByCategoryName(String category){
        return queryFactory
                .selectFrom(itemCategory)
                .distinct()
                .leftJoin(itemCategory.item, item).fetchJoin()
                .join(itemCategory.category, QCategory.category).fetchJoin()
                .where(itemCategory.category.name.eq(category))
                .fetch();
    }

    @Override
    public Item findCategoryFetchByItemId(Long itemId){
        return queryFactory
                .selectFrom(item)
                .join(item.itemCategories, itemCategory).fetchJoin()
                .where(item.id.eq(itemId))
                .fetchOne();
    }

    @Override
    public Page<ItemCategory> findItemPagingFetchByCategory(String categoryName, Pageable pageable){
        QueryResults<ItemCategory> findList = queryFactory
                .selectFrom(itemCategory)
                .join(itemCategory.category, category).fetchJoin()
                .join(itemCategory.item, item).fetchJoin()
                .where(category.name.eq(categoryName))
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetchResults();
        return new PageImpl<>(findList.getResults(), pageable, findList.getTotal());
    }
}
