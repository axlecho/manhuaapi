package com.axlecho.api.pica.results.init;

import com.axlecho.api.pica.results.PicaResult;
import net.sf.json.JSONObject;

/**
 * 代表进入哔咔时初始化的信息
 * 包含最新的app版本，更新内容，下载地址等
 */
public class PicaInitResult extends PicaResult {

    public PicaInitResult(JSONObject result) {
        super(result);
    }

    /**
     * 获取是否签到
     *
     * @return 获取失败固定返回false
     */
    public boolean isPunched() {
        if (getData().containsKey("isPunched")) {
            return getData().getBoolean("isPunched");
        }
        return false;
    }

    /**
     * 获取最新版本的app信息
     *
     * @return
     */
    public LatestApplication getLatestApplication() {
        if (getData().containsKey("latestApplication")) {
            return new LatestApplication(getData().getJSONObject("latestApplication"));
        }
        return null;
    }

    /**
     * 获取图片服务器
     * 返回样例： "https://s3.picacomic.com/static/"
     *
     * @return
     */
    public String getImageServer() {
        if (getData().containsKey("imageServer")) {
            return getData().getString("imageServer");
        }
        return null;
    }
}
