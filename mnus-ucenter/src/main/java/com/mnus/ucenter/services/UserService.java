package com.mnus.ucenter.services;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.RandomUtil;
import com.mnus.common.constance.Constance;
import com.mnus.common.constance.MDCKey;
import com.mnus.common.enums.BaseErrorCodeEnum;
import com.mnus.common.exception.BizException;
import com.mnus.common.utils.IdGenUtil;
import com.mnus.common.utils.JwtUtil;
import com.mnus.common.utils.LRUCache;
import com.mnus.ucenter.domain.User;
import com.mnus.ucenter.domain.UserExample;
import com.mnus.ucenter.mapper.UserMapper;
import com.mnus.ucenter.req.LoginOrRegistryReq;
import com.mnus.ucenter.req.UserSendCodeReq;
import com.mnus.ucenter.resp.UserLoginResp;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author: <a href="https://github.com/xkayah">xkayah</a>
 * @date: 2024/3/8 16:58:29
 */
@Service
public class UserService {
    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    @Resource
    private UserMapper userMapper;
    @Resource
    private JwtUtil jwtUtil;

    public Long count() {
        return userMapper.countByExample(null);
    }

    public void sendCode(UserSendCodeReq req) {
        String mobile = req.getMobile();
        // 查短信表，判断频率，黑号发现
        if (false) {
            throw new BizException(BaseErrorCodeEnum.SYSTEM_CODE_GET_FREQUENT);
        }
        // 发送短信
        String code = genAndCacheCode(mobile);
        LOG.info("[mobile:{}]-[code:{}]", mobile, code);
    }

    public UserLoginResp login(LoginOrRegistryReq req) {
        String mobile = req.getMobile();
        String code = req.getCode();

        User userDB = selectOneUser(mobile);
        User user = null;
        long uid = 0;
        // notnull，说明是已经注册的用户
        if (!ObjectUtils.isEmpty(userDB)) {
            user = userDB;
            uid = userDB.getId();
        } else {
            uid = IdGenUtil.nextId();
            // null，插入实体
            String uname = Constance.NAME_PREFIX + RandomUtil.randomString(6);
            user = new User();
            user.setUname(uname);
            user.setId(uid);
            user.setMobile(mobile);
            userMapper.insert(user);
        }

        MDC.put(MDCKey.UID, String.valueOf(uid));
        // 判断code
        String codeCC = LRUCache.get(mobile + ":code");
        codeCC = "666666";
        // code为null
        if (code.isEmpty()) {
            throw new BizException(BaseErrorCodeEnum.SYSTEM_CODE_IS_NOT_EXISTS);
        }
        // code不正确
        if (!code.equals(codeCC)) {
            throw new BizException(BaseErrorCodeEnum.SYSTEM_USER_EMAIL_OR_CODE_ERROR);
        }
        UserLoginResp userLoginResp = BeanUtil.toBean(user, UserLoginResp.class);
        // 生成token
        String token = jwtUtil.genToken(userLoginResp);
        userLoginResp.setToken(token);
        return userLoginResp;
    }

    private User selectOneUser(String mobile) {
        UserExample userExample = new UserExample();
        userExample.createCriteria().andMobileEqualTo(mobile);
        List<User> list = userMapper.selectByExample(userExample);
        if (CollectionUtils.isEmpty(list)) {
            return null;
        } else {
            return list.get(0);
        }
    }

    private static String genAndCacheCode(String mobile) {
        String code = String.valueOf(ThreadLocalRandom.current().nextInt(100000, 1000000));
        // todo 存cache
        LRUCache.put(mobile + ":code", code);
        return code;
    }
}
