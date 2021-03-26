package catsmall.cat;

import catsmall.cat.config.AppProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class ProfileResponse {
    private final AppProperties appProperties;

    @GetMapping("/profile")
    @ResponseBody
    public String response_profile(){
        return appProperties.getNowProfile();
    }
}
