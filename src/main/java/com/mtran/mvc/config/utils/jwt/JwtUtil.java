package com.mtran.mvc.config.utils.jwt;

import com.mtran.mvc.config.utils.RSAKeyUtil;
import com.mtran.mvc.dto.request.LogoutRequest;
import com.mtran.mvc.dto.request.RefreshRequest;
import com.mtran.mvc.entity.InvalidateToken;
import com.mtran.mvc.repository.InvalidatedTokenRepository;
import com.mtran.mvc.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import org.springframework.stereotype.Component;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtUtil {
    private final RSAKeyUtil rsaKeyUtil;
    private final InvalidatedTokenRepository invalidatedTokenRepository;

    public JwtUtil(RSAKeyUtil rsaKeyUtil, InvalidatedTokenRepository invalidatedTokenRepository,
                   UserRepository userRepository) {
        this.rsaKeyUtil = rsaKeyUtil;
        this.invalidatedTokenRepository = invalidatedTokenRepository;
    }

    // tao token
    public String generateToken(String username) throws Exception {
        PrivateKey privateKey = rsaKeyUtil.getPrivateKey();
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000)) // 1 giờ
                .setId(UUID.randomUUID().toString())
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .compact();
    }

    //tạo refresh token
    public String generateRefreshToken(String username) throws Exception {
        PrivateKey privateKey = rsaKeyUtil.getPrivateKey();
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 604800000)) // 7 ngày
                .setId(UUID.randomUUID().toString())
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .compact();
    }

    // refresh token ( email duoc dung lam usernam)
    public String refreshToken(RefreshRequest refreshRequest) throws Exception {
        var signJWT = validateToken(refreshRequest.getToken());
        String jit = signJWT.getId();
        Date expiryTime = signJWT.getExpiration();

        InvalidateToken invalidateToken = InvalidateToken.builder()
                .id(jit)
                .expiryTime(expiryTime)
                .build();
        invalidatedTokenRepository.save(invalidateToken);

        String email = extractEmail(refreshRequest.getToken());
        return generateToken(email);
    }

    // kiem tra token
    public Claims validateToken(String token) throws Exception {

        PublicKey publicKey = rsaKeyUtil.getPublicKey();
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(publicKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        if (invalidatedTokenRepository.existsById(claims.getId())) {
            throw new RuntimeException("Token is invalid");
        }
        return claims;
    }

    //vo hieu hoa ca access token va refresh token
    public void logout(LogoutRequest request) throws Exception {
        //access token
        if (request.getToken() != null) {
            var signToken = validateToken(request.getToken());
            String jit = signToken.getId();
            Date expiryTime = signToken.getExpiration();
            InvalidateToken invalidateToken = InvalidateToken.builder()
                    .id(jit)
                    .expiryTime(expiryTime)
                    .build();
            invalidatedTokenRepository.save(invalidateToken);
        }
        //refresh token
        if (request.getRefreshToken() != null) {
            var refreshSignToken = validateToken(request.getRefreshToken());
            String refreshJit = refreshSignToken.getId();
            Date refreshExpiryTime = refreshSignToken.getExpiration();
            InvalidateToken refreshInvalidateToken = InvalidateToken.builder()
                    .id(refreshJit)
                    .expiryTime(refreshExpiryTime)
                    .build();
            invalidatedTokenRepository.save(refreshInvalidateToken);
        }
    }

    //lay ra email(unique) cua nguoi dung tu token
    public String extractEmail(String token) throws Exception {
        PublicKey publicKey = rsaKeyUtil.getPublicKey();
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(publicKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }
}

