package com.ptpmhdv.product.service;

import com.ptpmhdv.common.exception.BadRequestException;
import com.ptpmhdv.common.exception.ResourceNotFoundException;
import com.ptpmhdv.product.dto.ProductRequest;
import com.ptpmhdv.product.dto.ProductResponse;
import com.ptpmhdv.product.entity.Category;
import com.ptpmhdv.product.entity.Product;
import com.ptpmhdv.product.repository.CategoryRepository;
import com.ptpmhdv.product.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    public Page<ProductResponse> search(String keyword, Long categoryId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        String normalizedKeyword = (keyword == null || keyword.isBlank()) ? null : keyword.trim();
        return productRepository.search(normalizedKeyword, categoryId, pageable).map(ProductResponse::from);
    }

    public ProductResponse findById(Long id) {
        return ProductResponse.from(getOrThrow(id));
    }

    public List<ProductResponse> findByIds(List<Long> ids) {
        return productRepository.findByIdIn(ids).stream().map(ProductResponse::from).toList();
    }

    public ProductResponse create(ProductRequest request) {
        Product product = new Product();
        applyRequest(product, request);
        return ProductResponse.from(productRepository.save(product));
    }

    public ProductResponse update(Long id, ProductRequest request) {
        Product product = getOrThrow(id);
        applyRequest(product, request);
        return ProductResponse.from(productRepository.save(product));
    }

    public void delete(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Khong tim thay san pham id=" + id);
        }
        productRepository.deleteById(id);
    }

    /** Duoc goi noi bo boi order-service (qua Feign) de kiem tra & tru ton kho khi checkout. */
    public void decreaseStock(Long productId, int quantity) {
        Product product = getOrThrow(productId);
        if (product.getStockQuantity() < quantity) {
            throw new BadRequestException("San pham '" + product.getName() + "' khong du hang ton kho");
        }
        product.setStockQuantity(product.getStockQuantity() - quantity);
        productRepository.save(product);
    }

    private Product getOrThrow(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Khong tim thay san pham id=" + id));
    }

    private void applyRequest(Product product, ProductRequest request) {
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStockQuantity(request.getStockQuantity());
        product.setImageUrl(request.getImageUrl());
        product.setBrand(request.getBrand());
        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Khong tim thay danh muc id=" + request.getCategoryId()));
            product.setCategory(category);
        } else {
            product.setCategory(null);
        }
    }
}
