package com.niulijie.ucenter.config;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.niulijie.ucenter.constant.AuthTokenConst;
import com.niulijie.ucenter.constant.SystemConstant;
import com.niulijie.ucenter.exception.CommonException;
import com.niulijie.ucenter.handler.AuthenticationEntryPointHandler;
import com.niulijie.ucenter.handler.ModifyHeaderRequestWrapper;
import com.niulijie.ucenter.mapper.AcAccountMapper;
import com.niulijie.ucenter.mapper.LoginMapper;
import com.niulijie.ucenter.pojo.entity.AcAccount;
import com.niulijie.ucenter.pojo.entity.UserAuthEntity;
import com.niulijie.ucenter.pojo.present.VO.login.LoginFirstVO;
import com.niulijie.ucenter.utils.AuthTokenUtil;
import com.niulijie.ucenter.utils.SaltUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

/**
 * @author dcs
 * @date 2022.7.4
 */

@Component
public class AuthTokenMD5Filter extends OncePerRequestFilter {
    private static final String[] excludedEndpoints = new String[]{
            "/user/noauth",
            "/actuator/*",
            "/member/count/status",
            "/member/getBatch",
            "/schedule/*",
            "/upgrade/*",
            "/member/search",
            "/member/search/number", //mdm设备注册使用
            "/member/search/name_id_card",  //mdm设备注册使用
            "/group/list",
            "/group/users",
            "/dept/top",
            "/member/simple_list",
            "/dept/get_batch",
            "/member/get",
            "/dept/list",
            "/member/list",
            "/menu/list"
    };
    @Autowired
    private AuthenticationEntryPointHandler authenticationEntryPointHandler;

    private AuthTokenUtil authTokenUtil;

    private String md5key;

    @Value("${auth.expiration:3}")
    private Long expiration;

    private UserAuthEntity userAuth = null;
    private String token = null;

    public AuthTokenMD5Filter(AcAccountMapper acAccountMapper, LoginMapper loginMapper, AuthTokenUtil authTokenUtil, @Value("${auth.account:}") Integer accountId, @Value("${auth.md5key:}") String md5key) {
        this.md5key = md5key;
        this.authTokenUtil = authTokenUtil;

        AcAccount acAccount = null;
        //md5设置的账号是否存在
        if (accountId != null) {
            acAccount = acAccountMapper.selectOne(Wrappers.<AcAccount>lambdaQuery()
                    .eq(AcAccount::getStatus, SystemConstant.NORMAL_STATUS)
                    .eq(AcAccount::getAccountId, accountId));
            if (ObjectUtils.isEmpty(acAccount)) {
                accountId = null;
            }
        }

        //如果没有设置md5规则，则取第一个管理员账号
        if (accountId == null && "".equals(md5key)){
            acAccount = acAccountMapper.selectOne(Wrappers.<AcAccount>lambdaQuery()
                    .eq(AcAccount::getStatus, SystemConstant.NORMAL_STATUS)
                    .orderByDesc(AcAccount::getAdmin)
                    .orderByAsc(AcAccount::getAccountId)
                    .last("limit 0,1"));
            if (!ObjectUtils.isEmpty(acAccount)) {
                accountId = acAccount.getAccountId();
            }
        }

        if (accountId != null) {
            LoginFirstVO loginFirstVO = loginMapper.loginFirst(accountId);
            this.userAuth = new UserAuthEntity(loginFirstVO);
            this.token = AuthTokenConst.TOKEN_PREFIX + authTokenUtil.generateToken(this.userAuth);
        }
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException, CommonException {

        try {
            String authorization = request.getHeader(AuthTokenConst.AUTHORIZATION);
            //如果没有配置md5规则，或者即使配置了，但是传入的是登录token，则让AuthTokenFilter验证
            if ("".equals(md5key) || (!StringUtils.isEmpty(authorization) && authorization.startsWith(AuthTokenConst.TOKEN_PREFIX))){
                filterChain.doFilter(request, response);
                return;
            }

            //如果配置了md5验证参数，则走md5验证逻辑
            if (!"".equals(md5key)) {

                String dateStampStr = request.getHeader("DateStamp");

                if (StringUtils.isEmpty(authorization)) {
                    throw new CommonException("md5token不存在");
                }

                if (StringUtils.isEmpty(dateStampStr)) {
                    throw new CommonException("DateStamp不存在");
                }

                if (!authorization.equals(SaltUtil.encryptionPwd(md5key, dateStampStr))) {
                    throw new CommonException("md5token非法");
                }

                Long dateStamp = Long.parseLong(dateStampStr);
                Long nowStamp = System.currentTimeMillis() / 1000;
                if (nowStamp - dateStamp > expiration || nowStamp - dateStamp < 0) {
                    throw new CommonException("md5token已经过期");
                }
            }

            //是否有设置账号
            if (this.token == null){
                throw new CommonException("没有设置账号或者所设置的账号不存在");
            }
        } catch (Exception e) {
            AuthenticationServiceException authenticationServiceException = new AuthenticationServiceException(e.getMessage());
            authenticationEntryPointHandler.commence(request, response, authenticationServiceException);
            return;
        }

        this.token = AuthTokenConst.TOKEN_PREFIX + authTokenUtil.generateToken(this.userAuth);

        ModifyHeaderRequestWrapper modifyHeaderRequestWrapper = new ModifyHeaderRequestWrapper(request);
        modifyHeaderRequestWrapper.putHeader(AuthTokenConst.AUTHORIZATION, this.token);

        filterChain.doFilter(modifyHeaderRequestWrapper, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        AntPathMatcher antPathMatcher = new AntPathMatcher();

        return Arrays.stream(excludedEndpoints)
                .noneMatch(e -> antPathMatcher.match(e, request.getServletPath()));
    }
}
