package catsmall.cat.zzim;

import catsmall.cat.entity.item.Item;
import catsmall.cat.member.Member;
import catsmall.cat.member.MemberRepository;
import catsmall.cat.member.MemberService;
import catsmall.cat.member.login.MemberLogin;
import catsmall.cat.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ZzimService {
    private final ZzimRepository zzimRepository;
    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final MemberService memberService;

    @Transactional
    public void addZzim(Member member, Long itemId) {
        Member findMember = memberRepository.findZzimItemsFetchByMemberId(member.getId());
        Zzim memberZzim = findMember.getZzim();
        Item findItem = itemRepository.findById(itemId).orElseThrow();

        Authentication originAuth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = new MemberLogin(findMember);
        Authentication token = new UsernamePasswordAuthenticationToken(userDetails, originAuth.getCredentials(), originAuth.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(originAuth);

        if(memberZzim == null){
            Zzim zzim = new Zzim(findMember, findItem);
        }
        else if(memberZzim != null && !memberZzim.getItemList().contains(findItem)) {
            memberZzim.addItem(findItem);
        }
    }
    @Transactional
    public void deleteZzimItem(Member member, Long itemId){
        Member findMember = memberRepository.findZzimItemsFetchByMemberId(member.getId());
        Item item = itemRepository.findById(itemId).orElseThrow();
        Zzim memberZzim = findMember.getZzim();
        if(memberZzim.getItemList().contains(item)){
            memberZzim.getItemList().remove(item);
        }
    }
}
