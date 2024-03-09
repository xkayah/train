package com.mnus.ucenter.services;

import com.mnus.common.constance.MDCKey;
import com.mnus.common.enums.BaseErrorCodeEnum;
import com.mnus.common.exception.BizException;
import com.mnus.common.utils.IdGenUtil;
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
public class UcenterService {
    private static final Logger LOG = LoggerFactory.getLogger(UcenterService.class);

    @Resource
    private UserMapper userMapper;

    public Long count() {
        return userMapper.countByExample(null);
    }

    public Long sendCode(UserSendCodeReq req) {
        String mobile = req.getMobile();
        // 发送短信，查短信表，黑号发现
        String code = genAndCacheCode(mobile);
        LOG.info("[code]:{}", code);

        User userDB = selectOneUser(mobile);

        long uid = -1L;
        // notnull，说明是已经注册的用户
        if (!ObjectUtils.isEmpty(userDB)) {
            uid = userDB.getId();
        }
        // null，插入用户实体
        uid = IdGenUtil.nextId();
        User user = new User();
        user.setId(IdGenUtil.nextId());
        user.setMobile(mobile);
        userMapper.insert(user);

        MDC.put(MDCKey.UID, String.valueOf(uid));
        return uid;
    }

    public UserLoginResp login(LoginOrRegistryReq req) {
        String mobile = req.getMobile();
        String code = req.getCode();

        String codeCC = LRUCache.get(mobile + ":code");
        if (!code.equals(codeCC)) {
            throw new BizException(BaseErrorCodeEnum.SYSTEM_USER_EMAIL_OR_CODE_ERROR);
        }

        return new UserLoginResp(mobile, MDC.get(MDCKey.UID));
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
