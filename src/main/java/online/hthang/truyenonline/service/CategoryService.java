package online.hthang.truyenonline.service;

import online.hthang.truyenonline.entity.Category;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

/**
 * @author Huy Thang
 */
public interface CategoryService {

    List< Category > getCategoryMenu();

    Optional< Category > getCategoryByID(Integer id);

    Page<Category> getAllCategory(int page, int size);

    boolean deleteCategory(Integer id);
}
