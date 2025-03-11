package com.muybaby.food.bean;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

//统一响应结果
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Result<T> {
    private Integer code;    // HTTP状态码（如200、409）
    private String message;  // 提示信息
    private T data;          // 返回数据

    // 成功响应（无数据）
    public static <T> Result<T> success(String message) {
        Result<T> result = new Result<>();
        result.setCode(HttpStatus.OK.value());
        result.setMessage(message);
        return result;
    }

    // 错误响应
    public static <T> Result<T> error(HttpStatus status, String message) {
        Result<T> result = new Result<>();
        result.setCode(status.value());
        result.setMessage(message);
        return result;
    }

    // 成功响应（有数据）
// 修改所有静态方法增加泛型支持
    public static <T> Result<T> success(String message, T data) {
        Result<T> result = new Result<>();
        result.setCode(HttpStatus.OK.value());
        result.setMessage(message);
        result.setData(data);
        return result;
    }

    // 成功响应（有数据，无message）
    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setCode(HttpStatus.OK.value());
        result.setData(data);
        return result;
    }

}
