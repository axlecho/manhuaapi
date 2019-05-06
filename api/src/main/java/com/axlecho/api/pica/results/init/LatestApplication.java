package com.axlecho.api.pica.results.init;

import com.axlecho.api.pica.resources.Media;
import net.sf.json.JSONObject;

/**
 * 更新信息
 * 注意：ios端不包含apk字段
 *
 * @author FlanN
 */
public class LatestApplication {

    private JSONObject latestJson;

    public LatestApplication(JSONObject latestJson) {
        this.latestJson = latestJson;
    }


    public String get_id() {
        if (latestJson.containsKey("_id")) {
            return latestJson.getString("_id");
        }
        return null;
    }

    /**
     * 获取最新版本
     *
     * @return
     */
    public String getVersion() {
        if (latestJson.containsKey("version")) {
            return latestJson.getString("version");
        }
        return null;
    }

    /**
     * 获取更新信息
     *
     * @return
     */
    public String getUpdateContent() {
        if (latestJson.containsKey("updateContent")) {
            return latestJson.getString("updateContent");
        }
        return null;
    }

    /**
     * 获取安装包下载地址
     * 可能是外链
     *
     * @return
     */
    public String getDownloadURL() {
        if (latestJson.containsKey("downloadUrl")) {
            return latestJson.getString("downloadUrl");
        }
        return null;
    }

    public String getUpdated_at() {
        if (latestJson.containsKey("updated_at")) {
            return latestJson.getString("updated_at");
        }
        return null;
    }

    public String getCreated_at() {
        if (latestJson.containsKey("created_at")) {
            return latestJson.getString("created_at");
        }
        return null;
    }

    /**
     * 获取apk信息
     * 包括下载地址（和downloadUrl不一致，是从pica的服务器上下载的）
     * 不一定是直链
     * 注意：如果请求的是IOS版本的信息则不包含此字段
     * @return
     */
    public Media getApk() {
        if (latestJson.containsKey("apk")) {
            return new Media(latestJson.getJSONObject("apk"));
        }
        return null;
    }

    @Override
    public String toString() {
        return this.latestJson.toString();
    }
}
