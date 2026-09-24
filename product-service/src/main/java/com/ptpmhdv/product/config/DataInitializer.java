package com.ptpmhdv.product.config;

import com.ptpmhdv.product.entity.Category;
import com.ptpmhdv.product.entity.Product;
import com.ptpmhdv.product.repository.CategoryRepository;
import com.ptpmhdv.product.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Du lieu mau: danh muc va san pham thiet bi dien tu de demo ngay khi chay.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public DataInitializer(CategoryRepository categoryRepository, ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    @Override
    public void run(String... args) {
        if (categoryRepository.count() > 0) {
            return;
        }

        Category dienThoai = save("Dien thoai", "Dien thoai thong minh cac hang");
        Category laptop = save("Laptop", "May tinh xach tay cho hoc tap va lam viec");
        Category amThanh = save("Am thanh", "Tai nghe, loa va thiet bi am thanh");
        Category phuKien = save("Phu kien", "Sac, cap, op lung va phu kien khac");
        Category manHinh = save("Man hinh", "Man hinh may tinh cac loai");

        product("iPhone 15 Pro Max", "Dien thoai flagship cua Apple, chip A17 Pro", new BigDecimal("32990000"), 25, dienThoai, "Apple", "https://picsum.photos/seed/iphone15/400/300");
        product("Samsung Galaxy S24 Ultra", "But S-Pen, camera 200MP", new BigDecimal("29990000"), 30, dienThoai, "Samsung", "https://picsum.photos/seed/s24ultra/400/300");
        product("Xiaomi 14", "Camera Leica, hieu nang manh me", new BigDecimal("16990000"), 40, dienThoai, "Xiaomi", "https://picsum.photos/seed/xiaomi14/400/300");

        product("MacBook Air M3", "Laptop mong nhe, pin trau, chip M3", new BigDecimal("27990000"), 20, laptop, "Apple", "https://picsum.photos/seed/macair/400/300");
        product("Dell XPS 13", "Man hinh InfinityEdge, thiet ke cao cap", new BigDecimal("25990000"), 15, laptop, "Dell", "https://picsum.photos/seed/xps13/400/300");
        product("Asus ROG Zephyrus G14", "Laptop gaming manh me, man hinh 165Hz", new BigDecimal("34990000"), 10, laptop, "Asus", "https://picsum.photos/seed/rogg14/400/300");

        product("AirPods Pro 2", "Chong on chu dong, am thanh khong gian", new BigDecimal("5990000"), 50, amThanh, "Apple", "https://picsum.photos/seed/airpodspro/400/300");
        product("Sony WH-1000XM5", "Tai nghe chup tai chong on hang dau", new BigDecimal("8490000"), 25, amThanh, "Sony", "https://picsum.photos/seed/sonywh/400/300");
        product("JBL Flip 6", "Loa bluetooth chong nuoc IP67", new BigDecimal("2490000"), 60, amThanh, "JBL", "https://picsum.photos/seed/jblflip6/400/300");

        product("Sac nhanh Anker 65W", "Sac nhanh GaN 3 cong cho laptop va dien thoai", new BigDecimal("890000"), 100, phuKien, "Anker", "https://picsum.photos/seed/anker65/400/300");
        product("Cap USB-C to USB-C 2m", "Cap sac va truyen du lieu toc do cao", new BigDecimal("199000"), 200, phuKien, "Ugreen", "https://picsum.photos/seed/usbc2m/400/300");

        product("LG UltraGear 27 inch", "Man hinh gaming 27 inch 165Hz QHD", new BigDecimal("7990000"), 18, manHinh, "LG", "https://picsum.photos/seed/lgultragear/400/300");
        product("Dell UltraSharp 27 4K", "Man hinh do hoa 4K, do phu mau cao", new BigDecimal("12990000"), 12, manHinh, "Dell", "https://picsum.photos/seed/dellultrasharp/400/300");
    }

    private Category save(String name, String description) {
        Category category = new Category();
        category.setName(name);
        category.setDescription(description);
        return categoryRepository.save(category);
    }

    private void product(String name, String description, BigDecimal price, int stock, Category category, String brand, String imageUrl) {
        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setStockQuantity(stock);
        product.setCategory(category);
        product.setBrand(brand);
        product.setImageUrl(imageUrl);
        productRepository.save(product);
    }
}
