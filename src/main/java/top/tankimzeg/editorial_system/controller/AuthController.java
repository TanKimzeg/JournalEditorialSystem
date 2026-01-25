package top.tankimzeg.editorial_system.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import top.tankimzeg.editorial_system.dto.response.JwtResponse;
import top.tankimzeg.editorial_system.dto.request.LoginRequest;
import top.tankimzeg.editorial_system.dto.request.RegisterRequest;
import top.tankimzeg.editorial_system.entity.JournalUserDetails;
import top.tankimzeg.editorial_system.entity.User;
import top.tankimzeg.editorial_system.exception.BusinessException;
import top.tankimzeg.editorial_system.service.UserService;
import top.tankimzeg.editorial_system.utils.JwtUtil;

import org.springframework.security.core.AuthenticationException;
import top.tankimzeg.editorial_system.utils.SecurityUtil;
import top.tankimzeg.editorial_system.utils.ApiResponse;

import jakarta.validation.Valid;

/**
 * @author Kim
 * @date 2026/1/20 21:09
 * @description 用户认证管理控制器
 */
@RestController
@RequestMapping("/api/auth/user")
@Tag(name = "认证管理", description = "用户登录、注册与认证相关接口")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    @Operation(summary = "用户登录", description = "用户通过用户名和密码进行登录，成功后返回JWT令牌")
    public ApiResponse<JwtResponse> authenticateUser(
            @Valid @RequestBody LoginRequest loginRequest
    ) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            JournalUserDetails userDetails = (JournalUserDetails) authentication.getPrincipal();
            String jwt = jwtUtil.generateToken(userDetails);
            return ApiResponse.success(
                    new JwtResponse(
                            jwt,
                            userDetails.getId(),
                            userDetails.getUsername(),
                            userDetails.getRealName(),
                            userDetails.getEmail()
                    ));
        } catch (AuthenticationException ex) {
            throw new BusinessException(HttpStatus.UNAUTHORIZED, "用户名或密码错误");
        }
    }

    @PostMapping("/register")
    @Operation(summary = "用户注册", description = "新用户注册接口")
    public ApiResponse<JwtResponse> registerUser(
            @Valid @RequestBody RegisterRequest registerRequest
    ) {
        if (userService.existsByUsername(registerRequest.getUsername())) {
            throw new BusinessException("用户名已被注册");
        }
        if (userService.existsByEmail(registerRequest.getEmail())) {
            throw new BusinessException("邮箱已被注册");
        }

        // 创建新用户
        User newUser = new User();
        newUser.setUsername(registerRequest.getUsername());
        newUser.setEmail(registerRequest.getEmail());
        newUser.setRealName(registerRequest.getRealName());
        newUser.setRole(User.Role.AUTHOR);
        newUser.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        User createdUser = userService.createAuthor(newUser);

        // 自动登录
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        registerRequest.getUsername(),
                        registerRequest.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        JournalUserDetails userDetails = (JournalUserDetails) authentication.getPrincipal();
        String jwt = jwtUtil.generateToken(userDetails);
        return ApiResponse.success(
                new JwtResponse(
                        jwt,
                        userDetails.getId(),
                        userDetails.getUsername(),
                        userDetails.getRealName(),
                        userDetails.getEmail()
                )
        );
    }

    @GetMapping("/me")
    @Operation(summary = "获取当前用户信息", description = "获取已认证用户的详细信息")
    public ApiResponse<JournalUserDetails> getCurrentUserInfo() {
        return ApiResponse.success(SecurityUtil.getCurrentUserDetails()
                .orElseThrow(() -> new BusinessException(HttpStatus.UNAUTHORIZED, "未认证的用户")));
    }

}