package catsmall.cat.alert;

import catsmall.cat.admin.repository.AdminMemberRepository;
import catsmall.cat.alert.entities.Alert;
import catsmall.cat.alert.entities.InStock;
import catsmall.cat.entity.item.Item;
import catsmall.cat.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
@Transactional
public class AlertUtils {
    private final AdminMemberRepository adminMemberRepository;
    private final AlertRepository alertRepository;

    public void makeAlert(Member member, AlertKinds alertKinds){
    }

    public void makeInStockAlert(Item item){
        List<Member> findMemberList = adminMemberRepository.findAllByZzimItemId(item.getId());
        for (Member findMember : findMemberList) {
            Alert alert = new InStock(findMember, AlertKinds.INSTOCK, item.getId());
            alert.setSubject(item.getName() + " 상품이 새로 입고됐어요!");
            alert.setContent("알람을 클릭해 따끈따끈한 새제품을 확인해보세요~");
            alertRepository.save(alert);
            findMember.addAlert(alert);
        }
    }
}
