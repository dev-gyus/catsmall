package catsmall.cat.member;

import catsmall.cat.entity.dto.LoginDto;
import catsmall.cat.entity.item.Item;
import catsmall.cat.member.forgot.EmailOnly;
import catsmall.cat.member.forgot.ForgotEmail;
import catsmall.cat.repository.ItemRepository;
import catsmall.cat.zzim.Zzim;
import lombok.RequiredArgsConstructor;
import org.dom4j.rule.Mode;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final MemberRepository memberRepository;

    /**
     * 로그인
     */
    @GetMapping("/signin")
    public String signin(@CurrentUser Member member ,@ModelAttribute("loginDto")LoginDto loginDto, HttpServletRequest request){
        String prevPage = request.getHeader("Referer");
        if(prevPage != null && !prevPage.equals("http://localhost:8080/members/signin")){
            request.getSession().setAttribute("prevPage", prevPage);
        }
        if(member == null) {
            return "member/signin";
        }
        return "redirect:/";
    }

    @PostMapping("/signin")
    public String signin_do(@Valid @ModelAttribute("loginDto") LoginDto loginDto, BindingResult errors){
        if(errors.hasErrors()){
            return "member/signin";
        }
        return "redirect:/";
    }

    /**
     * 회원가입
     */

    @GetMapping("/signup")
    public String signup(@ModelAttribute("memberDto") MemberDto memberDto){
        return "member/signup";
    }


    @PostMapping("/signup")
    public String signup_do(@Valid @ModelAttribute("memberDto") MemberDto memberDto,
                            BindingResult result, Model model){
        memberService.signup(memberDto, result);
        if(result.hasErrors()){
            return "member/signup";
        }

        model.addAttribute(memberDto);
        return "redirect:/";
    }


    @GetMapping("{token}/email/validate")
    public String email_validate(@PathVariable String token, @RequestParam String email){
        memberService.emailValidate(token, email);
        return "member/email-validation";
    }

    @GetMapping("/mypage")
    public String mypage(@CurrentUser Member member, @ModelAttribute ProfileDto profileDto, Model model){
        model.addAttribute(member);
        return "member/mypage/profile";
    }

    @PostMapping("/mypage")
    public String mypage_do(@CurrentUser Member member, @Valid @ModelAttribute ProfileDto profileDto, BindingResult result,Model model){
        memberService.modifyProfile(member, profileDto, result);
        if(result.hasErrors()){
            model.addAttribute(member);
            model.addAttribute(profileDto);
            return "member/mypage/profile";
        }
        return "redirect:/members/mypage";
    }


    // TODO 오더 메인페이지 제작하고 이상없으면 지울것
    @GetMapping("/orders")
    public String orders(@CurrentUser Member member, Model model){
        model.addAttribute(member);
        return "member/mypage/orders";
    }

    @GetMapping("/address")
    public String address(@CurrentUser Member member, @ModelAttribute AddressDto addressDto,Model model){
        model.addAttribute(member.getAddress());
        return "member/mypage/address";
    }
    @PostMapping("/address")
    public String address_do(@CurrentUser Member member, @ModelAttribute AddressDto addressDto,Model model){
        memberService.modifyAddress(member, addressDto);
        return "redirect:/members/address";
    }

    @GetMapping("/password-repeat")
    public String password_repeat(@ModelAttribute PasswordRepeat passwordRepeat){
        return "member/mypage/password-repeat";
    }
    @PostMapping("/password-repeat")
    public String password_repeat_do(@CurrentUser Member member, @Valid @ModelAttribute PasswordRepeat passwordRepeat, BindingResult result ,Model model){
        memberService.passwordCheck(member, passwordRepeat, result);
        if(result.hasErrors()){
            return "member/mypage/password-repeat";
        }
        model.addAttribute(new ModifyPasswordDto());
        return "member/mypage/modify-password";
    }

    @GetMapping("/modify-password")
    public String modify_password_get(){
        return "redirect:/members/password-repeat";
    }

    @PostMapping("/modify-password")
    public String modify_password(@CurrentUser Member member, @Valid @ModelAttribute ModifyPasswordDto modifyPasswordDto, BindingResult result ,Model model){
        memberService.modifyPassword(member, modifyPasswordDto, result);
        if(result.hasErrors()){
            return "member/mypage/modify-password";
        }
        return "redirect:/members/mypage";
    }

    @GetMapping("/forgot/email")
    public String forgot_email(@CurrentUser Member member ,@ModelAttribute ForgotEmail forgotEmail){
        if(member != null){
            throw new IllegalArgumentException("잘못된 접근입니다.");
        }
        return "member/forgot/email";
    }

    @PostMapping("/forgot/email")
    public String forgot_email_sendMail(@ModelAttribute ForgotEmail forgotEmail, BindingResult result, Model model){
        String mail = memberService.forgotEmailSendMail(forgotEmail, result);
        if(result.hasErrors()){
            return "member/forgot/email";
        }
        String[] split = mail.split("@");
        String convertedId = split[0].substring(0, 3) + "********";
        String convertedMail = convertedId + split[1];
        model.addAttribute("convertedMail",convertedMail);
        return "member/forgot/email-done";
    }

    @GetMapping("/forgot/password")
    public String forgot_password(@ModelAttribute EmailOnly emailOnly){
        return "member/forgot/password-input-email";
    }

    @PostMapping("/forgot/password")
    public String forgot_password_sendEmail(@Valid @ModelAttribute EmailOnly emailOnly, BindingResult result, Model model){
        if(result.hasErrors()){
            model.addAttribute(emailOnly);
            return "member/forgot/password-input-email";
        }
        memberService.forgotPasswordSendEmail(emailOnly, result);
        if(result.hasErrors()){
            model.addAttribute(emailOnly);
            return "member/forgot/password-input-email";
        }
        model.addAttribute("email", emailOnly.getEmail());
        return "member/forgot/password-email-done";
    }

    @GetMapping("/forgot/{token}/password")
    public String forgot_password_form(@PathVariable String token, @RequestParam String email, @ModelAttribute ModifyPasswordDto modifyPasswordDto, Model model){
        memberService.forgotPasswordCheckToken(token, email);
        model.addAttribute("token", token);
        model.addAttribute("email", email);
        return "member/mypage/modify-password";
    }

    @PostMapping("/forgot/{token}/password")
    public String forgot_password_do(@PathVariable String token, @RequestParam String email, @ModelAttribute ModifyPasswordDto modifyPasswordDto,
                               BindingResult result, Model model){
        memberService.forgotPasswordChange(token, email, modifyPasswordDto, result);
        if(result.hasErrors()){
            model.addAttribute("token", token);
            model.addAttribute("email", email);
            return "member/mypage/modify-password";
        }
        return "redirect:/members/signin";
    }

    @GetMapping("/zzim")
    public String zzim(@CurrentUser Member member, Model model){
        member = memberRepository.findZzimItemsFetchByMemberId(member.getId());
        List<Item> zzimItemList = member.getZzim().getItemList();
        model.addAttribute("zzimItemList", zzimItemList);
        return "member/mypage/zzim";
    }
}
