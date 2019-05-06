package com.axlecho.api.pica.results;

import net.sf.json.JSONObject;

public class PicaLoginResult extends PicaResult {

    public PicaLoginResult(JSONObject result) {
        super(result);
    }

    /**
     * 获取登陆所需的token（请求头中的authorization字段）
     *
     * @return 如果获取失败返回null
     */
    public String getToken() {
        if (!super.hasError()) {
            JSONObject data = super.getData();
            if (data != null) {
                if (data.containsKey("token")) {
                    return data.getString("token");
                }
            }
        }
        return null;
    }

}
