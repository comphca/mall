package com.mall.service;

import com.mall.common.ServerResponse;
import com.mall.pojo.User;


public interface iUserService {
    ServerResponse<User> login(String username, String password);
}