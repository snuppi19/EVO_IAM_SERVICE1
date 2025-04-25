package com.mtran.mvc.controller;

import com.mtran.mvc.config.utils.jwt.JwtUtil;
import com.mtran.mvc.dto.CustomUserDetails;
import com.mtran.mvc.dto.UserDTO;
import com.mtran.mvc.dto.request.ChangePasswordRequest;
import com.mtran.mvc.dto.request.LogoutRequest;
import com.mtran.mvc.entity.User;
import com.mtran.mvc.mapper.UserMapper;
import com.mtran.mvc.service.UserActivityLogService;
import com.mtran.mvc.service.impl.UploadImageFileServiceImpl;
import com.mtran.mvc.service.impl.UserServiceImpl;
import com.mtran.mvc.support.ActivityType;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@RestController
@RequestMapping("/home")
@AllArgsConstructor
public class HomeController {
    private final UserServiceImpl userService;
    private final UserMapper userMapper;
    private final UserActivityLogService userActivityLogService;
    private final JwtUtil jwtUtil;
    private final UploadImageFileServiceImpl uploadImageFile;

    @GetMapping("/user")
    public UserDTO getUserIn4(Authentication authentication) {
        try {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            User user = userDetails.getUser();
            return userMapper.toUserDTO(user);
        } catch (Exception e) {
            throw new RuntimeException("Token không hợp lệ hoặc đã hết hạn : " + e.getMessage());
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateUserIn4(@RequestBody UserDTO userDTO,
                                           HttpServletRequest request) {
        userService.updateUser(userDTO);
        userActivityLogService.logActivity(userDTO.getEmail(), ActivityType.UPDATE_PROFILE,
                "user update profile", request);
        return ResponseEntity.ok("User cập nhật thông tin thành công!");
    }

    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest,
                                            HttpServletRequest request) {
        userService.changePassword(changePasswordRequest.getUserDTO().getEmail(),
                changePasswordRequest.getUserDTO().getPassword(),
                changePasswordRequest.getNewPassword());

        String token = changePasswordRequest.getToken();
        String refreshToken = changePasswordRequest.getRefreshToken();

        if (token != null || refreshToken != null) {
            LogoutRequest logoutRequest = new LogoutRequest();
            logoutRequest.setToken(token);
            logoutRequest.setRefreshToken(refreshToken);
            try {
                jwtUtil.logout(logoutRequest);
            } catch (Exception e) {
                System.out.println("Token không hợp lệ : " + e.getMessage());
            }
        }

        userActivityLogService.logActivity(changePasswordRequest.getUserDTO().getEmail(),
                ActivityType.CHANGE_PASSWORD,"user change password", request);
        return ResponseEntity.ok("User thay đổi mật khẩu thành công !");
    }

    @PostMapping("/upload-image")
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file,
                                         Authentication authentication,
                                         HttpServletRequest request) throws IOException {
       String imageUrl=uploadImageFile.uploadImageFile(file);
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User user=userDetails.getUser();

        UserDTO userDTO=userMapper.toUserDTO(user);
        userDTO.setImageUrl(imageUrl);
        userService.updateUser(userDTO);
        userActivityLogService.logActivity(userDTO.getEmail(), ActivityType.UPLOAD_IMAGE, "user uploaded profile image", request);
        return ResponseEntity.ok("Tải ảnh thành công");
    }
}
