package catsmall.cat.admin.controller;

import catsmall.cat.admin.repository.AdminMemberRepository;
import catsmall.cat.admin.manage.dto.MemberManageDto;
import catsmall.cat.admin.manage.dto.ModifyItemDto;
import catsmall.cat.admin.repository.AdminCategoryRepository;
import catsmall.cat.admin.repository.AdminItemRepository;
import catsmall.cat.admin.service.AdminItemService;
import catsmall.cat.entity.Category;
import catsmall.cat.entity.item.Item;
import catsmall.cat.member.Member;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/manage")
@RequiredArgsConstructor
public class AdminManageController {
    private final AdminMemberRepository adminManageMemberRepository;
    private final ModelMapper modelMapper;
    private final AdminCategoryRepository categoryRepository;
    private final AdminItemRepository adminItemRepository;
    private final AdminItemService adminItemService;

    @GetMapping("/member")
    public String manageType(Model model){
        List<Member> members = adminManageMemberRepository.findAll();
        List<MemberManageDto> collect = members.stream().map(m -> modelMapper.map(m, MemberManageDto.class)).collect(Collectors.toList());
        model.addAttribute("memberList", collect);
        return "admin/manage/member";
    }

    @GetMapping("/item")
    public String manage_item(Model model){
        List<Category> categories = categoryRepository.findAllIgnoreTopCategories();
        model.addAttribute("categories", categories);
        return "admin/item/item-list";
    }

    @GetMapping("/item/{itemId}/modify")
    public String item_modify(@PathVariable Long itemId, Model model){
        // TODO 아이템 <-> 카테고리 1:1매칭하는거 고려해볼것. 지금상황으론 카테고리수정에 에로사항이 많음
        Item item = adminItemRepository.findCategoryFetchByItemId(itemId);
        ModifyItemDto modifyItemDto = modelMapper.map(item, ModifyItemDto.class);

        model.addAttribute(modifyItemDto);
        return "admin/item/modify_item";
    }

    @PostMapping("/item/{itemId}/modify")
    public String item_modify_do(@PathVariable Long itemId, @Valid @ModelAttribute ModifyItemDto modifyItemDto,
                                 BindingResult result, Model model){
        Item findItem = adminItemRepository.findCategoryFetchByItemId(itemId);
        if(modifyItemDto.isEvent() && (modifyItemDto.getDiscount() <= 0 || modifyItemDto.getDiscount() > 100)){
            result.rejectValue("discount", null, "0보다 큰 값 혹은 100이하의 값을 입력해주세요.");
        }
        if(result.hasErrors()){
            modelMapper.map(findItem, modifyItemDto);
            return "admin/item/modify_item";
        }
        adminItemService.modifyItem(findItem, modifyItemDto, result);
        return "redirect:/admin/manage/item";
    }

    @PostMapping("/item/{itemId}/delete")
    public String item_delete_do(@PathVariable Long itemId){
        adminItemService.deleteItem(itemId);
        return "redirect:/admin/manage/item";
    }

    @PostMapping("/item/{itemId}/change-stat")
    public String chageStat(@PathVariable Long itemId, RedirectAttributes redirectAttributes){
        adminItemService.changeStatus(itemId, redirectAttributes);
        return "redirect:/admin/manage/item";
    }
}
