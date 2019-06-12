package com.gemii.auth.service;

import javax.servlet.http.HttpServletRequest;

import com.gemii.auth.data.AuthToken;
import com.gemii.auth.data.ResultContent;
import com.gemii.auth.data.User;

/**
 * @ClassName UserService
 * @Description TODO
 * @Author wen.liu
 * @Date 2019-06-05 15:38
 **/
public interface UserService {

    ResultContent<AuthToken> login(User user);

    ResultContent<User> getCurrentUser(HttpServletRequest request);

    ResultContent<AuthToken> refreshToken(String refreshToken);
}
