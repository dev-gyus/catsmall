package catsmall.cat.member.query;

import catsmall.cat.cart.QCart;
import catsmall.cat.entity.item.QItem;
import catsmall.cat.member.Member;
import catsmall.cat.member.QMember;
import catsmall.cat.zzim.QZzim;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;

import static catsmall.cat.cart.QCart.cart;
import static catsmall.cat.entity.item.QItem.item;
import static catsmall.cat.member.QMember.member;
import static catsmall.cat.zzim.QZzim.zzim;

public class MemberRepositoryImpl implements MemberQueryRepository{
    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    public MemberRepositoryImpl(EntityManager em){
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Member findCartFetchByMemberId(Long id) {
        return queryFactory
                .selectFrom(member)
                .leftJoin(member.cart, cart).fetchJoin()
                .where(member.id.eq(id))
                .fetchOne();
    }

    public Member findZzimItemsFetchByMemberId(Long id){
        return queryFactory
                .selectFrom(member)
                .distinct()
                .leftJoin(member.zzim, zzim).fetchJoin()
                .leftJoin(zzim.itemList, item).fetchJoin()
                .where(member.id.eq(id))
                .fetchOne();

    }
}
