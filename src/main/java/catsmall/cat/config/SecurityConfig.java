package catsmall.cat.config;

import catsmall.cat.admin.manage.handler.CustomLoginFailureHandler;
import catsmall.cat.admin.manage.handler.CustomLoginSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.access.expression.WebExpressionVoter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.session.HttpSessionEventPublisher;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final DataSource dataSource;
    private final CustomLoginSuccessHandler customLoginSuccessHandler;
    private final CustomLoginFailureHandler customLoginFailureHandler;

    public AccessDecisionManager accessDecisionManager(){
        RoleHierarchyImpl hierarchy = new RoleHierarchyImpl();
        hierarchy.setHierarchy("ROLE_ADMIN > ROLE_USER");

        DefaultWebSecurityExpressionHandler handler = new DefaultWebSecurityExpressionHandler();
        handler.setRoleHierarchy(hierarchy);

        WebExpressionVoter voter = new WebExpressionVoter();
        voter.setExpressionHandler(handler);

        List<AccessDecisionVoter<? extends Object>> voters = Arrays.asList(voter);
        return new AffirmativeBased(voters);
    }


    // Form인증 기반 설정 메소드
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .mvcMatchers("/", "/api/**","/board", "/item/**", "/search/**", "/profile",
                        "/members/signin", "/members/signup", "/members/logout", "/members/forgot/**").permitAll()
                .mvcMatchers("/admin/**","/admin").hasRole("ADMIN")
                .anyRequest().authenticated();

        http
                .formLogin()
                .loginPage("/members/signin")
                .loginProcessingUrl("/members/signin")
                .successHandler(customLoginSuccessHandler)
                .failureHandler(customLoginFailureHandler);

        http
                .logout()
                .logoutSuccessUrl("/");

        http
                .rememberMe()
                .key("token")
                .tokenRepository(jdbcTokenRepository())
                .rememberMeParameter("duringLogin")
                .rememberMeCookieName("duringLogin")
                .tokenValiditySeconds(60 * 60 * 24 * 14); // 쿠키 2주간 지속


        http
                .httpBasic();

        http
                .sessionManagement()
                .sessionFixation()
                .changeSessionId()
                .maximumSessions(1)
                .maxSessionsPreventsLogin(false)
                .sessionRegistry(sessionRegistry()) // session clustering에서 중복로그인 방지 및 실시간유저밴을위함
                .expiredUrl("/members/signin");
    }
    // WAS에서 세션관리를 위한 레지스트리
    @Bean
    public SessionRegistry sessionRegistry(){
        return new SessionRegistryImpl();
    }
    // SessionRegistry에서 추가, 삭제한 세션은 WAS단까진 적용되지만, spring security까진 적용이 안됨.
    // 이는 서블릿 리스너를 빈으로 등록해주면 적용받을수있음
    @Bean
    public static ServletListenerRegistrationBean<HttpSessionEventPublisher> httpSessionEventPublisher() {
        return new ServletListenerRegistrationBean<HttpSessionEventPublisher>(new HttpSessionEventPublisher());
    }

    @Bean
    public PersistentTokenRepository jdbcTokenRepository(){
        JdbcTokenRepositoryImpl jdb = new JdbcTokenRepositoryImpl();
        jdb.setDataSource(dataSource);
        return jdb;
    }


    // static 주소에 대한 security ignore설정
    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations())
                .mvcMatchers("/node_modules/**")
                .mvcMatchers("/upload/**");
    }
}
