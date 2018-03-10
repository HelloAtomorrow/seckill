package com.uestc.result;

public class Result<T> {

    private int code;

    private String msg;

    private T data;

    /**
     * 私有构造方法，这个设计并不希望new出Result对象，
     * 只能使用success和error两个静态方法得到对应的成功和失败对象。
     * @param data
     */
    private Result(T data) {
        this.code = 0;
        this.msg = "success";
        this.data = data;
    }

    private Result(CodeMsg codeMsg) {
        if (codeMsg == null) {
            return;
        }
        this.code = codeMsg.getCode();
        this.msg = codeMsg.getMsg();
    }

    /**
     * 成功时候的调用
     * @param data ： 成功时需要返回的数据
     * @return ：封装的Result对象
     */
    public static <T> Result<T> success(T data) {
        return new Result<T>(data);
    }

    /**
     * 失败时候的调用
     * @param codeMsg ： 错误的代码和错误的具体说明
     * @return ：封装的Result对象
     */
    public static <T> Result<T> error(CodeMsg codeMsg) {
        return new Result<T>(codeMsg);
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public T getData() {
        return data;
    }
}
