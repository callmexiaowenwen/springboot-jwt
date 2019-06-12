package com.gemii.auth.service.impl;

import javax.servlet.http.HttpServletRequest;

import com.auth0.jwt.JWT;
import com.gemii.auth.constant.ResultCodeConstant;
import com.gemii.auth.dao.UserDao;
import com.gemii.auth.data.AuthToken;
import com.gemii.auth.data.ResultContent;
import com.gemii.auth.data.User;
import com.gemii.auth.exception.LoginFailedException;
import com.gemii.auth.service.RedisService;
import com.gemii.auth.service.UserService;
import com.gemii.auth.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName UserServiceImpl
 * @Description TODO
 * @Author wen.liu
 * @Date 2019-06-05 15:38
 **/
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;
    @Autowired
    private RedisService redisService;

    @Override
    public ResultContent<AuthToken> login(User user) {
        String username = user.getUsername();
        String password = user.getPassword();
        ResultContent<AuthToken> result = new ResultContent<>();
        //这里只是做个demo，并没有对password做加密
        User userByName = userDao.findUserByName(username);
        if (null == userByName) {
            throw new LoginFailedException(ResultCodeConstant.USER_ACCOUNT_WRONG, "this username may not be exists, please check this.");
        }
        if (!userByName.getPassword().equals(password)) {
            throw new LoginFailedException(ResultCodeConstant.USER_ACCOUNT_WRONG, "username or password wrong, please check this.");
        }

        String accessTokenRedisKey = ResultCodeConstant.ACCESS_TOKEN_REDIS_KEY + userByName.getUsername();
        String refreshTokenRedisKey = ResultCodeConstant.REFRESH_TOKEN_REDIS_KEY + userByName.getUsername();
        String accessToken = (String)redisService.getStr(accessTokenRedisKey);
        String refreshToken = (String)redisService.getStr(refreshTokenRedisKey);
        AuthToken authToken = new AuthToken();
        if (StringUtils.isNotBlank(accessToken)) {
            authToken.setAccessToken(accessToken);
            authToken.setRefreshToken(refreshToken);
            authToken.setExpiresIn(redisService.getExpire(accessTokenRedisKey));
        } else if (StringUtils.isNotBlank(refreshToken)){
            return this.refreshToken(refreshToken);
        } else {
            String accessTokenNew = JwtUtil.createJWT(null, ResultCodeConstant.ACCESS_TOKEN_EXPIRES, userByName);
            String refreshTokenNew = JwtUtil.createJWT(ResultCodeConstant.REFRESH_GRANT_TYPE, ResultCodeConstant.REFRESH_TOKEN_EXPIRES, userByName);
            redisService.setStr(accessTokenRedisKey, accessTokenNew, ResultCodeConstant.ACCESS_TOKEN_EXPIRES_SECOND);
            redisService.setStr(refreshTokenRedisKey, refreshTokenNew, ResultCodeConstant.REFRESH_TOKEN_EXPIRES_SECOND);
            authToken.setAccessToken(accessTokenNew);
            authToken.setRefreshToken(refreshTokenNew);
            authToken.setExpiresIn(ResultCodeConstant.ACCESS_TOKEN_EXPIRES_SECOND);
        }
        result.setResultCode(ResultCodeConstant.OPERATION_SUCCESS);
        result.setDescription("success");
        result.setData(authToken);
        return result;
    }

    @Override
    public ResultContent<User> getCurrentUser(HttpServletRequest request) {
        ResultContent<User> result = new ResultContent<>();
        String token = request.getHeader("Authorization");
        String userId = JWT.decode(token).getClaim("id").asString();
        User userById = userDao.findUserById(userId);
        result.setResultCode(ResultCodeConstant.OPERATION_SUCCESS);
        result.setDescription("success");
        result.setData(userById);
        return result;
    }

    @Override
    public ResultContent<AuthToken> refreshToken(String refreshToken) {
        ResultContent<AuthToken> result = new ResultContent<>();
        String username = JWT.decode(refreshToken).getClaim("username").asString();
        User userByName = userDao.findUserByName(username);
        String accessTokenRedisKey = ResultCodeConstant.ACCESS_TOKEN_REDIS_KEY + userByName.getUsername();
        String refreshTokenRedisKey = ResultCodeConstant.REFRESH_TOKEN_REDIS_KEY + userByName.getUsername();
        String accessToken = (String)redisService.getStr(accessTokenRedisKey);
        AuthToken authToken = new AuthToken();
        if (StringUtils.isNotBlank(accessToken)) {
            authToken.setAccessToken(accessToken);
            authToken.setRefreshToken(refreshToken);
            authToken.setExpiresIn(redisService.getExpire(accessTokenRedisKey));
        }  else {
            String accessTokenNew = JwtUtil.createJWT(null, ResultCodeConstant.ACCESS_TOKEN_EXPIRES, userByName);
            String refreshTokenNew = JwtUtil.createJWT(ResultCodeConstant.REFRESH_GRANT_TYPE, ResultCodeConstant.REFRESH_TOKEN_EXPIRES, userByName);
            redisService.setStr(accessTokenRedisKey, accessTokenNew, ResultCodeConstant.ACCESS_TOKEN_EXPIRES_SECOND);
            redisService.setStr(refreshTokenRedisKey, refreshTokenNew, ResultCodeConstant.REFRESH_TOKEN_EXPIRES_SECOND);
            authToken.setAccessToken(accessTokenNew);
            authToken.setRefreshToken(refreshTokenNew);
            authToken.setExpiresIn(ResultCodeConstant.ACCESS_TOKEN_EXPIRES_SECOND);
        }
        result.setResultCode(ResultCodeConstant.OPERATION_SUCCESS);
        result.setDescription("success");
        result.setData(authToken);
        return result;
    }

}
