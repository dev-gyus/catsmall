package catsmall.cat.member.query;

import catsmall.cat.member.Member;

public interface MemberQueryRepository {
    Member findCartFetchByMemberId(Long id);
    Member findZzimItemsFetchByMemberId(Long id);
}
