package com.gemii.auth.interceptor;

import java.lang.reflect.Method;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.gemii.auth.annotation.CheckToken;
import com.gemii.auth.annotation.UserLoginToken;
import com.gemii.auth.constant.ResultCodeConstant;
import com.gemii.auth.dao.UserDao;
import com.gemii.auth.data.User;
import com.gemii.auth.exception.LoginFailedException;
import com.gemii.auth.utils.JwtUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * @ClassName AuthenticationInterceptor
 * @Description TODO
 * @Author wen.liu
 * @Date 2019-06-10 16:16
 **/
public class AuthenticationInterceptor implements HandlerInterceptor {

    @Autowired
    private UserDao userDao;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        String token = httpServletRequest.getHeader("Authorization");
        // 不是映射到方法直接返回
        if (!(o instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) o;
        Method method = handlerMethod.getMethod();
        if (method.isAnnotationPresent(UserLoginToken.class)) {
            UserLoginToken userLoginToken = method.getAnnotation(UserLoginToken.class);
            if (userLoginToken.required()) {
                return true;
            }
        }
        if (method.isAnnotationPresent(CheckToken.class)) {
            CheckToken checkToken = method.getAnnotation(CheckToken.class);
            if (checkToken.required()) {
                if (null == token) {
                    throw new LoginFailedException(ResultCodeConstant.NO_PERMISSION, "miss token.");
                }
                String userId;
                try {
                    userId = JWT.decode(token).getClaim("id").asString();
                    String grantType = JWT.decode(token).getClaim("grantType").asString();
                    if (StringUtils.isNotBlank(grantType)) {
                        throw new LoginFailedException(ResultCodeConstant.NO_PERMISSION, "can't use refreshToken.");
                    }
                } catch (JWTDecodeException e) {
                    throw new LoginFailedException(ResultCodeConstant.NO_PERMISSION, "maybe exist exception.");
                }
                User user = userDao.findUserById(userId);
                if (null == user) {
                    throw new LoginFailedException(ResultCodeConstant.NO_PERMISSION, "this user may not be exists.");
                }
                Boolean verify = JwtUtil.isVerify(token, user);
                if (!verify) {
                    throw new LoginFailedException(ResultCodeConstant.NO_PERMISSION, "this request may be illegal");
                }
                return true;
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }

}
