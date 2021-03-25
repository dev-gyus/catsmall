package catsmall.cat.admin.repository.query;

import catsmall.cat.member.Member;

import java.util.List;

public interface AdminMemberQueryRepository {
    List<Member> findAllByZzimItemId(Long itemId);
}
