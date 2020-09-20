package com.mall.service.impl;


import com.mall.common.Const;
import com.mall.common.ServerResponse;
import com.mall.common.TokenCache;
import com.mall.dao.UserMapper;
import com.mall.pojo.User;
import com.mall.service.iUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

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

    @Override
    public ServerResponse selectQuestion(String username) {
        int resultCount = userMapper.checkUsername(username);
        if (resultCount == 0){
            return ServerResponse.createByErrorMessage("用户名不存在");
        }

        String question = userMapper.selectQuestionByUsername(username);
        if (StringUtils.isNotBlank(question)){
            return ServerResponse.cerateBySuccessMessage(question);
        }
        return ServerResponse.createByErrorMessage("设置密码问题的时候没有设置，为空");
    }

    @Override
    public ServerResponse checkAnswer(String username, String question, String answer) {
        int count = userMapper.checkAnswer(username,question,answer);
        if (count > 0){
            //根据用户名、问题、答案三要素在数据库里面取到了记录，并且答案是对的上的
            String forgetToken = UUID.randomUUID().toString();
            TokenCache.setKey(TokenCache.TOKEN_PREFIX + username, forgetToken);
            return ServerResponse.cerateBySuccess(forgetToken);
        }
        return ServerResponse.createByErrorMessage("对应问题的答案是错误的");

    }

    @Override
    public ServerResponse forgetRestPassword(String username, String passwordNew, String forgetToken) {
        if (StringUtils.isBlank(forgetToken)){
            return ServerResponse.createByErrorMessage("参数错误，没有传递token");
        }

        int resultCount = userMapper.checkUsername(username);
        if (resultCount == 0){
            return ServerResponse.createByErrorMessage("用户名不存在");
        }
        String token = TokenCache.getKey(TokenCache.TOKEN_PREFIX + username);
        if (StringUtils.isBlank(token)){
            return ServerResponse.createByErrorMessage("token无效或者已过期");
        }
        if (StringUtils.equals(forgetToken, token)){
            int count = userMapper.updatePasswordByUsername(username,passwordNew);
            if (count > 0){
                return ServerResponse.cerateBySuccessMessage("密码修改成功");
            }
        }else {
            return ServerResponse.createByErrorMessage("token错误");
        }
        return ServerResponse.createByErrorMessage("修改密码失败");
    }


}
