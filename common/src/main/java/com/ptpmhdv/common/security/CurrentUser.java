package com.ptpmhdv.common.security;

import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Thong tin nguoi dung duoc giai ma tu JWT, gan vao SecurityContext lam
 * "principal" boi JwtAuthFilter.
 */
public record CurrentUser(Long userId, String username, String role) {

    public static CurrentUser get() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof CurrentUser currentUser)) {
            throw new IllegalStateException("Khong tim thay nguoi dung dang dang nhap trong SecurityContext");
        }
        return currentUser;
    }
}
