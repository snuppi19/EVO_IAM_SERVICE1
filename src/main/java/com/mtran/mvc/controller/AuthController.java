package com.mtran.mvc.controller;

import com.mtran.mvc.config.utils.jwt.JwtUtil;
import com.mtran.mvc.dto.request.AccessRequest;
import com.mtran.mvc.dto.request.LogoutRequest;
import com.mtran.mvc.dto.request.OtpVerificationRequest;
import com.mtran.mvc.dto.request.RefreshRequest;
import com.mtran.mvc.dto.UserDTO;
import com.mtran.mvc.dto.response.TokenResponse;
import com.mtran.mvc.entity.UserActivityLog;
import com.mtran.mvc.service.UserActivityLogService;
import com.mtran.mvc.service.email.EmailSenderService;
import com.mtran.mvc.service.impl.UserServiceImpl;
import com.mtran.mvc.support.ActivityType;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {
    private final UserServiceImpl userService;
    private final JwtUtil jwtUtil;
    private final EmailSenderService emailSenderService;
    private final com.mtran.mvc.service.email.OtpService OtpService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserActivityLogService userActivityLogService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AccessRequest accessRequest, HttpServletRequest request) {
        String username = accessRequest.getEmail();
        String password = accessRequest.getPassword();

        //tao token neu hop le
        UserDTO userDTO = userService.findByEmail(username);
        if (userDTO == null) {
            throw new RuntimeException("Tên đăng nhập không đúng!");
        }
        if (!passwordEncoder.matches(password, userDTO.getPassword())) {
            throw new RuntimeException("Mật khẩu không đúng!");
        }

        try {
            String accessToken = jwtUtil.generateToken(username);
            String refreshToken = jwtUtil.generateRefreshToken(username);
            userActivityLogService.logActivity(username, ActivityType.LOGIN,"user login",request);
            return ResponseEntity.ok(new TokenResponse(accessToken, refreshToken));
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi tạo token : " + e.getMessage());
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody LogoutRequest apiRequest,
                                       HttpServletRequest request) throws Exception {
        String email= jwtUtil.extractEmail(apiRequest.getToken());
        userActivityLogService.logActivity(email, ActivityType.LOGOUT,"user logout",request);
        jwtUtil.logout(apiRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/refresh")
    public String refresh(@RequestBody RefreshRequest refreshRequest) throws Exception {
        return jwtUtil.refreshToken(refreshRequest);
    }


    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserDTO userDTO,HttpServletRequest request) {
        if (userService.findByEmail(userDTO.getEmail()) != null) {
            return ResponseEntity.badRequest().body("Email đã được sử dụng");
        }
        String otp = OtpService.generateOtp(userDTO.getEmail());
        emailSenderService.sendEmail(userDTO.getEmail(), "Mã xác thực OTP", "Mã OTP của bạn là: " + otp);
        userActivityLogService.logActivity(userDTO.getEmail(), ActivityType.REGISTER,"user register",request);
        return ResponseEntity.ok("OTP đã được gửi tới email");
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtp(@RequestBody OtpVerificationRequest request) {
        if (OtpService.verifyOtp(request.getEmail(), request.getOtp()) && request.isRegister()) {
            userService.createUser(request.getUserDTO());
            OtpService.deleteOtp(request.getEmail());
            return ResponseEntity.ok("Đăng ký thành công");
        } else if (OtpService.verifyOtp(request.getEmail(), request.getOtp()) && !request.isRegister()) {
            userService.forgotPassword(request.getEmail(), request.getUserDTO().getPassword());
            return ResponseEntity.ok("đã thay đổi password thành công");
        } else {
            return ResponseEntity.badRequest().body("OTP không hợp lệ");
        }
    }

    @PutMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody UserDTO userDTO, HttpServletRequest request) {
        if (userService.findByEmail(userDTO.getEmail()) == null) {
            return ResponseEntity.badRequest().body("Email không tồn tại");
        }
        String otp = OtpService.generateOtp(userDTO.getEmail());
        emailSenderService.sendEmail(userDTO.getEmail(), "Mã xác thực OTP", "Mã OTP của bạn là: " + otp);
        userActivityLogService.logActivity(userDTO.getEmail(), ActivityType.FORGOT_PASSWORD,"user forgot password",request);
        return ResponseEntity.ok("OTP đã được gửi tới email");
    }

    @GetMapping("/activity-log")
    public ResponseEntity<List<UserActivityLog>> getActivityLog(HttpServletRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        List<UserActivityLog> activities = userActivityLogService.getUserActivities(email);
        return ResponseEntity.ok(activities);
    }

}
