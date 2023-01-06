package com.niulijie.ucenter.utils;

import com.niulijie.ucenter.constant.AuthTokenConst;
import com.niulijie.ucenter.exception.CommonException;
import com.niulijie.ucenter.pojo.entity.UserAuthEntity;
import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.DefaultClock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.function.Function;

/**
 * @author byron
 * @date 2021.7.16
 */
@Component
public class AuthTokenUtil {

    private static final String CLAIM_KEY_ACCESS_ID = "aid";
    private static final String CLAIM_KEY_TENANT_ID = "tid";
    private static final String CLAIM_KEY_TENANT_IDS = "tids";
    private static final String CLAIM_KEY_USER_ID = "uid";
    private static final String CLAIM_KEY_DEPT_IDS = "dids";
    private static final String CLAIM_KEY_SYS_CODE = "sysCode";
    private static final String CLAIM_KEY_ADMIN = "admin";

    private Clock clock = DefaultClock.INSTANCE;

    @Value("${config.jwt.secret:c3efaaba024f03d0997c68428da6a480}")
    private String secret;

    @Value("${config.jwt.expiration:86400}")
    private Long expiration;

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public String getToken (){
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        String authorization = request.getHeader(AuthTokenConst.AUTHORIZATION);
        if (StringUtils.isEmpty(authorization) || !authorization.startsWith(AuthTokenConst.TOKEN_PREFIX)){
            throw new CommonException("token不存在");
        }
        return authorization.replace(AuthTokenConst.TOKEN_PREFIX,"");
    }

    /**
     * 获取账号ID
     * @return
     */
    public Integer getAccessIdFromToken() {
        return getAllClaimsFromToken(getToken()).get(CLAIM_KEY_ACCESS_ID, Integer.class);
    }

    /**
     * 是否是管理员
     * @return
     */
    public Boolean isAdmin(){
        Boolean isAdmin = getAllClaimsFromToken(getToken()).get(CLAIM_KEY_ADMIN, Boolean.class);
        if(ObjectUtils.isEmpty(isAdmin) || !isAdmin){
             return false;
        }
        return true;
    }

    public Integer getAccessIdFromToken(String token) {
        return getAllClaimsFromToken(token).get(CLAIM_KEY_ACCESS_ID, Integer.class);
    }

    /**
     * 获取企业信息
     * @return
     */
    public Integer getTenantIdFromToken() {
        return getAllClaimsFromToken(getToken()).get(CLAIM_KEY_TENANT_ID, Integer.class);
    }

    public Integer getTenantIdFromToken(String token) {
        return getAllClaimsFromToken(token).get(CLAIM_KEY_TENANT_ID, Integer.class);
    }

    public List<?> getTenantIdsFromToken(String token) {
        return getAllClaimsFromToken(token).get(CLAIM_KEY_TENANT_IDS, List.class);
    }

    String getSysCodeFromToken(String token) {
        return getAllClaimsFromToken(token).get(CLAIM_KEY_SYS_CODE, String.class);
    }

    public Integer getUserIdFromToken(String token) {
        return getAllClaimsFromToken(token).get(CLAIM_KEY_USER_ID, Integer.class);
    }

    public List<Integer> getDeptIdsFromToken(String token) {
        return getAllClaimsFromToken(token).get(CLAIM_KEY_DEPT_IDS, List.class);
    }

    public Boolean getAdminFromToken(String token) {
        return getAllClaimsFromToken(token).get(CLAIM_KEY_ADMIN, Boolean.class);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        return claimsResolver.apply(getAllClaimsFromToken(token));
    }

    private Claims getAllClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        }catch (ExpiredJwtException e) {
            claims = e.getClaims();
        }

        return claims;
    }

    Boolean isTokenExpired(String token) {
        return getExpirationDateFromToken(token).before(clock.now());
    }

    private Boolean ignoreTokenExpiration(String token) {
        return false;
    }

    public String generateToken(UserAuthEntity user) {
        Map<String, Object> claims = new HashMap<>(10);
        claims.put(CLAIM_KEY_ACCESS_ID, user.getAccessId());
        claims.put(CLAIM_KEY_TENANT_ID, user.getTenantId());
        claims.put(CLAIM_KEY_TENANT_IDS, user.getTenantIds().toArray());
        claims.put(CLAIM_KEY_USER_ID, user.getUserId());
        claims.put(CLAIM_KEY_DEPT_IDS, user.getDeptIds().toArray());
        claims.put(CLAIM_KEY_SYS_CODE, user.getSysCode());
        claims.put(CLAIM_KEY_ADMIN, user.getAdmin());
        return doGenerateToken(claims, user.getUsername());
    }

    private String doGenerateToken(Map<String, Object> claims, String subject) {
        final Date createdDate = clock.now();
        final Date expirationDate = calculateExpirationDate(createdDate);

        return Jwts.builder()
                .setClaims(claims)
                // 该JWT所面向得用户
                .setSubject(subject)
                // 签发时间
                .setIssuedAt(createdDate)
                // 过期时间
                .setExpiration(expirationDate)
                // 加密方式
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public Boolean canTokenBeRefreshed(String token) {
        return (!isTokenExpired(token) || ignoreTokenExpiration(token));
    }

    public String refreshToken(String token) {
        final Date createdDate = clock.now();
        final Date expirationDate = calculateExpirationDate(createdDate);

        final Claims claims = getAllClaimsFromToken(token);
        claims.setIssuedAt(createdDate);
        claims.setExpiration(expirationDate);

        return Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS512, secret).compact();
    }

    Boolean validateToken(String token, UserAuthEntity user) {
        if (user != null) {
            return (getUsernameFromToken(token).equals(user.getUsername())
                    && getAccessIdFromToken(token).equals(user.getAccessId())
                    && !isTokenExpired(token));
        }
        return false;
    }

    private Date calculateExpirationDate(Date createdDate) {
        return new Date(createdDate.getTime() + expiration * 1000);
    }

}
