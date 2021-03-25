package catsmall.cat.admin.repository.query;

import catsmall.cat.entity.Category;
import catsmall.cat.entity.QCategory;
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

public class AdminCategoryRepositoryImpl implements AdminCategoryQueryRepository{
    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    public AdminCategoryRepositoryImpl(EntityManager em) {
        this.em = em;
        queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<Category> findTopCategories(){
        QCategory categorySub = new QCategory("parent");
        return queryFactory
                .selectFrom(category)
                .distinct()
                .where(category.parent.isNull())
                .fetch();
    }

    @Override
    public Category findChildrenFetchByParentName(String parentName){
        QCategory cloneCategory = new QCategory("clone");
        return queryFactory
                .selectFrom(category)
                .distinct()
                .leftJoin(category.children, cloneCategory).fetchJoin()
                .where(category.name.eq(parentName))
                .fetchOne();
    }

    @Override
    public Category findChildWithItemCatByChildName(String childName){
        return queryFactory
                .selectFrom(category)
                .distinct()
                .leftJoin(category.itemCategory, itemCategory)
                .where(category.name.eq(childName))
                .fetchOne();
    }
    @Override
    public Page<Category> findChildItemsFetchByParentName(String categoryName, Pageable pageable){
        QueryResults<Category> result = queryFactory
                .selectFrom(category)
                .distinct()
                .leftJoin(category.itemCategory, itemCategory).fetchJoin()
                .join(itemCategory.item, item).fetchJoin()
                .where(category.name.eq(categoryName))
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetchResults();

        List<Category> categoryList = result.getResults();
        long total = result.getTotal();
        return new PageImpl<>(categoryList, pageable, total);
    }

    @Override
    public List<Category> findAllIgnoreTopCategories(){
        return queryFactory
                .selectFrom(category)
                .where(category.parent.isNotNull())
                .fetch();
    }
}
