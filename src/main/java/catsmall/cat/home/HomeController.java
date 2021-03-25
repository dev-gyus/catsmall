package catsmall.cat.home;

import catsmall.cat.entity.ItemCategory;
import catsmall.cat.entity.dto.item.ItemListDto;
import catsmall.cat.repository.ItemCategoryRepository;
import catsmall.cat.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final ItemCategoryRepository itemCategoryRepository;
    private final ItemRepository itemRepository;
    @GetMapping("/")
    public String home(Model model, @RequestParam(name = "category", defaultValue = "kitten") String category,
                       @RequestParam(name = "type", defaultValue = "CatFood") String type,
                       @PageableDefault(size = 9, page = 0) Pageable pageable){
        Page<ItemListDto> eventItems = itemRepository.findAllCategoryItemByType(category, type, pageable,true);
        model.addAttribute("category", category);
        model.addAttribute("type", type);
        model.addAttribute("eventItems", eventItems.getContent());
        return "index";
    }
}
