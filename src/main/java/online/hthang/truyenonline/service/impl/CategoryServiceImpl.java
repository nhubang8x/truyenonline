package online.hthang.truyenonline.service.impl;

import online.hthang.truyenonline.entity.Category;
import online.hthang.truyenonline.projections.CategorySummary;
import online.hthang.truyenonline.repository.CategoryRepository;
import online.hthang.truyenonline.service.CategoryService;
import online.hthang.truyenonline.utils.ConstantsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author Huy Thang
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List< Category > getCategoryMenu() {
        return categoryRepository.findByStatus(ConstantsUtils.STATUS_ACTIVED);
    }

    @Override
    public Optional< Category > getCategoryByID(Integer id) {
        return categoryRepository.findByIdAndStatus(id, ConstantsUtils.STATUS_ACTIVED);
    }

    @Override
    public Page< Category > getAllCategory(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return categoryRepository.findAll(pageable);
    }

    @Override
    public boolean deleteCategory(Integer id) {
        try {
            categoryRepository.deleteById(id);
        }catch (Exception e){
            return false;
        }
        return true;
    }
}
