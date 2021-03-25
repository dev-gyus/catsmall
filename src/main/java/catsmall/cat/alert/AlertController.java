package catsmall.cat.alert;

import catsmall.cat.alert.entities.Alert;
import catsmall.cat.alert.entities.InStock;
import catsmall.cat.member.CurrentUser;
import catsmall.cat.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/alert")
public class AlertController {
    private final AlertRepository alertRepository;
    private final AlertService alertService;

    @GetMapping({"","/"})
    public String alert(@CurrentUser Member member,
                        @RequestParam(name = "read", defaultValue = "false", required = false) boolean isRead,
                        Model model){
        int count = alertRepository.countByMemberIdAndIsRead(member.getId(), false); // 읽지 않은 알람만 카운트해옴
        List<Alert> alertList = alertService.findAlert(member, isRead);
        List<InStock> zzimAlertList = new ArrayList<>();
        alertList.forEach(a -> {
            if(a instanceof InStock){
                zzimAlertList.add((InStock) a);
            }
        });
        alertList.removeAll(zzimAlertList); // 찜목록 알람과 기타 알람과의 중복 제거
        model.addAttribute("alertList", alertList);
        model.addAttribute("zzimAlertList", zzimAlertList);
        model.addAttribute("count",count);
        model.addAttribute("nowPage", isRead);
        return "alert/main";
    }

}
