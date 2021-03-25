package catsmall.cat.repository.query;

import catsmall.cat.entity.Category;
import catsmall.cat.entity.QCategory;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.util.List;

import static catsmall.cat.entity.QCategory.category;

public class CategoryRepositoryImpl implements CategoryQueryRepository{
    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    public CategoryRepositoryImpl(EntityManager em) {
        this.em = em;
        queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<Category> findChildrenByParentName(String parentName){
        QCategory temp = new QCategory("temp");
        return queryFactory
                .selectFrom(category)
                .join(category.children, temp).fetchJoin()
                .where(category.name.eq(parentName))
                .fetch();
    }
}
