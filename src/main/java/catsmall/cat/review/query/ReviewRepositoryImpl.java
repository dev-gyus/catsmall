package catsmall.cat.review.query;

import catsmall.cat.entity.item.QItem;
import catsmall.cat.member.QMember;
import catsmall.cat.review.QReview;
import catsmall.cat.review.Review;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import java.util.List;

import static catsmall.cat.entity.item.QItem.item;
import static catsmall.cat.member.QMember.member;
import static catsmall.cat.review.QReview.review;

public class ReviewRepositoryImpl implements ReviewQueryRepository{
    private EntityManager em;
    private JPAQueryFactory queryFactory;

    public ReviewRepositoryImpl(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<Review> findReviewsByItemId(Long itemId, Pageable pageable) {
        QueryResults<Review> result = queryFactory
                .selectFrom(review)
                .join(review.item, item)
                .join(review.member, member).fetchJoin()
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .where(item.id.eq(itemId))
                .fetchResults();

        return new PageImpl<>(result.getResults(), pageable, result.getTotal());
    }
}
