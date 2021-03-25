package catsmall.cat.search;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/search")
public class SearchController {
    private final SearchService searchService;
    private final SearchRepository searchRepository;

    @GetMapping({"/",""})
    public String search(@RequestParam String keyword, @PageableDefault(size = 6, page = 0) Pageable pageable, Model model){
        // TODO 검색결과에 넣을거 추가하면 따로 쿼리메소드 만들것 (현재는 상품만 검색해옴)
        Page<SearchDto> searchItems = searchRepository.findAllItemPagingByKeyword(keyword, pageable);
        model.addAttribute("searchItems", searchItems);
        model.addAttribute("keyword", keyword);
        return "search/main";
    }
    @GetMapping("/item")
    public String search_item(@RequestParam String keyword, @PageableDefault(size = 20, page = 0) Pageable pageable, Model model){
        Page<SearchDto> searchItems = searchRepository.findAllItemPagingByKeyword(keyword, pageable);
        model.addAttribute("keyword", keyword);
        model.addAttribute("searchItems", searchItems);
        return "search/item";
    }
}
