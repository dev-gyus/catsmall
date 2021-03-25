package catsmall.cat.admin.service;

import catsmall.cat.admin.repository.AdminItemCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminItemCategoryService {
    private final AdminItemCategoryRepository adminItemCategoryRepository;
}
