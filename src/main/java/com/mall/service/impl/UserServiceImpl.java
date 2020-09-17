package com.mall.service.impl;


import com.mall.common.Const;
import com.mall.common.ServerResponse;
import com.mall.dao.UserMapper;
import com.mall.pojo.User;
import com.mall.service.iUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("iUserService")
public class UserServiceImpl implements iUserService{

    @Autowired
    private UserMapper userMapper;


    @Override
    public ServerResponse<User> login(String username, String password) {
        int resultCount = userMapper.checkUsername(username);
        if (resultCount == 0){
            return ServerResponse.createByErrorMessage("用户名不存在");
        }

        User user = userMapper.selectLogin(username, password);

        if (user == null){
            return ServerResponse.createByErrorMessage("密码错误");
        }

        user.setPassword(StringUtils.EMPTY);

        return ServerResponse.cerateBySuccess("登录成功", user);
    }

    @Override
    public ServerResponse register(User user) {
        //前面传过来的属性service要进行校验是否能够进行注册
        int resultCount = userMapper.checkUsername(user.getUsername());
        if (resultCount > 0){
            return ServerResponse.createByErrorMessage("用户名已存在");
        }

        user.setRole(Const.Role.ROLE_ADMIN);

        //TODO MD5加密
        resultCount = userMapper.insert(user);
        if (resultCount == 0){
            return ServerResponse.createByErrorMessage("注册失败");
        }

        return ServerResponse.cerateBySuccessMessage("注册成功");
    }
}
