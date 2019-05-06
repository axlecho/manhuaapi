package com.axlecho.api.pica.results;

import net.sf.json.JSONObject;

/**
 * 所有返回结果类必须继承此类
 */
public class PicaResult {

    private JSONObject resultJson;

    public PicaResult(JSONObject result) {
        this.resultJson = result;
    }

    public JSONObject getResultJson() {
        return resultJson;
    }

    /**
     * 返回本次请求的HTTP状态码
     *
     * @return 成功返回200，可能返回400，401，403，404
     */
    public int getCode() {
        return resultJson.getInt("code");
    }

    /**
     * 判断本次请求是否成功
     * 注意:不一定准确
     *
     * @return 请求成功返回true，失败返回false
     */
    public boolean hasError() {
        return getCode() != 200;
    }

    /**
     * 获取消息
     *
     * @return 成功为success，请求失败为相关的错误信息，例如: invalid email or password ，如果不存在此字段返回null
     */
    public String getMessage() {
        if (resultJson.containsKey("message")) {
            return resultJson.getString("message");
        }
        return null;
    }

    /**
     * 获取详细信息
     * 不一定存在此字段
     * @return 目前仅当错误时会返回一个颜文字: ":("
     */
    public String getDetail() {
        if (resultJson.containsKey("detail")) {
            return resultJson.getString("detail");
        }
        return null;
    }

    /**
     * 获取错误信息
     *
     * @return
     */
    public String getError() {
        if (resultJson.containsKey("error")) {
            return resultJson.getString("error");
        }
        return null;
    }

    /**
     * 获取包含的数据
     * 仅当成功时才会返回数据
     *
     * @return 不存在此字段返回null
     */
    public JSONObject getData() {
        if (resultJson.containsKey("data")) {
            return resultJson.getJSONObject("data");
        }
        return null;
    }

    @Override
    public final String toString() {
        return this.resultJson.toString();
    }
}
