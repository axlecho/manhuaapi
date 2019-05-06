package com.axlecho.api.pica.results.book;

import com.axlecho.api.pica.others.pages.PageInfo;
import com.axlecho.api.pica.others.pages.Pageable;
import com.axlecho.api.pica.resources.Episode;
import com.axlecho.api.pica.resources.Media;
import com.axlecho.api.pica.results.PicaResult;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 代表指定本子的图片资源信息
 */
public class PicaBookMediaResult extends PicaResult implements Pageable {

    public PicaBookMediaResult(JSONObject result) {
        super(result);
    }

    /**
     * 获取图片资源信息
     *
     * @return 如果为空或无法获取返回空列表
     */
    public List<Media> getMedia() {
        List<Media> mediaList = new ArrayList<>();
        JSONObject pagesJson = getPagesJSONObject();
        if (pagesJson != null) {
            if (pagesJson.containsKey("docs")) {
                JSONArray docs = pagesJson.getJSONArray("docs");
                for (int i = 0; i < docs.size(); i++) {
                    //不是单纯的media，还有一层json
                    JSONObject media = docs.getJSONObject(i);
                    if (media.containsKey("media")) {
                        media = media.getJSONObject("media");
                    }
                    mediaList.add(new Media(media));
                }
            }
        }
        return mediaList;
    }

    /**
     * 获取当前章节的信息
     * 注意：截止2019/3/9，此方法返回的章节信息只包含章节的ID和title，获取其他信息将返回null
     *
     * @return
     */
    public Episode getEpisode() {
        JSONObject epi = getData();
        epi.remove("pages");
        return new Episode(epi);
    }


    /**
     * 获取子json
     *
     * @return
     */
    private JSONObject getPagesJSONObject() {
        if (getData().containsKey("pages")) {
            return getData().getJSONObject("pages");
        }
        return null;
    }

    /**
     * 获取本子的资源信息
     *
     * @return
     */
    @Override
    public PageInfo getPageInfo() {
        //必须重新构造对象，否则会移除掉原始json的值
        JSONObject pageJson = JSONObject.fromObject(getPagesJSONObject());
        if (pageJson != null && pageJson.containsKey("docs")) {
            pageJson.remove("docs");
        }
        return new PageInfo(pageJson);
    }
}
