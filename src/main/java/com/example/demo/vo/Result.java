package com.example.demo.vo;

import lombok.*;

/**
 * @author Peter Fan
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {

    public static <T> Result<T> success(T data) {
        return new Result<>(true, data, null, null);
    }

    public static <T> Result<T> successWithoutData() {
        return new Result<>(true, null, null, null);
    }

    public static <T> Result<T> fail(String code, String msg) {
        return new Result<>(false, null, code, msg);
    }

    //服务处理是否成功
    private boolean success;

    //服务处理成功时，返回的数据
    private T data;

    //错误码。服务处理失败时（必须）返回
    private String code;

    //错误信息。服务处理失败时（可选）返回
    private String msg;


}
