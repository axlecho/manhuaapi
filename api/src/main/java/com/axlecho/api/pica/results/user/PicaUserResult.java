package com.axlecho.api.pica.results.user;

import com.axlecho.api.pica.results.PicaResult;
import net.sf.json.JSONObject;

/**
 * 代表包含用户信息的返回结果
 */
public class PicaUserResult extends PicaResult {
    public PicaUserResult(JSONObject result) {
        super(result);
    }

    /**
     * 获取用户信息
     * <p>
     * 个人中心的资料请调用本方法
     * 如需更详细的信息，请使用getUserDetailInfo方法获取(对个人中心返回的结果无效)
     *
     * @return
     */
    public UserSimpleInfo getUserSimpleInfo() {
        if (getData().containsKey("user")) {
            return new UserSimpleInfo(getData().getJSONObject("user"));
        }
        return null;
    }

    /**
     * 获取用户信息
     * 本对象包含了更加详细的个人信息
     * 个人中心信息请勿调用本方法，请使用getUserSimpleInfo方法代替。
     *
     * @return
     */
    public UserDetailInfo getUserDetailInfo() {
        if (getData().containsKey("user")) {
            return new UserDetailInfo(getData().getJSONObject("user"));
        }
        return null;
    }


}
