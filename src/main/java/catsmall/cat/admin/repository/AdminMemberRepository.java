package catsmall.cat.admin.repository;

import catsmall.cat.admin.repository.query.AdminMemberQueryRepository;
import catsmall.cat.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminMemberRepository extends JpaRepository<Member, Long>, AdminMemberQueryRepository {
}
