package catsmall.cat.admin.controller;

import catsmall.cat.admin.repository.AdminCategoryRepository;
import catsmall.cat.admin.service.AdminCategoryService;
import catsmall.cat.admin.service.AdminItemService;
import catsmall.cat.entity.Category;
import catsmall.cat.admin.manage.dto.AdminCategoryDto;
import catsmall.cat.entity.dto.item.ItemDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminCategoryService adminCategoryService;
    private final AdminItemService adminItemService;
    private final AdminCategoryRepository adminCategoryRepository;

    @GetMapping({"", "/"})
    public String admin_home(){
        return "admin/main";
    }
    @GetMapping("/addcategory")
    public String addCategory(@ModelAttribute("adminCategoryDto") AdminCategoryDto adminCategoryDto,
                              Model model){
        List<Category> categories = adminCategoryService.findAll();
        model.addAttribute("categories", categories);
        return "admin/category/add_category";
    }
    @PostMapping("/addcategory")
    public String addCategory_do(@ModelAttribute("adminCategoryDto") AdminCategoryDto adminCategoryDto){
        adminCategoryService.addSubcategory(adminCategoryDto);
        return "redirect:/admin";
    }

    @GetMapping("/additem")
    public String addItem(@ModelAttribute("itemDto") ItemDto itemDto,
                          Model model){
        List<Category> types = adminCategoryRepository.findTopCategories();
        List<Category> allCategories = adminCategoryRepository.findAll();
        model.addAttribute("types", types);
        model.addAttribute("categories",allCategories);
        return "admin/item/add_item";
    }
    @PostMapping("/additem")
    public String additem_do(@Valid @ModelAttribute("itemDto") ItemDto itemDto,
                             BindingResult result,
                             Model model){
        adminItemService.addItem(itemDto, result);
        if(result.hasErrors()){
            List<Category> types = adminCategoryRepository.findTopCategories();
            List<Category> categories = adminCategoryRepository.findAll();
            model.addAttribute("categories",categories);
            model.addAttribute("types", types);
            return "admin/item/add_item";
        }
        return "redirect:/admin";
    }
}
