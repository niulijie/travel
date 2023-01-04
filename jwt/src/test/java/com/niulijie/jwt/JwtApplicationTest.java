package com.niulijie.jwt;

import org.apache.commons.codec.binary.Base64;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@SpringBootTest
public class JwtApplicationTest {

    /**
     * 测试方法不能有返回值，否则报错
     * eyJhbGciOiJIUzI1NiIsICJ0eXAiOiJKV1QifQ
     */
    //@Test
    public String getHeader(){
        String header = "{\"alg\":\"HS256\", \"typ\":\"JWT\"}";
        // 把header的json转为base64URL编码的字符串
        String base64URLSafeString = Base64.encodeBase64URLSafeString(header.getBytes(StandardCharsets.UTF_8));
        System.out.println("base64URLSafeString:"+base64URLSafeString);
        return base64URLSafeString;
    }

    /**
     * eyJzdWIiOiJsaXNpIiwgImlkIjoiMTAwMSIsICJyb2xlIjoiYWRtaW4ifQ
     */
    //@Test
    public String getPayload(){
        String payload = "{\"sub\":\"lisi\", \"id\":\"1001\", \"role\":\"admin\"}";
        // 把header的json转为base64URL编码的字符串
        String payload64 = Base64.encodeBase64URLSafeString(payload.getBytes(StandardCharsets.UTF_8));
        System.out.println("payload64:"+payload64);
        return payload64;
    }

    /**
     * 1.结构：Header(头部).Payload(负载).Signature(签名)
     * 2.Header:是一个json对象，存储元数据
     *      alg：是签名的算法名称(Algorithm), 默认是HMAC SHA256(写成HS256)；
     *      typ：属性表示这个令牌(token)的类型(type), JWT令牌统一写为JWT
     *      元数据的json对象使用base64URL编码，翻译为字符串
     * 3.Payload:负载,是一个json对象，存储传递的数据，数据分为Public和Private的。
     *      Public是JWT中规定的一些字段，可以自己选择使用
     *          iss(issuer):签发人
     *          exp(expiration time):过期时间
     *          sub(subject):该JWT所面向的用户
     *          aud(audience):受众，接收该JWT的一方
     *          nbf(Not Before):生效时间
     *          jat(Issued At):签发时间
     *          jti(JWT ID):编号
     *      Private是自己定义的字段
     * 4.Signature:签名。签名是对Header和 Payload两部分的签名，目的是防止数据被篡改
     *      HMAC SHA256(base64UrlEncode(header)+"."+base64UrlEncode(payload),secret)
     *      签名算法:先指定一个secret密钥，把base64URL的header，base64URL的payload 和secret密钥 使用HMAC SHA256生成签名字符串
     * eyJhbGciOiJIUzI1NiIsICJ0eXAiOiJKV1QifQ.eyJzdWIiOiJsaXNpIiwgImlkIjoiMTAwMSIsICJyb2xlIjoiYWRtaW4ifQ.a2CKhApNABpFTm2jJhNj_KopRIvzsP_PgUHeTMCpCuY
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     */
    @Test
    public void getSignature() throws NoSuchAlgorithmException, InvalidKeyException {
        String secret = UUID.randomUUID().toString().replaceAll("-", "");
        System.out.println("secret:"+secret+", length:"+secret.length());

        // 指定初始化的算法类型
        Mac mac = Mac.getInstance("HmacSHA256");
        // 创建安全的密钥对象
        SecretKeySpec spec = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        // 让Mac对象使用指定的密钥对象
        mac.init(spec);

        // 准备数据
        String data = getHeader() + "." +getPayload();
        // 生成签名值
        byte[] bytes = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
        // 把bytes转为String
        String safeString = Base64.encodeBase64URLSafeString(bytes);

        // 组成完整的jwt
        String jwt = data +"."+ safeString;
        System.out.println("jwt:"+jwt);

    }
}
