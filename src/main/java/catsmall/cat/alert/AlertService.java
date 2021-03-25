package catsmall.cat.alert;

import catsmall.cat.alert.entities.Alert;
import catsmall.cat.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AlertService {
    private final AlertRepository alertRepository;

    @Transactional
    public List<Alert> findAlert(Member member, boolean isRead){
        List<Alert> alertList = alertRepository.findAllByMemberIdAndIsRead(member.getId(), isRead);
        alertList.forEach(a -> a.setRead(true));
        return alertList;
    }
}
