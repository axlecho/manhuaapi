package com.axlecho.api.pica.results.book;

import com.axlecho.api.pica.resources.BookDetailInfo;
import com.axlecho.api.pica.results.PicaResult;
import net.sf.json.JSONObject;

/**
 * 代表了请求本子详细信息的返回结果
 */
public class PicaBookDetailInfoResult extends PicaResult {


    public PicaBookDetailInfoResult(JSONObject result) {
        super(result);
    }


    private JSONObject getComic() {
        return getData().getJSONObject("comic");
    }

    /**
     * 获取本子详细信息
     *
     * @return
     */
    public BookDetailInfo getBookDetailInfo() {
        return new BookDetailInfo(getComic());
    }

}
