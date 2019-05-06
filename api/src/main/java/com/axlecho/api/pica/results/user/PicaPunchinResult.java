package com.axlecho.api.pica.results.user;

import com.axlecho.api.pica.results.PicaResult;
import net.sf.json.JSONObject;

/**
 * 哔咔签到返回结果
 *
 * @author FlanN
 * @date 2019/03/20
 */
public class PicaPunchinResult extends PicaResult {

    public PicaPunchinResult(JSONObject result) {
        super(result);
    }

    /**
     * 获取签到状态
     *
     * @return 成功签到返回ok，签到失败返回fail,获取失败返回null
     */
    public String getStatus() {
        if (getRes() != null) {
            if (getRes().containsKey("status")) {
                return getRes().getString("status");
            }
        }
        return null;
    }

    /**
     * 简单的判断是否签到成功
     * @return 签到成功返回true，否则返回false
     */
    public boolean isSuccess(){
        return "ok".equals(getStatus());
    }

    /**
     * 获取最后签到日期
     * 如果签到失败，则无法获取此字段
     *
     * @return 例:2019-01-28
     */
    public String getPunchInLastDay() {
        if (getRes() != null) {
            if (getRes().containsKey("punchInLastDay")) {
                return getRes().getString("punchInLastDay");
            }
        }
        return null;
    }

    private JSONObject getRes() {
        if (getData().containsKey("res")) {
            return getData().getJSONObject("res");
        }
        return null;
    }

}
