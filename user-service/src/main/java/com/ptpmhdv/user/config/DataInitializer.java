package com.ptpmhdv.user.config;

import com.ptpmhdv.user.entity.Role;
import com.ptpmhdv.user.entity.User;
import com.ptpmhdv.user.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Khoi tao du lieu mau: tai khoan quan tri de dang nhap ngay khi chay demo.
 * Tai khoan: admin / admin123
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (userRepository.existsByUsername("admin")) {
            return;
        }

        User admin = new User();
        admin.setUsername("admin");
        admin.setEmail("admin@ptpmhdv.com");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setFullName("Quan tri vien");
        admin.setPhone("0900000000");
        admin.setAddress("Ha Noi, Viet Nam");
        admin.setRole(Role.ADMIN);
        userRepository.save(admin);

        User demoUser = new User();
        demoUser.setUsername("khachhang");
        demoUser.setEmail("khachhang@ptpmhdv.com");
        demoUser.setPassword(passwordEncoder.encode("123456"));
        demoUser.setFullName("Nguyen Van A");
        demoUser.setPhone("0911111111");
        demoUser.setAddress("Quan 1, TP.HCM");
        demoUser.setRole(Role.USER);
        userRepository.save(demoUser);
    }
}
