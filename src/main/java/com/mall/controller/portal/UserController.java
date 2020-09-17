package com.mall.controller.portal;


import com.mall.common.Const;
import com.mall.common.ServerResponse;
import com.mall.pojo.User;
import com.mall.service.iUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/user/")
public class UserController {

    @Autowired
    private iUserService iUserService;

    @RequestMapping(value = "login",method = RequestMethod.POST )
    @ResponseBody
    public ServerResponse<User> login(String username, String password, HttpSession session){
        //登录的时候需要session进行存储会话
        ServerResponse<User> response = iUserService.login(username,password);
        if (response.isSuccess()){
            session.setAttribute(Const.CURRENT_USER, response.getData());
        }
        return response;
    }

    @RequestMapping(value = "loginout",method = RequestMethod.GET)
    @ResponseBody
    //登出直接把session删除就行了
    public ServerResponse loginout(HttpSession session){
        session.removeAttribute(Const.CURRENT_USER);
        return ServerResponse.cerateBySuccess();
    }

    @RequestMapping(value = "register",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse register(User user){
        return iUserService.register(user);
    }
}
