package catsmall.cat.zzim;

import catsmall.cat.member.CurrentUser;
import catsmall.cat.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequiredArgsConstructor
@RequestMapping("/zzim")
public class ZzimController {
    private final ZzimService zzimService;

    @PostMapping("/{itemId}/add")
    public String zzim_add(@CurrentUser Member member, @PathVariable Long itemId){
        zzimService.addZzim(member, itemId);
        return "redirect:/item/main?item=" + itemId;
    }
    @PostMapping("/{itemId}/delete")
    public String zzim_delete(@CurrentUser Member member, @PathVariable Long itemId, HttpServletRequest request){
        zzimService.deleteZzimItem(member, itemId);
        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }
}
