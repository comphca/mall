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

    /*注册功能模块请求参数直接封装成一个user对象，这里前台请求参数为username、password、question、answer、email、phone，
    对于整个bean来说还剩role、创建时间和更新时间三个属性后台会自动设置，不设置就会报错

    时间类型在xml配置文件用函数直接生成
    */
    @RequestMapping(value = "register",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse register(User user){
//        System.out.println(user.getUsername());
//        System.out.println(user.getRole());
        return iUserService.register(user);
    }


    @RequestMapping(value = "get_user_info", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse get_user_info(HttpSession session){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if (user == null){
            return ServerResponse.createByErrorMessage("没有登录的用户");
        }
        return ServerResponse.cerateBySuccess(user);
    }


}
