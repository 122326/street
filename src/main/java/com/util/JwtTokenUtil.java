package com.util;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * jwt生成token
 * @Author kai
 * @create 2022-09-11 11:23
 */
@Component
public class JwtTokenUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    /**
     * 传入用户登陆信息，生成token
     * @param details
     * @return
     */
    public String generateToken(UserDetails details) {
        Map<String, Object> map = new HashMap<>();
        map.put("userName", details.getUsername());
        map.put("createTime", new Date());
        return this.generateJWT(map);
    }


    /**
     * 根据荷载生成token
     * @param claims
     * @return
     */
    public String generateJWT(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, secret)
                .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000))
                .compact();
    }

    /**
     * 获取token中的信息
     * @param token
     * @return
     */
    public Claims getTokenBody(String token) {
        try {
            return Jwts.parser()
                    //返回了DefaultJwtParser 对象
                    .setSigningKey(secret)
                    //setSigningKey() 与builder中签名方法signWith()对应，parser中的此方法拥有与signWith()方法相同的三种参数形式，用于设置JWT的签名key，用户后面对JWT进行解析。
                    .parseClaimsJws(token)
                    //parseClaimsJwt 载荷为claims（即Json），未签名
                    .getBody();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 根据token得到用户名
     * @param token
     * @return
     */
    public String getUsernameByToken(String token){
        return (String) this.getTokenBody(token).get("userName");
    }

    /**
     * 根据token判断当前时间内，该token是否过期
     * @param token
     * @return
     */
    public boolean isExpiration(String token){
        return this.getTokenBody(token).getExpiration().before(new Date());
    }

    /**
     * 刷新token
     * @param token
     * @return
     */
    public String reRefresh(String token){
        Claims claims=this.getTokenBody(token);
        claims.setExpiration(new Date());
        return this.generateJWT(claims);
    }

}
