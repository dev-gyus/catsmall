package catsmall.cat.admin.repository.query;

import catsmall.cat.entity.item.QItem;
import catsmall.cat.member.Member;
import catsmall.cat.member.QMember;
import catsmall.cat.zzim.QZzim;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.util.List;

import static catsmall.cat.entity.item.QItem.item;
import static catsmall.cat.member.QMember.member;
import static catsmall.cat.zzim.QZzim.zzim;

public class AdminMemberRepositoryImpl implements AdminMemberQueryRepository{
    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    public AdminMemberRepositoryImpl(EntityManager em) {
        this.em = em;
        queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<Member> findAllByZzimItemId(Long itemId) {
        return queryFactory
                .selectFrom(member)
                .join(member.zzim, zzim)
                .join(zzim.itemList, item)
                .where(item.id.eq(itemId))
                .fetch();
    }
}
