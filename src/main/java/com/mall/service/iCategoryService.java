package com.mall.service;

import com.mall.common.ServerResponse;

public interface iCategoryService {
    ServerResponse addCategory(String categoryName, Integer parentId);
}
