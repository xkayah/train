package com.mnus.ucenter.services;

import com.mnus.ucenter.domain.User;
import com.mnus.ucenter.domain.UserExample;
import com.mnus.ucenter.mapper.UserMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author: <a href="https://github.com/xkayah">xkayah</a>
 * @date: 2024/3/8 16:58:29
 */
@Service
public class UcenterService {

    @Resource
    private UserMapper userMapper;

    public Long count() {
        return userMapper.countByExample(null);
    }

    public Long registry(String mobile) {
        UserExample userExample = new UserExample();
        userExample.createCriteria().andMobileEqualTo(mobile);
        List<User> list = userMapper.selectByExample(userExample);

        // 登录注册做成同一个接口时，可以将数据库实体类的id返回出去做校验
        if (!CollectionUtils.isEmpty(list)) {
            // return list.get(0).getId();
            throw new RuntimeException("手机号已注册");
        }

        long id = System.currentTimeMillis();
        User user = new User();
        user.setId(id);
        user.setMobile(mobile);
        userMapper.insert(user);

        return id;
    }
}
