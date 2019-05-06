package com.axlecho.api.pica.exceptions;

import com.axlecho.api.pica.results.PicaResult;

/**
 * 代表哔咔所有请求的异常信息
 *
 * @author FlanN
 */
public class PicaException extends RuntimeException {
    private PicaResult result;


    public PicaException(PicaResult result) {
        this.result = result;
    }

    /**
     * 获取错误信息的汉化版
     *
     * @return
     */
    public String getErrorMessage() {
        switch (result.getError()) {
            case "1005":
                return "身份验证失败:未验证身份";
            case "1029":
                return "time is not synchronize/客户端与北京时间不得相差300秒以上";
            case "1023":
                return "too many requests / 请求过快/请求包含过多数据";
            default:
                return result.getMessage();
        }
    }

    /**
     * 获取请求结果
     * @return
     */
    public PicaResult getResult() {
        return result;
    }

    /**
     * 打印堆栈
     */
    @Override
    public void printStackTrace() {
        System.err.println("请求失败: " + getErrorMessage());
        printPicaResult();
        super.printStackTrace();
    }

    /**
     * 打印服务端返回的结果
     */
    private void printPicaResult() {
        System.err.println(result.getResultJson().toString());
    }
}
