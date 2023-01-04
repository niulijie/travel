package com.niulijie.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.KeyException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@SpringBootTest
public class JwsTest {

    /**
     * 生成带签名的jwt即jws
     * eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiLlvKDkuIkifQ.w1SHtgYr6cpADL2ZCD0mG0zhwxK9Nt6NJ9zUh9uQ85Q
     */
    @Test
    public void test01(){
        // 创建一个key，使用HS256算法
        SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        // 使用JJWT库创建jwt
        String jws = Jwts.builder().setSubject("张三").signWith(secretKey).compact();
        System.out.println("带有签名的jwt："+jws);

        // 旧签名，验证不通过会抛出io.jsonwebtoken.security.SignatureException异常
        // jws = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiLlvKDkuIkifQ.w1SHtgYr6cpADL2ZCD0mG0zhwxK9Nt6NJ9zUh9uQ85Q";

        // 验证jwt claim = payload
        Claims body = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(jws).getBody();
        String subject = body.getSubject();
        System.out.println("subject:"+subject);
    }

    /**
     * RSA算法支持
     * 使用私钥创建jwt，使用公钥验签
     * eyJhbGciOiJSUzI1NiJ9.eyJzdWIiOiLmnY7lm5sifQ.RFqq13lMBM-mpOpVrfZNgtq4O1HGcXHMlGYIXbs9PzXlhr6lVrggTRVw5Mq4DfuqPDHIjxqz4Zu5MoUX_DgTxNtaedZnzMcW_2S5qTXLjHoqs2qFpxIMy4bpbRGF6br3h6Wxp9ZPBedL53zRd0QZtLJqauhLrRbH9LjnYXDjbbaypnUeoyR_eO28TX7ivbvlLVGpyM1gWxOLl9ApwmFgcc21_IVx4GPWOiMFnuV9V1F1F_OcjIgi5UJWmhY1F9MuLr50q3avxbohepnDg87KqANTvL3DNA9wEWXerixpbG7SITq1YPoRZ6a52BEIGFdjsnzXIjRSpvgtOdisUCM0lA
      */
    @Test
    public void test02(){
        // 创建RSA使用的公私钥
        KeyPair keyPair = Keys.keyPairFor(SignatureAlgorithm.RS256);
        // 使用私钥创建jwt
        String jwt = Jwts.builder().setSubject("李四").signWith(keyPair.getPrivate()).compact();
        System.out.println("jwt:"+jwt);

        // 使用公钥验证jwt
        try {
            jwt = "eyJhbGciOiJSUzI1NiJ9.eyJzdWIiOiLmnY7lm5sifQ.RFqq13lMBM-mpOpVrfZNgtq4O1HGcXHMlGYIXbs9PzXlhr6lVrggTRVw5Mq4DfuqPDHIjxqz4Zu5MoUX_DgTxNtaedZnzMcW_2S5qTXLjHoqs2qFpxIMy4bpbRGF6br3h6Wxp9ZPBedL53zRd0QZtLJqauhLrRbH9LjnYXDjbbaypnUeoyR_eO28TX7ivbvlLVGpyM1gWxOLl9ApwmFgcc21_IVx4GPWOiMFnuV9V1F1F_OcjIgi5UJWmhY1F9MuLr50q3avxbohepnDg87KqANTvL3DNA9wEWXerixpbG7SITq1YPoRZ6a52BEIGFdjsnzXIjRSpvgtOdisUCM0lA";
            Claims body = Jwts.parserBuilder().setSigningKey(keyPair.getPublic()).build().parseClaimsJws(jwt).getBody();
            System.out.println("body:"+body.getSubject());
        } catch (SignatureException e) {
            e.printStackTrace();
            System.out.println("验证签名失败");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 设置公共payload内容 -- 无签名部分
     * eyJhbGciOiJub25lIn0.eyJzdWIiOiLlvKDkuIkiLCJhdWQiOiJ6aGFuZ3NhbiIsImV4cCI6MTY3MjczNjkwNywibmJmIjoxNjcyNzM1MTA3LCJpYXQiOjE2NzI3MzUxMDcsImp0aSI6Ijg0NzIzZTRjLTdkZjAtNDFkNC1iMWExLWU0YWQyZTAyYzYyNiJ9.
     */
    @Test
    public void test03(){
        String jwt = Jwts.builder()
                // Subject和Audience 一般设置一个就可以
                // 面向的用户
                .setSubject("张三")
                // 接受jwt的一方
                .setAudience("zhangsan")
                // 30分钟后过期
                .setExpiration(DateUtils.addMinutes(new Date(), 30))
                // 生效时间
                .setNotBefore(new Date())
                // 签发时间
                .setIssuedAt(new Date())
                // id
                .setId(UUID.randomUUID().toString())
                .compact();

        System.out.println("jwt:"+jwt);
    }

    /**
     * 自定义payload内容
     * eyJhbGciOiJub25lIn0.eyJyb2xlSWQiOjEwMDEsIm5hbWUiOiJsaXNpIiwicm9sZU5hbWUiOiJtYW5hZ2VyIiwidXNlcklkIjoiVTAwMSJ9.
     */
    @Test
    public void test04(){
        // 定义Private Payload
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("name", "lisi");
        map.put("roleId", 1001);
        map.put("roleName", "manager");
        map.put("userId", "U001");

        String jwt = Jwts.builder()
                .setExpiration(DateUtils.addMinutes(new Date(), 30))
                // 设置私有内容
                .setClaims(map)
                .compact();
        System.out.println("jwt:"+jwt);
    }

    /**
     * 使用自定义的key
     * eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJsaXNpIn0.2fazAa9iiSbrnDyTNc20JOPCXcTPgacGrGahsWM6wrA
     */
    @Test
    public void test05(){
        //String s = UUID.randomUUID().toString().replaceAll("-", "");
        //System.out.println("s:"+s+", s.length:"+s.length());

        //s:4636e15049fa45ada3e9b25217331181, s.length:32
        String key = "4636e15049fa45ada3e9b25217331181";
        SecretKey secretKey = Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8));

        // 创建jws
        String jws = Jwts.builder()
                .setSubject("lisi")
                // 签名方式
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
        System.out.println("jws:"+jws);
    }

