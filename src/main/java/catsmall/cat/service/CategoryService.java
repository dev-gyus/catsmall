package catsmall.cat.service;

import catsmall.cat.entity.Category;
import catsmall.cat.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public void save(Category category){
        categoryRepository.save(category);
    }

    public List<Category> findAll(){
        return categoryRepository.findAll();
    }

    public Category findById(Long id){
        Optional<Category> result = categoryRepository.findById(id);
        return result.get();
    }

    public Category findByName(String name){
        return categoryRepository.findByName(name);
    }

    public List<Category> findByParentName(String parentName){
        return categoryRepository.findByParentName(parentName);
    }
}
