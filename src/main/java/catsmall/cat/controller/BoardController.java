package catsmall.cat.controller;

import catsmall.cat.entity.Category;
import catsmall.cat.entity.dto.item.ItemDto;
import catsmall.cat.entity.dto.ItemTypeDto;
import catsmall.cat.entity.dto.item.ItemListDto;
import catsmall.cat.repository.CategoryRepository;
import catsmall.cat.repository.ItemRepository;
import catsmall.cat.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {
    private final ItemService itemService;
    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;

    @GetMapping(value = {"/",""})
    public String main(@RequestParam("category") String category,
                       @RequestParam("type") String type,
                       Pageable pageable,
                       Model model){

        Page<ItemListDto> result = itemRepository.findAllCategoryItemByType(category, type, pageable, null);
        List<ItemListDto> itemDtos;
        if(!result.isEmpty()) {
            itemDtos = result.getContent();
        }else{
            itemDtos = Collections.EMPTY_LIST;
        }
        List<Category> parentCategory = categoryRepository.findChildrenByParentName(type);
        List<Category> childrenCategories = new ArrayList<>();
        if(!parentCategory.isEmpty()){
            childrenCategories = parentCategory.get(0).getChildren();
        }else{
            childrenCategories = Collections.EMPTY_LIST;
        }

        model.addAttribute("type", type);
        model.addAttribute("category", category);
        model.addAttribute("childrenCategories", childrenCategories);
        model.addAttribute("items", itemDtos);
        return "board/itemlist";
    }
}
