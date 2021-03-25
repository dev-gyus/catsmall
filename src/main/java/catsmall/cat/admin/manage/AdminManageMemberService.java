package catsmall.cat.admin.manage;

import catsmall.cat.admin.BlockUserDto;
import catsmall.cat.member.Member;
import catsmall.cat.member.MemberRepository;
import catsmall.cat.member.login.MemberLogin;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AdminManageMemberService {
    private final MemberRepository memberRepository;
    private final SessionRegistry sessionRegistry;

    @Transactional
    public void blockUser(BlockUserDto blockUserDto) {
        Member member = memberRepository.findMemberByEmail(blockUserDto.getEmail());
        List<MemberLogin> collect = sessionRegistry.getAllPrincipals()
                .stream().map(o -> (MemberLogin) o).collect(Collectors.toList());
        for (MemberLogin memberLogin : collect) {
            Member target = memberLogin.getMember();
            if(target.getEmail() == member.getEmail()){
                List<SessionInformation> allSessions = sessionRegistry.getAllSessions(memberLogin, false);
                for (SessionInformation allSession : allSessions) {
                   allSession.expireNow();
                }
            }
        }
        member.setBlocked(true);
    }

    @Transactional
    public void unBlockUser(BlockUserDto blockUserDto) {
        Member member = memberRepository.findMemberByEmail(blockUserDto.getEmail());
        member.setBlocked(false);
    }
}
