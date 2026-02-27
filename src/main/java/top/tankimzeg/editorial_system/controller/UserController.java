package top.tankimzeg.editorial_system.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import top.tankimzeg.editorial_system.entity.User;
import top.tankimzeg.editorial_system.service.UserService;
import top.tankimzeg.editorial_system.utils.ApiResponse;

import java.util.List;

/**
 * @author Kim
 * @date 2026/2/27 20:26
 * @description 用户管理控制器，提供用户信息查询与管理相关接口，管理员权限访问
 */
@RestController
@RequestMapping("/api/users")
@Tag(name = "用户管理", description = "用户信息查询与管理相关接口")
@PreAuthorize("hasRole('ADMIN')")
public class UserController {

    @Autowired
    private UserService userService;

    @DeleteMapping("/{id}")
    @Operation(summary = "删除用户", description = "根据用户ID删除用户")
    public ApiResponse<Boolean> deleteUserById(@PathVariable Long id) {
        userService.deleteUserById(id);
        return ApiResponse.success(Boolean.TRUE);
    }

    @Operation(summary = "获取用户列表", description = "获取系统中所有用户的列表")
    @GetMapping("/list")
    public ApiResponse<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ApiResponse.success(users);
    }
}
