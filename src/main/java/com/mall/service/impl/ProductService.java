package com.mall.service.impl;


import com.mall.common.ServerResponse;
import com.mall.dao.CategoryMapper;
import com.mall.dao.ProductMapper;
import com.mall.pojo.Category;
import com.mall.pojo.Product;
import com.mall.service.iProductService;
import com.mall.utils.DateUtil;
import com.mall.utils.PropertiesUtil;
import com.mall.vo.ProductDetailVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
public class ProductService implements iProductService {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private CategoryMapper categoryMapper;

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

    @Override
    public ServerResponse setSaleStatus(Integer productId, Integer status) {
        if (productId == null || status == null)
        {
            return ServerResponse.createByErrorMessage("设置参数出错");
        }
        Product product = new Product();
        product.setId(productId);
        product.setStatus(status);

        int rowCount = productMapper.updateByPrimaryKeySelective(product);
        if (rowCount > 0)
        {
            return ServerResponse.cerateBySuccessMessage("更新产品状态成功");
        }
        return ServerResponse.createByErrorMessage("更新产品状态失败");
    }


    //通过前端传过来的产品id来获取产品的详情，由于不需要全部展示，这里新建一个vo进行展示，将product的对应的属性传到vo里面，最后返回
    //设置产品的父分类id的时候先获取产品的分类id，存在就将父分类设为得到的，不存在父分类就设为0--根节点
    @Override
    public ServerResponse managerProductDetail(Integer productId) {
        if (productId == null)
        {
            ServerResponse.createByErrorMessage("参数错误");
        }

        Product product = productMapper.selectByPrimaryKey(productId);
        if (product == null)
        {
            return ServerResponse.createByErrorMessage("产品已下架");
        }

        //VO
        ProductDetailVo productDetailVo = assembleProductDetailVo(product);

        return ServerResponse.cerateBySuccess(productDetailVo);
    }

    private ProductDetailVo assembleProductDetailVo(Product product)
    {
        ProductDetailVo productDetailVo = new ProductDetailVo();
        productDetailVo.setId(product.getId());
        productDetailVo.setSubImage(product.getSubImages());
        productDetailVo.setSubtitle(product.getSubtitle());
        productDetailVo.setPrice(product.getPrice());
        productDetailVo.setMainImage(product.getMainImage());
        productDetailVo.setCategoryId(product.getCategoryId());
        productDetailVo.setDetail(product.getDetail());
        productDetailVo.setName(product.getName());
        productDetailVo.setStatus(product.getStatus());
        productDetailVo.setStock(product.getStock());


        productDetailVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix","http://img.comphca.com/"));
        Category category = categoryMapper.selectByPrimaryKey(product.getCategoryId());
        if (category == null)
        {
            productDetailVo.setParentCategoryId(0); //默认根节点
        }
        else
        {
            productDetailVo.setParentCategoryId(category.getParentId());
        }

        productDetailVo.setCreateTime(DateUtil.dateToStr(product.getCreateTime()));
        productDetailVo.setUpdateTime(DateUtil.dateToStr(product.getUpdateTime()));

        return productDetailVo;
    }
}
