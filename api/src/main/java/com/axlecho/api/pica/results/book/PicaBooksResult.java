package com.axlecho.api.pica.results.book;

import com.axlecho.api.pica.others.pages.PageInfo;
import com.axlecho.api.pica.others.pages.Pageable;
import com.axlecho.api.pica.resources.BookSimpleInfo;
import com.axlecho.api.pica.results.PicaResult;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 代表了哔咔本子列表的结果信息
 * 例如搜索结果，自己收藏的本子等
 *
 * @author FlanN
 * @date 2019/03/20
 */
public class PicaBooksResult extends PicaResult implements Pageable {


    public PicaBooksResult(JSONObject result) {
        super(result);
    }

    /**
     * 获取搜索结果的漫画信息
     *
     * @return 如果不存在返回空list
     */
    public List<BookSimpleInfo> getComics() {
        List<BookSimpleInfo> infoList = new ArrayList<>();
        if (getComicJson() != null) {
            JSONArray docArr = getComicJson().getJSONArray("docs");
            for (int i = 0; i < docArr.size(); i++) {
                infoList.add(new BookSimpleInfo(docArr.getJSONObject(i)));
            }
        }
        return infoList;
    }


    @Override
    public PageInfo getPageInfo() {
        //必须重新构造一个对象，否则会把原始对象中的键删除
        JSONObject pageJson = JSONObject.fromObject(getComicJson());
        //去除冗余信息
        pageJson.remove("docs");
        return new PageInfo(pageJson);
    }


    private JSONObject getComicJson() {
        if (getData().containsKey("comics")) {
            return getData().getJSONObject("comics");
        }
        return null;
    }

}
