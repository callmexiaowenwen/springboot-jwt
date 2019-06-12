package com.gemii.auth.dao;

import com.gemii.auth.data.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @ClassName UserDao
 * @Description TODO
 * @Author wen.liu
 * @Date 2019-06-05 15:55
 **/
@Repository
public interface UserDao {

    User login(@Param("username") String username, @Param("password") String password);

    User findUserById(@Param("userId") String userId);

    User findUserByName(@Param("username") String username);
}
