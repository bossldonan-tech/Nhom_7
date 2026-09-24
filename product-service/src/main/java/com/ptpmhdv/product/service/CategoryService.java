package com.ptpmhdv.product.service;

import com.ptpmhdv.common.exception.ResourceNotFoundException;
import com.ptpmhdv.product.dto.CategoryRequest;
import com.ptpmhdv.product.dto.CategoryResponse;
import com.ptpmhdv.product.entity.Category;
import com.ptpmhdv.product.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<CategoryResponse> findAll() {
        return categoryRepository.findAll().stream().map(CategoryResponse::from).toList();
    }

    public CategoryResponse create(CategoryRequest request) {
        Category category = new Category();
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        return CategoryResponse.from(categoryRepository.save(category));
    }

    public CategoryResponse update(Long id, CategoryRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Khong tim thay danh muc id=" + id));
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        return CategoryResponse.from(categoryRepository.save(category));
    }

    public void delete(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Khong tim thay danh muc id=" + id);
        }
        categoryRepository.deleteById(id);
    }
}
