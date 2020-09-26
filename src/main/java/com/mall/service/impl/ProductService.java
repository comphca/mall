package com.mall.service.impl;


import com.mall.common.ServerResponse;
import com.mall.dao.ProductMapper;
import com.mall.pojo.Product;
import com.mall.service.iProductService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
public class ProductService implements iProductService {

    @Autowired
    private ProductMapper productMapper;

    @Override
    public ServerResponse saveOrUpdateProduct(Product product) {
        if (product != null)
        {
            if (StringUtils.isNotBlank(product.getSubImages()))
            {
                String[] subImageArray = product.getSubImages().split(",");
                if (subImageArray.length > 0)
                {
                    product.setMainImage(subImageArray[0]);
                }
            }

            //如果在数据库中存在id就说明这个操作是更新产品
            if (product.getId() != null)
            {
                int rowCount = productMapper.updateByPrimaryKey(product);
                if (rowCount > 0)
                {
                    return ServerResponse.cerateBySuccess("更新成功");
                }
                return ServerResponse.createByErrorMessage("更新失败");
            }else
            {
                int rowCount = productMapper.insert(product);
                if (rowCount > 0)
                {
                    return ServerResponse.cerateBySuccess("新增产品成功");
                }
                return ServerResponse.createByErrorMessage("新增失败");
            }
        }
        return ServerResponse.createByErrorMessage("更新或新增产品失败");
    }
}
