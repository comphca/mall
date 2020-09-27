package com.mall.controller.backend;


import com.mall.common.Const;
import com.mall.common.ResponseCode;
import com.mall.common.ServerResponse;
import com.mall.pojo.Product;
import com.mall.pojo.User;
import com.mall.service.iProductService;
import com.mall.service.iUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping(value = "manage/product")
public class ProductManageController {

    @Autowired
    private iUserService iUserService;

    @Autowired
    private iProductService iProductService;




    //增加商品
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse save(HttpSession session, Product product)
    {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null){
            return ServerResponse.createByErrorMessage(ResponseCode.NEED_LOGIN.getCode(),"未登录");
        }

        if (iUserService.checkAdminRole(user).isSuccess()){
            //校验管理员成功
            return iProductService.saveOrUpdateProduct(product);
        }else
        {
            return ServerResponse.createByErrorMessage("无权限操作");
        }
    }

    //通过产品id和产品状态设置商品属性，主要针对在售、下架
    @RequestMapping(value = "/set_sale_status", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse setSaleStatus(HttpSession session, Integer productId, Integer status)
    {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null)
        {
            return ServerResponse.createByErrorMessage(ResponseCode.NEED_LOGIN.getCode(),"未登录");
        }
        if (iUserService.checkAdminRole(user).isSuccess())
        {
            return iProductService.setSaleStatus(productId,status);
        }else
        {
            return ServerResponse.createByErrorMessage("无权限操作");
        }
    }

    //产品详情
    @RequestMapping(value = "detail", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse getDetail(HttpSession session, Integer productId)
    {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null)
        {
            return ServerResponse.createByErrorMessage(ResponseCode.NEED_LOGIN.getCode(),"未登录");
        }
        if (iUserService.checkAdminRole(user).isSuccess())
        {
            return iProductService.managerProductDetail(productId);
        }
        else
        {
            return ServerResponse.createByErrorMessage("没有权限操作");
        }
    }
}
