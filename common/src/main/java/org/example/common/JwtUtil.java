package org.example.common;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    // 秘钥
    private static final String SECRET_KEY = "850966wym";
    private static final Algorithm ALGORITHM = Algorithm.HMAC256(SECRET_KEY);
    // 生成 JWT 的有效时间（例如 1 小时）
    private static final long EXPIRATION_TIME = 3600 * 1000 * 24;

    /**
     * 生成 JWT Token
     *
     * @param userId 用户 ID
     * @return JWT 字符串
     */
    public String generateToken(String userId) {
        return JWT.create()
                .withSubject("user-info")
                .withClaim("userId", userId)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(ALGORITHM);


    }

    /**
     * 验证并解析 JWT
     *
     * @param token JWT 字符串
     * @return DecodedJWT 对象
     */
    public DecodedJWT verifyToken(String token) {
        return JWT.require(ALGORITHM)
                .build()
                .verify(token);
    }

    /**
     * 从 Token 中提取用户 ID
     *
     * @param token JWT 字符串
     * @return 用户 ID
     */
    public String parseToken(String token) {
        DecodedJWT decodedJWT = verifyToken(token);
        return decodedJWT.getClaim("userId").asString();
    }
}