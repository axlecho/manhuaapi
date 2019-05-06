package com.axlecho.api.pica.results.announcement;

import com.axlecho.api.pica.resources.Media;
import net.sf.json.JSONObject;

public class Announcement {

    public JSONObject anno;

    Announcement(JSONObject json) {
        this.anno = json;
    }

    /**
     * 获取公告ID
     *
     * @return
     */
    public String get_id() {
        if (anno.containsKey("_id")) {
            return anno.getString("_id");
        }
        return null;
    }

    /**
     * 获取公告标题
     *
     * @return
     */
    public String getTitle() {
        if (anno.containsKey("title")) {
            return anno.getString("title");
        }
        return null;
    }

    /**
     * 获取内容
     *
     * @return
     */
    public String getContent() {
        if (anno.containsKey("content")) {
            return anno.getString("content");
        }
        return null;
    }

    /**
     * 获取创建时间
     * 样例:"2019-01-05T13:07:59.108Z"
     *
     * @return
     */
    public String getCreated_at() {
        if (anno.containsKey("created_at")) {
            return anno.getString("created_at");
        }
        return null;
    }

    /**
     * 获取资源文件
     *
     * @return
     */
    public Media getThumb() {
        if (anno.containsKey("thumb")) {
            return new Media(anno.getJSONObject("thumb"));
        }
        return null;
    }


    @Override
    public String toString() {
        return anno.toString();
    }
}