    /**
     * 压缩jwt内容
     */
    @Test
    public void test06(){
        // 定义Private Payload
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("name", "lisi");
        map.put("roleId", 1001);
        map.put("roleName", "manager");
        map.put("userId", "U001");
        map.put("name1", "lisi");
        map.put("name2", "lisi");
        map.put("name3", "lisi");
        map.put("name4", "lisi");
        map.put("name5", "lisi");
        map.put("name6", "lisi");

        String jwt = Jwts.builder()
                .setExpiration(DateUtils.addMinutes(new Date(), 30))
                // 设置私有内容
                .setClaims(map)
                .compact();
        // jwt.length:229
        System.out.println("jwt:"+jwt+", jwt.length:"+jwt.length());

        // jwtDeflate.length:145
        String jwtDeflate = Jwts.builder()
                .setExpiration(DateUtils.addMinutes(new Date(), 30))
                // 设置私有内容
                .setClaims(map)
                // 设置压缩算法
                .compressWith(CompressionCodecs.DEFLATE)
                .compact();
        System.out.println("jwtDeflate:"+jwtDeflate+", jwtDeflate.length:"+jwtDeflate.length());

        String jwtZip = Jwts.builder()
                .setExpiration(DateUtils.addMinutes(new Date(), 30))
                // 设置私有内容
                .setClaims(map)
                // 设置压缩算法
                .compressWith(CompressionCodecs.GZIP)
                .compact();
        // jwtZip.length:162
        System.out.println("jwtZip:"+jwtDeflate+", jwtZip.length:"+jwtZip.length());
    }

