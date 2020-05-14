package com.ysjj.cloud.data.common.util;

import com.ysjj.cloud.data.common.config.JwtProperties;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: jwt的claim里一般包含以下几种数据:
 * 1. iss -- token的发行者
 * 2. sub -- 该JWT所面向的用户
 * 3. aud -- 接收该JWT的一方
 * 4. exp -- token的失效时间
 * 5. nbf -- 在此时间段之前,不会被处理
 * 6. iat -- jwt发布时间
 * 7. jti -- jwt唯一标识,防止重复使用
 * <br>
 * @Date: 2020/5/13 18:00 <br>
 * @author: hongjie.liu <br>
 * @version: 1.0 <br>
 */
@Component
public class JwtTokenUtil {

    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 获取用户名从token中
     */
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token).getSubject();
    }

    /**
     * 获取jwt发布时间
     */
    public Date getIssuedAtDateFromToken(String token) {
        return getClaimFromToken(token).getIssuedAt();
    }

    /**
     * 获取jwt失效时间
     */
    public Date getExpirationDataFromToken(String token) {
        return getClaimFromToken(token).getExpiration();
    }

    /**
     * 获取jwt接受者
     */
    public String getAudienceFromToken(String token) {
        return getClaimFromToken(token).getAudience();
    }

    /**
     * 获取私有的jwt claim
     */
    public String getPrivateClaimFromToken(String token, String key) {
        return getClaimFromToken(token).get(key).toString();
    }

    /**
     * 获取md5 key 从 token中
     */
    public String getMd5KeyFromToken(String token) {
        return getPrivateClaimFromToken(token, jwtProperties.getMd5Key());
    }

    /**
     * 获取jwt的payload部分
     */
    public Claims getClaimFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(jwtProperties.getSecret())
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 解析token是否正确，不正确会抛异常
     */
    public void parseToken(String token) throws JwtException {
        Jwts.parser().setSigningKey(jwtProperties.getSecret()).parseClaimsJws(token).getBody();
    }

    /**
     * 验证token是否失效
     * true：失效 false：没有失效
     */
    public Boolean isTokenExpired(String token) {
        try {
            final Date expiration = getExpirationDataFromToken(token);
            return expiration.before(new Date());
        } catch (ExpiredJwtException e) {
            return true;
        }
    }

    /**
     * 生成token（通过用户名和签名时候用的随机数）
     */
    public String generateToken(String jsonString, String randomKey) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(jwtProperties.getMd5Key(), randomKey);
        return doGenerateToken(claims, jsonString);
    }

    /**
     * 生成token（通过用户名和签名时候的随机数）
     */
    public String generateTokenNetty(String jsonString, String randomKey, Long expiration) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(jwtProperties.getMd5Key(), randomKey);
        return doGenerateTokenNetty(claims, jsonString, expiration);
    }

    /**
     * 生成token
     */
    private String doGenerateToken(Map<String, Object> claims, String subject) {
        final Date createdDate = new Date();
        final Date expirationDate = new Date(createdDate.getTime() + jwtProperties.getExpiration() * 1000);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(createdDate)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, jwtProperties.getSecret())
                .compact();
    }

    /**
     * 生成token
     */
    public String doGenerateTokenNetty(Map<String, Object> claims, String subject, Long expiration) {
        final Date createdDate = new Date();
        final Date expirationDate = new Date(createdDate.getTime() + expiration * 1000);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, jwtProperties.getSecret())
                .compact();
    }

    /**
     * 获取混淆MD5签名用的随机数字符串
     */
    public String getRandomKey() {
        return ToolUtil.getRandomString(6);
    }

}
