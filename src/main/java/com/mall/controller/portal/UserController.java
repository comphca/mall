package com.mall.controller.portal;


import com.mall.common.Const;
import com.mall.common.ResponseCode;
import com.mall.common.ServerResponse;
import com.mall.pojo.User;
import com.mall.service.iUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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

    //这个地方将传递方式改为GET就是没办法进行判断，一直会报用户名不存在
    @RequestMapping(value = "forget_get_question", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse forgetGetQuestion(String username){
        return iUserService.selectQuestion(username);
    }

    @RequestMapping(value = "forget_check_answer", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse forgetCheckAnswer(String username, String question, String answer){
        return iUserService.checkAnswer(username,question,answer);
    }

    @RequestMapping(value = "forget_reset_password", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse forgetResetPassword(String username, String passwordNew, String forgetToken){
        return iUserService.forgetRestPassword(username, passwordNew, forgetToken);
    }


    @RequestMapping(value = "reset_password",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse resetPassword(HttpSession session, String passwordOld, String passwordNew){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null){
            return ServerResponse.createByErrorMessage("用户未登录");
        }

        return iUserService.resetPassword(passwordOld, passwordNew, user);
    }


    //修改用户信息  用户名不能更新
    @RequestMapping(value = "update_information", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse updateInformation(HttpSession session,User user){
        User currentUser = (User)session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null){
            return ServerResponse.createByErrorMessage("用户未登录");
        }
        user.setId(currentUser.getId());
        user.setUsername(currentUser.getUsername());
        //TODO 这里的泛型必须要加，不然下面的getData不能判定是User类型的数据，没办法使用对应的方法
        ServerResponse<User> response = iUserService.updateInformation(user);
        if (response.isSuccess()){
            //这里上面已经将当前用户的username设置到修改的用户信息里面去，为什么这里还要在设置一次？
            response.getData().setUsername(currentUser.getUsername());
            session.setAttribute(Const.CURRENT_USER, response.getData());
        }
        return response;
    }

    //获取用户信息
    @RequestMapping(value = "get_Information", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse get_information(HttpSession session){
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null){
            return ServerResponse.createByErrorMessage(ResponseCode.NEED_LOGIN.getCode(), "未登录，需要先登录");
        }
        return iUserService.getInformation(currentUser.getId());
    }





}