    /**
     * 读取jwt
     */
    @Test
    public void test07(){
        // 创建自定义的key
        String key = UUID.randomUUID().toString().replaceAll("-", "");
        System.out.println("key:"+key);

        // 创建secretKey
        SecretKey secretKey = Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8));

        // 定义Private Payload
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("name", "lisi");
        map.put("roleId", 1001);
        map.put("roleName", "manager");
        map.put("userId", "U001");

        String jwt = Jwts.builder()
                // 设置私有内容 -- 会将主题部门的所有内容都覆盖一下
                .setClaims(map)
                .setSubject("张三")
                // 签名方式
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
        System.out.println("jwt:"+jwt);

        String jwt2 = Jwts.builder()
                .setSubject("张三")
                // 使用添加的方式添加私有属性，可以解决claim会覆盖所有属性内容问题
                .addClaims(map)
                // 签名方式
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();

        // 解析jwt
        // 1.获取Builder对象
        JwtParserBuilder jwtParserBuilder = Jwts.parserBuilder();
        // 2.设置key
        jwtParserBuilder.setSigningKey(secretKey);
        // 3.获取Parser
        JwtParser jwtParser = jwtParserBuilder.build();
        // 4.解析数据
        Jws<Claims> claimsJws = jwtParser.parseClaimsJws(jwt);
        // 5.获取数据
        Claims body = claimsJws.getBody();
        String subject = body.getSubject();
        String userId = (String) body.get("userId");
        Integer roleId = body.get("roleId", Integer.class);
        System.out.println("subject:"+subject+", userId:"+userId+", roleId:"+roleId);

        Claims body2 = jwtParser.parseClaimsJws(jwt2).getBody();
        String body2Subject = body2.getSubject();
        String userId2 = (String) body2.get("userId");
        System.out.println("body2Subject:"+body2Subject+", userId2:"+userId2);
    }

    /**
     * 异常
     */
    @Test
    public void test08(){
        // 创建自定义的key
        String key = "f3598dda27534d49a0eb6f27ee0f15b8";
        // 2.2 签名异常
        //String key = "qqf3598dda27534d49a0eb6f27ee0f15b8";
        SecretKey secretKey = Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8));
        // 2.1 过期数据
        String jwt = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlSWQiOjEwMDEsIm5hbWUiOiJsaXNpIiwicm9sZU5hbWUiOiJtYW5hZ2VyIiwidXNlcklkIjoiVTAwMSIsInN1YiI6IuW8oOS4iSIsImV4cCI6MTY3MjgwMzY4MH0.lRL4D2gt7V9hM57Lhevi7mVddAcCg_d6n_Y1WDtKIEI";
        System.out.println("jwt:"+jwt);

        // 解析jwt
        try {
            // 1.获取Builder对象
            JwtParserBuilder jwtParserBuilder = Jwts.parserBuilder();
            // 2.设置key
            jwtParserBuilder.setSigningKey(secretKey);
            // 3.获取Parser
            JwtParser jwtParser = jwtParserBuilder.build();
            // 4.解析数据
            Jws<Claims> claimsJws = jwtParser.parseClaimsJws(jwt);
            // 5.获取数据
            Claims body = claimsJws.getBody();
            String subject = body.getSubject();
            String userId = (String) body.get("userId");
            Integer roleId = body.get("roleId", Integer.class);
            System.out.println("subject:"+subject+", userId:"+userId+", roleId:"+roleId);
        } catch (ExpiredJwtException e) {
            System.out.println("============ExpiredJwtException=================");
            //e.printStackTrace();
        } catch (KeyException e) {
            System.out.println("============KeyException=================");
            //e.printStackTrace();
        } catch (SignatureException e) {
            System.out.println("============SignatureException=================");
            //e.printStackTrace();
        } catch (PrematureJwtException e) {
            System.out.println("============PrematureJwtException=================");
            //e.printStackTrace();
        } catch (CompressionException e) {
            System.out.println("============CompressionException=================");
            //e.printStackTrace();
        } catch (JwtException e) {
            System.out.println("============JwtException=================");
            //e.printStackTrace();
        } catch (Exception e) {
            System.out.println("============Exception=================");
            //e.printStackTrace();
        }
    }
}
