package com.gemii.auth.data;

import lombok.Data;

/**
 * @ClassName AuthToken
 * @Description TODO
 * @Author wen.liu
 * @Date 2019-06-05 15:48
 **/
@Data
public class AuthToken {

    private String accessToken;

    private String refreshToken;

    private Long expiresIn;

}
