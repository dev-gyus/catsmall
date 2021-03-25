package catsmall.cat.interceptor;

import catsmall.cat.member.Member;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class TopnavInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        HttpSession session = request.getSession();
        Member loginInfo = (Member) session.getAttribute("loginInfo");
        if(loginInfo != null){
            request.setAttribute("role", loginInfo.getRole().toString());
        }
        return true;
    }
}
