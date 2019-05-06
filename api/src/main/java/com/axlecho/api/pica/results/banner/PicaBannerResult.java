package com.axlecho.api.pica.results.banner;

import com.axlecho.api.pica.results.PicaResult;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PicaBannerResult extends PicaResult {

    public PicaBannerResult(JSONObject json) {
        super(json);
    }

    /**
     * 获取banner
     * 首页的横幅广告
     *
     * @return
     */
    public List<Banner> getBanners() {
        List<Banner> bnlist = new ArrayList<>();

        if (getData().containsKey("banners")) {
            JSONArray arr = getData().getJSONArray("banners");
            for (int i = 0; i < arr.size(); i++) {
                bnlist.add(new Banner(arr.getJSONObject(i)));
            }
        }
        return bnlist;
    }


}
