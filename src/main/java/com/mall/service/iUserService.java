package com.mall.service;

import com.mall.common.ServerResponse;
import com.mall.pojo.User;


public interface iUserService {
    ServerResponse<User> login(String username, String password);

    ServerResponse register(User user);

    ServerResponse selectQuestion(String username);

    ServerResponse checkAnswer(String username, String question, String answer);

    ServerResponse forgetRestPassword(String username, String passwordNew, String forgetToken);

    ServerResponse resetPassword(String passwordOld, String passwordNew, User user);

    ServerResponse updateInformation(User user);

    ServerResponse getInformation(Integer id);

    ServerResponse checkAdminRole(User user);
}
