package com.mall.controller.backend;


import com.mall.common.Const;
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
            return ServerResponse.createByErrorMessage("未登录，不能增加商品");
        }

        if (iUserService.checkAdminRole(user).isSuccess()){
            //校验管理员成功
            return iProductService.saveOrUpdateProduct(product);
        }else
        {
            return ServerResponse.createByErrorMessage("无权限操作");
        }
    }
}
