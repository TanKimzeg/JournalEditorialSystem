package top.tankimzeg.editorial_system.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import top.tankimzeg.editorial_system.entity.JournalUserDetails;
import top.tankimzeg.editorial_system.entity.User;

import java.util.Optional;

/**
 * @author Kim
 * @date 2026/1/20 21:54
 * @description 获取当前登录用户信息的工具类
 */
@Component
public class SecurityUtil {

    public static Long getCurrentUserId() {
        return getCurrentUserDetails()
                .orElseThrow()
                .getId();
    }

    public static User.Role getCurrentUserRole() {
        return getCurrentUserDetails()
                .orElseThrow()
                .getRole();

    }

    public static String getCurrentUsername() {
        return getCurrentUserDetails()
                .orElseThrow()
                .getUsername();
    }

    public static String getCurrentUserEmail() {
        return getCurrentUserDetails()
                .orElseThrow()
                .getEmail();
    }

    public static String getCurrentUserRealName() {
        return getCurrentUserDetails()
                .orElseThrow()
                .getRealName();
    }

    public static boolean isAuthor() {
        return getCurrentUserRole().equals(
                User.Role.AUTHOR
        ) || getCurrentUserRole().equals(
                User.Role.REVIEWER
        );
    }

    public static boolean isEditor() {
        return getCurrentUserRole().equals(
                User.Role.EDITOR
        );
    }

    public static boolean isReviewer() {
        return getCurrentUserRole().equals(
                User.Role.REVIEWER
        );
    }

    public static Optional<JournalUserDetails> getCurrentUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getDetails();
            if (principal instanceof JournalUserDetails)
                return Optional.ofNullable((JournalUserDetails) authentication.getPrincipal());
        }
        return Optional.empty();
    }

}
