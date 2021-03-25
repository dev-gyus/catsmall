package catsmall.cat.admin.manage.handler;

import catsmall.cat.member.Member;
import catsmall.cat.member.MemberRepository;
import catsmall.cat.member.login.MemberLogin;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {
    private final RequestCache requestCache = new HttpSessionRequestCache();
    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        MemberLogin principal = (MemberLogin) authentication.getPrincipal();
        Member findMember = memberRepository.findMemberByEmail(principal.getMember().getEmail());
        if(findMember.isBlocked()){
            throw new LockedException("계정이 정지되었습니다. 관리자에게 문의하세요");
        }else if(findMember.isLocked()){
            throw new LockedException("계정이 잠겼습니다. 아이디/비밀번호 찾기를 이용하여 잠금해제 하세요");
        }

        SavedRequest savedRequest = requestCache.getRequest(request, response);
        String prevPage = (String) request.getSession().getAttribute("prevPage");
        findMember.setLoginFailedCount(0);

        if(savedRequest == null && prevPage != null){   // 인증 필요없는곳에서 직접 로그인에 접근한경우
            request.getSession().removeAttribute("prevPage"); // 사용 후 해당 세션 데이터를 지워서 메모리 누수 방지
            redirectStrategy.sendRedirect(request, response, prevPage);
        }else if(savedRequest != null && prevPage != null){     // 인증 필요한리소스 요청을 해서 로그인페이지로 넘어간경우
            redirectStrategy.sendRedirect(request, response, savedRequest.getRedirectUrl());
        }else {     // 메인페이지에서 로그인 접근한 경우
            redirectStrategy.sendRedirect(request, response, "/");
        }
    }
}
