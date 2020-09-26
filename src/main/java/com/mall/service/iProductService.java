package com.mall.service;

import com.mall.common.ServerResponse;
import com.mall.pojo.Product;

public interface iProductService {
    ServerResponse saveOrUpdateProduct(Product product);
}
