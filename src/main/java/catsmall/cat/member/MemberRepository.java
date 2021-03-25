package catsmall.cat.member;

import catsmall.cat.member.Member;
import catsmall.cat.member.query.MemberQueryRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberQueryRepository {

    Member findMemberByEmail(String email);

    Member findMemberByPhonenum(String phonenum);

    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);
}
