package com.niulijie.ucenter.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.niulijie.ucenter.constant.SystemConstant;
import com.niulijie.ucenter.exception.CommonException;
import com.niulijie.ucenter.mapper.AcAccountMapper;
import com.niulijie.ucenter.mapper.LoginMapper;
import com.niulijie.ucenter.menus.ErrorEnum;
import com.niulijie.ucenter.pojo.entity.AcAccount;
import com.niulijie.ucenter.pojo.present.AO.login.LoginFistAO;
import com.niulijie.ucenter.pojo.present.AO.login.LoginSecondAO;
import com.niulijie.ucenter.pojo.present.VO.login.LoginFirstVO;
import com.niulijie.ucenter.pojo.present.VO.login.LoginSecondVO;
import com.niulijie.ucenter.service.LoginService;
import com.niulijie.ucenter.utils.SaltUtil;
import com.niulijie.ucenter.utils.UserIPUtil;
import com.niulijie.ucenter.utils.ValidatorUtils;
import com.sun.deploy.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private LoginMapper loginMapper;

    @Autowired
    private ServiceImpl<AcAccountMapper, AcAccount> serviceImpl;

    @Autowired
    private HttpServletRequest request;

    //客户端连续输入密码错误次数
    private HashMap<String, Integer> clientErrNum = new HashMap<>();

    //客户端被锁结束时间
    private HashMap<String, Long> clientErrTime = new HashMap<>();
    /**
     * 首次登录
     *
     * @param loginFistAO
     * @return
     */
    @Override
    public LoginFirstVO loginFirst(LoginFistAO loginFistAO) throws Exception {
        int loginType = 0;

        String accountName = loginFistAO.getAccountName();
        String password = loginFistAO.getPassword();

        String accessCode =loginFistAO.getAccessCode();

        if (accountName != null && accountName.length() > 0){
            //参数验证
            ValidatorUtils.validateEntity(loginFistAO, LoginFistAO.Login.class);
            //账号登录
            loginType = 1;
        }else if (accessCode != null && accessCode.length() > 0){
            //参数验证
            ValidatorUtils.validateEntity(loginFistAO, LoginFistAO.Access.class);
            //授权码登录
            loginType = 2;
        }else{
            throw new CommonException(ErrorEnum.PARAM_ERROR);
        }

        Integer accountId = null;
        if (loginType == 1) {
            //客户端的标记，这里用ip地址
            String clientId = UserIPUtil.getIpAddress(request);

            //客户端连续输入密码错误次数
            Integer errNum = 0;
            //客户端被锁结束时间
            Long errTime = 0L;
            if(clientErrNum.containsKey(clientId)){
                errNum = clientErrNum.get(clientId);
            }
            if(errNum == 3){
                errTime = clientErrTime.get(clientId);
            }
            if(errNum == 3){
                if(errTime > System.currentTimeMillis()){
                    throw new CommonException("已被锁定，不能登录");
                }else{
                    clientErrNum.remove(clientId);
                    clientErrTime.remove(clientId);
                    errNum = 0;
                    errTime = 0L;
                }
            }

            //检验账号是否存在，以及密码是否正确
            AcAccount acAccount = serviceImpl.getOne(Wrappers.<AcAccount>lambdaQuery()
                    .eq(AcAccount::getAccountName, accountName)
                    .eq(AcAccount::getStatus, SystemConstant.NORMAL_STATUS));
            if (ObjectUtils.isEmpty(acAccount) || !acAccount.getPassword().equals(SaltUtil.encryptionPwd(password, acAccount.getSalt()))) {
                errNum++;
                clientErrNum.put(clientId, errNum);
                if(errNum == 3){
                    clientErrTime.put(clientId, System.currentTimeMillis() + 1000*60*1);
                    throw new CommonException("连续登录失败达到3次，请1分钟之后再登录");
                }else {
                    throw new CommonException(ErrorEnum.LOGIN_ERRPR);
                }
            }

            clientErrNum.remove(clientId);
            clientErrTime.remove(clientId);

            accountId = acAccount.getAccountId();
        }else if(loginType == 2){
            accountId = loginMapper.getAccountIdByAccessCode(accessCode);
            if (accountId == null) {
                throw new CommonException(ErrorEnum.TOKEN_ERROR);
            }
        }

        // 这个地方处理逻辑有问题
        LoginFirstVO loginFirstVO = loginMapper.loginFirst(accountId);
        if (loginFirstVO == null) {
            throw new CommonException(ErrorEnum.ACCOUNT_NOT_EXIST);
        }

        return loginFirstVO;
    }

    @Override
    public LoginSecondVO loginSecond(LoginSecondAO loginSecondAO) throws Exception {
        Integer accountId = loginSecondAO.getAccountId();

        List<String> roleList = loginMapper.getRolelistByAccount(accountId);
        String roleIds = StringUtils.join(roleList, ",");

        LoginSecondVO loginSecondVO = loginMapper.loginSecond(accountId, roleIds);
        return loginSecondVO;
    }
}
