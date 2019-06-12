package com.gemii.auth.controller;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.gemii.auth.annotation.CheckToken;
import com.gemii.auth.annotation.UserLoginToken;
import com.gemii.auth.constant.ResultCodeConstant;
import com.gemii.auth.data.AuthToken;
import com.gemii.auth.data.ResultContent;
import com.gemii.auth.data.User;
import com.gemii.auth.exception.LoginFailedException;
import com.gemii.auth.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName UserController
 * @Description TODO
 * @Author wen.liu
 * @Date 2019-06-05 15:36
 **/
@RestController
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 用户登录，并返回token
     *
     * @param user
     * @return
     */
    @RequestMapping(value = "/user/login", method = RequestMethod.POST)
    @UserLoginToken
    public ResultContent<AuthToken> login(@RequestBody User user) {
        log.info("[LW] login with params: {}", JSONObject.toJSONString(user));
        ResultContent<AuthToken> result = new ResultContent<>();
        try {
            result = userService.login(user);
        } catch (LoginFailedException e) {
            log.error(e.getMessage());
            result.setResultCode(e.getExceptionCode());
            result.setDescription(e.getExceptionDesc());
            return result;
        } catch (Exception e) {
            log.error(e.getMessage());
            result.setResultCode(ResultCodeConstant.OPERATION_FAILED);
            result.setDescription(e.getMessage());
            return result;
        }
        return result;
    }

    /**
     * 获取用户信息
     *
     * @param request
     * @return
     */
    @CheckToken
    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public ResultContent<User> getUserInfo(HttpServletRequest request) {
        ResultContent<User> result = new ResultContent<>();
        try {
            return userService.getCurrentUser(request);
        } catch (LoginFailedException e) {
            log.error(e.getMessage());
            result.setResultCode(e.getExceptionCode());
            result.setDescription(e.getExceptionDesc());
            return result;
        } catch (Exception e) {
            log.error(e.getMessage());
            result.setResultCode(ResultCodeConstant.OPERATION_FAILED);
            result.setDescription(e.getMessage());
            return result;
        }

    }

    @RequestMapping(value = "/refresh", method = RequestMethod.GET)
    public ResultContent<AuthToken> refreshToken(@RequestParam(value = "refreshToken") String refreshToken) {
        ResultContent<AuthToken> result = new ResultContent<>();
        try {
            return userService.refreshToken(refreshToken);
        } catch (LoginFailedException e) {
            log.error(e.getMessage());
            result.setResultCode(e.getExceptionCode());
            result.setDescription(e.getExceptionDesc());
            return result;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.setResultCode(ResultCodeConstant.OPERATION_FAILED);
            result.setDescription(e.getMessage());
            return result;
        }
    }

}
