package me.ooify.boogai.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Result {
    public static final int CODE_SUCCESS = 200;
    public static final int CODE_ERROR = 500;
    private int code;
    private String msg;

    private Object data;

    private String error;


    public Result setCode(int code) {
        this.code = code;
        return this;
    }

    public Result setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public Result setData(Object data) {
        this.data = data;
        return this;
    }


    public Result setError(String error) {
        this.error = error;
        return this;
    }

    public static Result ok() {
        return new Result(CODE_SUCCESS, "ok", null, null);
    }

    public static Result ok(String msg) {
        return new Result(CODE_SUCCESS, msg, null, null);
    }

    public static Result error() {
        return new Result(CODE_ERROR, "error", null, null);
    }

    public static Result error(String msg) {
        return new Result(CODE_ERROR, msg, null, null);
    }


}
