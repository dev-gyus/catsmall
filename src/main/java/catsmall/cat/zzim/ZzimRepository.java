package catsmall.cat.zzim;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ZzimRepository extends JpaRepository<Zzim, Long> {
    @Query("select z from Zzim z join z.member m join fetch z.itemList il where m.id = :memberId")
    Zzim findByMemberId(@Param("memberId") Long memberId);
}
