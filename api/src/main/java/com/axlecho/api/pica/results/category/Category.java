package com.axlecho.api.pica.results.category;


import com.axlecho.api.pica.resources.Media;
import net.sf.json.JSONObject;

public class Category {
    private JSONObject cateJson;

    public Category(JSONObject category) {
        this.cateJson = category;
    }

    public String get_id() {
        if (cateJson.containsKey("_id")) {
            return cateJson.getString("_id");
        }
        return null;
    }

    public String getTitle() {
        if (cateJson.containsKey("title")) {
            return cateJson.getString("title");
        }
        return null;
    }

    public String getDescription() {
        if (cateJson.containsKey("description")) {
            return cateJson.getString("description");
        }
        return null;
    }

    public Media getThumb() {
        if (cateJson.containsKey("thumb")) {
            return new Media(cateJson.getJSONObject("thumb"));
        }
        return null;
    }


    @Override
    public String toString() {
        return cateJson.toString();
    }
}
