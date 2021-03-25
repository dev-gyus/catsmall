package catsmall.cat.controller;

import catsmall.cat.entity.dto.ItemTypeDto;
import catsmall.cat.entity.item.Item;
import catsmall.cat.member.CurrentUser;
import catsmall.cat.member.Member;
import catsmall.cat.member.MemberRepository;
import catsmall.cat.repository.ItemRepository;
import catsmall.cat.review.Review;
import catsmall.cat.review.ReviewRepository;
import catsmall.cat.service.ItemService;
import catsmall.cat.zzim.Zzim;
import catsmall.cat.zzim.ZzimRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/item")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;
    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;

    @GetMapping(value = "/main")
    public String main(@CurrentUser Member member,
                       @RequestParam("item") Long itemId,
                       @PageableDefault(value = 10, size = 1, page = 0) Pageable pageable,
                       Model model){
        Item item = itemService.findById(itemId);
        Page<Review> reviewList = reviewRepository.findReviewsByItemId(itemId, pageable);
        boolean isZzimed = false;
        int nowPage = reviewList.getNumber()/10;
        int totalPages = reviewList.getTotalPages();
        int startPage = nowPage * 10;
        int endPage;
        if(totalPages/10 - nowPage < 1){
            endPage = totalPages - 1;
        }else{
            endPage = startPage + 9;
        }


        if(member != null) {
            Member findMember = memberRepository.findZzimItemsFetchByMemberId(member.getId());
            isZzimed = findMember.getZzim().getItemList().contains(item);
            model.addAttribute("memberId", member.getId());
        }
        model.addAttribute("item", item);
        model.addAttribute("reviewList", reviewList);
        model.addAttribute("isZzimed", isZzimed);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage",endPage);
        return "board/item_detail";
    }
}
