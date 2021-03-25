package catsmall.cat.member;

import catsmall.cat.cart.Cart;
import catsmall.cat.config.AppProperties;
import catsmall.cat.entity.item.Item;
import catsmall.cat.mail.EmailMessage;
import catsmall.cat.mail.MyMailSender;
import catsmall.cat.member.forgot.EmailOnly;
import catsmall.cat.member.forgot.ForgotEmail;
import catsmall.cat.member.login.MemberLogin;
import catsmall.cat.zzim.Zzim;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService implements UserDetailsService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;
    private final MyMailSender myMailSender;
    private final AppProperties appProperties;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findMemberByEmail(email);
        if (member == null) {
            throw new UsernameNotFoundException(email);
        }

        return new MemberLogin(member);
    }

    @Transactional
    public void signup(MemberDto memberDto, BindingResult result) {
        duplicationCheck(memberDto.getEmail(), memberDto.getNickname(), result);
        if (result.hasErrors()) return;
        Member member = Member.getMember(memberDto);
        member.setPassword(passwordEncoder.encode(member.getPassword()));
        member.setEmailToken(UUID.randomUUID().toString());
        Zzim zzim = new Zzim(member);
        Cart cart = new Cart(member);

        Context context = new Context();
        context.setVariable("nickname", member.getNickname());
        context.setVariable("token", member.getEmailToken());
        context.setVariable("email", member.getEmail());
        context.setVariable("host", appProperties.getHost());
        String message = templateEngine.process("mail", context);

        EmailMessage emailMessage = new EmailMessage();
        emailMessage.setTo(member.getEmail());
        emailMessage.setSubject("Cat's Mall 이메일 인증 메일입니다.");
        emailMessage.setMessage(message);

        myMailSender.sendMail(emailMessage);

        memberRepository.save(member);
    }


    @Transactional
    public void modifyAddress(Member member, AddressDto addressDto) {
        Member findMember = (Member) memberRepository.findById(member.getId()).get();
        Address address = new Address(addressDto.getPostcode(), addressDto.getRoadAddress(), addressDto.getJibunAddress(), addressDto.getDetailAddress(), addressDto.getExtraAddress());
        findMember.setAddress(address);
        updateAuthentication(findMember);
    }


    /**
     * 프로필 수정
     */
    @Transactional
    public void modifyProfile(Member member, ProfileDto profileDto, BindingResult result) {
        if (!member.getNickname().equals(profileDto.getNickname()))
            duplicationCheck(null, profileDto.getNickname(), result);

        if (result.hasErrors()) return;
        Member findMember = memberRepository.findMemberByEmail(member.getEmail());
        findMember.setNickname(profileDto.getNickname());
        findMember.setPhonenum(profileDto.getPhonenum());
        findMember.setName(profileDto.getName());
        findMember.setProfileImage(profileDto.getProfileImage());
        updateAuthentication(findMember);
    }

    /**
     * 비밀번호 변경
     */
    @Transactional
    public void modifyPassword(Member member, ModifyPasswordDto modifyPasswordDto, BindingResult result) {
        if (!modifyPasswordDto.getPassword().equals(modifyPasswordDto.getPasswordRepeat())) {
            result.rejectValue("passwordRepeat", "not.equal", "동일한 비밀번호를 입력해주세요.");
            return;
        }
        Member findMember = memberRepository.findMemberByEmail(member.getEmail());
        String inputPassword = passwordEncoder.encode(modifyPasswordDto.getPassword());
        findMember.setPassword(inputPassword);
        updateAuthentication(member);
    }

    /**
     * 비밀번호 변경 전 현재 비밀번호 일치 확인
     */
    public void passwordCheck(Member member, PasswordRepeat passwordRepeat, BindingResult result) {
        if (!passwordEncoder.matches(passwordRepeat.getPassword(), member.getPassword())) {
            result.rejectValue("password", null, "비밀번호가 일치하지 않습니다.");
        }
    }

    public String forgotEmailSendMail(ForgotEmail forgotEmail, BindingResult result) {
        Member findMember = memberRepository.findMemberByPhonenum(forgotEmail.getPhonenum());
        if (findMember == null || !forgotEmail.getName().equals(findMember.getName())) {
            result.reject(null, "계정 조회 실패");
            return null;
        }
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setText(findMember.getNickname() + " 님의 이메일 주소는 = " + findMember.getEmail() + " 입니다.");
        javaMailSender.send(mailMessage);
        return findMember.getEmail();
    }

    public void forgotPasswordCheckToken(String token, String email) {
        Member member = memberRepository.findMemberByEmail(email);
        parameterValidationCheck(token, member);
    }

    @Transactional
    public void forgotPasswordChange(String token, String email, ModifyPasswordDto modifyPasswordDto, BindingResult result) {
        if (!modifyPasswordDto.getPassword().equals(modifyPasswordDto.getPasswordRepeat())) {
            result.rejectValue("passwordRepeat", null, "동일한 비밀번호를 입력해주세요.");
            return;
        }
        Member member = memberRepository.findMemberByEmail(email);
        parameterValidationCheck(token, member);
        member.setPassword(passwordEncoder.encode(modifyPasswordDto.getPassword()));
    }

    public void forgotPasswordSendEmail(EmailOnly emailOnly, BindingResult result) {
        Member member = memberRepository.findMemberByEmail(emailOnly.getEmail());
        if (member == null) {
            result.rejectValue("email", null, "계정이 존재하지 않습니다.");
            return;
        }
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setText(member.getEmail() + " 계정의 비밀번호를 리셋하시려면 다음 링크를 클릭해주세요. " +
                "/members/forgot/" + member.getEmailToken() + "/password?email=" + member.getEmail());
        javaMailSender.send(mailMessage);
    }


    private void parameterValidationCheck(String token, Member member) {
        if (member == null) {
            throw new IllegalArgumentException("잘못된 요청입니다.");
        }
        if (!member.getEmailToken().equals(token)) {
            throw new IllegalArgumentException("잘못된 요청입니다.");
        }
    }

    private void duplicationCheck(String email, String nickname, BindingResult result) {
        if (email != null && memberRepository.existsByEmail(email)) {
            result.rejectValue("email", null, "중복된 이메일 입니다.");
            if (memberRepository.existsByNickname(nickname)) {
                result.rejectValue("nickname", null, "중복된 닉네임 입니다.");
                return;
            }
            return;
        }
        if (nickname != null && memberRepository.existsByNickname(nickname)) {
            result.rejectValue("nickname", null, "중복된 닉네임 입니다.");
            return;
        }
    }

    /**
     * 회원 정보 변경 후 SecurityContext 내 사용자 신규 업데이트목적 비즈니스 메소드
     */
    private void updateAuthentication(Member member) {
        Authentication currentAuth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails newPrincipal = new MemberLogin(member);
        UsernamePasswordAuthenticationToken newAuth = new UsernamePasswordAuthenticationToken(newPrincipal, currentAuth.getCredentials(), currentAuth.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(newAuth);
    }

    @Transactional
    public void emailValidate(String token, String email) {
        Member findMember = memberRepository.findMemberByEmail(email);
        if(!findMember.getEmailToken().equals(token)){
            throw new IllegalArgumentException("유효한 요청이 아닙니다.");
        }
        findMember.setEmailChecked(true);
    }
}
