package catsmall.cat.alert;

import catsmall.cat.alert.entities.Alert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface AlertRepository extends JpaRepository<Alert, Long> {
    List<Alert> findAllByMemberIdAndIsRead(Long memberId, boolean isRead);

    int countByMemberIdAndIsRead(Long id, boolean isRead);
}
