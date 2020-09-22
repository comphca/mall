package com.mall.controller.backend;


import com.mall.common.Const;
import com.mall.common.ResponseCode;
import com.mall.common.ServerResponse;
import com.mall.pojo.User;
import com.mall.service.iCategoryService;
import com.mall.service.iUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;


/**
 * 1.增加分类
 * 2.设置分类名称
 * 3.获取平级分类的分类
 * 4.获取大分类的子类
 * **/

@Controller
@RequestMapping("/manage/category")
public class CategoryMangerContorller {

    @Autowired
    private iUserService iUserService;
    @Autowired
    private iCategoryService iCategoryService;


    //1.先判断当前有没有登录；2.判断登录的是否为管理员；3.增加分类
    @RequestMapping(value = "add_category", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse addCategory(HttpSession session, String categoryName, @RequestParam(value = "parentId", defaultValue = "0") Integer parentId){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null){
            return ServerResponse.createByErrorMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录");
        }
        //校验用户是否为管理员--去封装一个方法
        if (iUserService.checkAdminRole(user).isSuccess()){
            return iCategoryService.addCategory(categoryName, parentId);
        }else {
            return ServerResponse.createByErrorMessage("无权限操作，当前不是管理员");
        }




    }

    @RequestMapping(value = "/set_category_name", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse setCategoryName(HttpSession session, Integer categoryId, String categoryName){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null){
            return ServerResponse.createByErrorMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录");
        }
        if (iUserService.checkAdminRole(user).isSuccess()){
            return iCategoryService.updateCategoryName(categoryName, categoryId);
        }else {
            return ServerResponse.createByErrorMessage("不是管理员，没有权限修改");
        }
    }


    //获取当前大分类的所有直接子类
    @RequestMapping(value = "get_catrgory", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse getCategory(HttpSession session, @RequestParam(value = "categoryId", defaultValue = "0") Integer categoryId){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null){
            return ServerResponse.createByErrorMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录");
        }
        if (iUserService.checkAdminRole(user).isSuccess()){
            return iCategoryService.getChildrenParallelCategory(categoryId);
        }else {
            return ServerResponse.createByErrorMessage("不是管理员，没有权限修改");
        }
    }




































}
