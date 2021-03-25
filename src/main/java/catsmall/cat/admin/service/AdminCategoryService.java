package catsmall.cat.admin.service;

import catsmall.cat.admin.manage.dto.AdminCategoryDto;
import catsmall.cat.admin.repository.AdminCategoryRepository;
import catsmall.cat.entity.Category;
import catsmall.cat.entity.CategoryDto;
import catsmall.cat.entity.ItemCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminCategoryService {
    private final AdminCategoryRepository adminCategoryRepository;

    public List<Category> findAll(){
        return adminCategoryRepository.findAll();
    }
    public void addChild(CategoryDto categoryDto){

    }

    @Transactional
    public void addSubcategory(AdminCategoryDto adminCategoryDto) {
        Category child = new Category(adminCategoryDto.getChild());
        Category parent = adminCategoryRepository.findByName(adminCategoryDto.getParent());
        adminCategoryRepository.save(child);
        parent.addChild(child);
    }
}
