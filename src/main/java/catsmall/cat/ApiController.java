package catsmall.cat;

import catsmall.cat.entity.ItemCategory;
import catsmall.cat.entity.dto.item.ItemListDto;
import catsmall.cat.repository.ItemCategoryRepository;
import catsmall.cat.repository.ItemRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ApiController {
    private final ItemCategoryRepository itemCategoryRepository;
    private final ItemRepository itemRepository;

    @GetMapping("/api/find-eventItem-list")
    @ResponseBody
    public ItemDtoListWrapper find_eventItemList(@PageableDefault(size = 9, page = 0) Pageable pageable, @RequestParam String category, @RequestParam String type){
        // TODO 메인페이지도 카테고리따라서 행사상품 다르게 보이도록 설정
        Page<ItemListDto> eventItems = itemRepository.findAllCategoryItemByType(category, type, pageable, true);
        ItemDtoListWrapper itemDtoListWrapper = new ItemDtoListWrapper();
        itemDtoListWrapper.setItemlistDtoList(eventItems);
        return itemDtoListWrapper;
    }
    @GetMapping("/api/find-item-list")
    @ResponseBody
    public ItemDtoListWrapper find_itemList(Pageable pageable, @RequestParam String category){
        Page<ItemCategory> itemList = itemCategoryRepository.findItemList(pageable, category);
        Page<ItemListDto> result = itemList.map(ic -> new ItemListDto(ic.getItem().getId(), ic.getItem().getThumbnailName(),
                ic.getItem().getName(), ic.getItem().getPrice(), ic.getItem().getDiscount(),
                ic.getItem().getEventPrice(), ic.getItem().isEvent()));
        ItemDtoListWrapper itemDtoListWrapper = new ItemDtoListWrapper();
        itemDtoListWrapper.setItemlistDtoList(result);
        return itemDtoListWrapper;
    }

    @Data
    static class ItemDtoListWrapper{
        private Page<ItemListDto> itemlistDtoList;
    }
}
