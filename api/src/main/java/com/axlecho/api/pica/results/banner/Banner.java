package com.axlecho.api.pica.results.banner;

import com.axlecho.api.pica.resources.Media;
import net.sf.json.JSONObject;

public class Banner {

    private JSONObject bannerJson;

    Banner(JSONObject bannerJson) {
        this.bannerJson = bannerJson;
    }

    /**
     * 获取bannerID
     * 都是乱起的，没什么价值
     *
     * @return
     */
    public String get_id() {
        if (bannerJson.containsKey("_id")) {
            return bannerJson.getString("_id");
        }
        return null;
    }

    /**
     * 获取banner的标题
     *
     * @return
     */
    public String getTitle() {
        if (bannerJson.containsKey("title")) {
            return bannerJson.getString("title");
        }
        return null;
    }

    /**
     * 获取简短的描述
     *
     * @return
     */
    public String getShortDescription() {
        if (bannerJson.containsKey("shortDescription")) {
            return bannerJson.getString("shortDescription");
        }
        return null;
    }

    /**
     * 获取comic，此字段不一定存在
     *
     * @return
     */
    public String get_comic() {
        if (bannerJson.containsKey("_comic")) {
            return bannerJson.getString("_comic");
        }
        return null;
    }

    /**
     * 获取banner类型
     *
     * @return 已知：广告:ads，网页:web，游戏:game
     */
    public String getType() {
        if (bannerJson.containsKey("type")) {
            return bannerJson.getString("type");
        }
        return null;
    }

    /**
     * 获取连接
     * 都是直链
     *
     * @return
     */
    public String getLink() {
        if (bannerJson.containsKey("link")) {
            return bannerJson.getString("link");
        }
        return null;
    }

    /**
     * 获取资源文件
     *
     * @return
     */
    public Media getThumb() {
        if (bannerJson.containsKey("thumb")) {
            return new Media(bannerJson.getJSONObject("thumb"));
        }
        return null;
    }


    @Override
    public String toString() {
        return bannerJson.toString();
    }
}
