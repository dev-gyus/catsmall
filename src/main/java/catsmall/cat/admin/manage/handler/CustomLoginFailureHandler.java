package catsmall.cat.admin.manage.handler;

import catsmall.cat.member.Member;
import catsmall.cat.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationProvider;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomLoginFailureHandler implements AuthenticationFailureHandler {
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        if(exception instanceof BadCredentialsException){
            String username = request.getParameter("username");
            Member findMember = memberRepository.findMemberByEmail(username);
            findMember.setLoginFailedCount(findMember.getLoginFailedCount() + 1);

            if(findMember.getLoginFailedCount() > 4){
                request.setAttribute("errorTitle", "계정 잠김");
                request.setAttribute("errorMsg", "계정이 잠겼습니다. 비밀번호 찾기를 통해 계정잠금을 해제하세요.");
                request.setAttribute("isLocked", true);
                RequestDispatcher requestDispatcher = request.getRequestDispatcher("/members/signin?error=true");
                requestDispatcher.forward(request, response);
                findMember.setLocked(true);
                return;
            }
        }
        request.setAttribute("errorTitle", "로그인 실패");
        request.setAttribute("errorMsg", "아이디/비밀번호를 확인해주세요.");
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("/members/signin?error=true");
        requestDispatcher.forward(request, response);
    }
}
