package com.gemii.auth.data;

import lombok.Data;

/**
 * @ClassName ResultContent
 * @Description TODO
 * @Author wen.liu
 * @Date 2019-06-05 16:04
 **/
@Data
public class ResultContent<T> {

    private String resultCode;

    private String description;

    private T data;

}
