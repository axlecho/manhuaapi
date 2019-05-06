package com.axlecho.api.pica.api;

import com.axlecho.api.pica.PicaHeader;
import com.axlecho.api.pica.exceptions.PicaException;
import com.axlecho.api.pica.results.PicaResult;
import com.axlecho.api.pica.results.category.PicaCategoryResult;
import com.axlecho.api.pica.results.book.PicaBooksResult;
import com.axlecho.api.pica.utils.NetUtil;
import com.axlecho.api.pica.utils.PicaRequestUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * 哔咔搜索类
 */
public class PicaSearchApi {

    private PicaHeader header;

    /**
     * 默认构造函数
     *
     * @param header 哔咔请求头，注意需要事先设定authorization字段，否则请求将会出错
     */
    public PicaSearchApi(PicaHeader header) {
        this.header = header;
    }

    /**
     * 获取热门搜索词
     *
     * @return 不存在搜索词返回空数组
     * @throws java.io.IOException 网络失败等
     * @throws PicaException       哔咔请求出错
     */
    public List<String> getHotKeywords() throws Exception {
        List<String> keylist = new ArrayList<>();
        header.setTargetURL("https://picaapi.picacomic.com/keywords");
        header.setMethod(PicaHeader.Method.GET);
        PicaResult result = PicaRequestUtil.getPicaResult(header);
        JSONObject data = result.getData();
        if (data == null) {
            throw new PicaException(result);
        }
        if (data.containsKey("keywords")) {
            JSONArray arr = data.getJSONArray("keywords");
            for (int i = 0; i < arr.size(); i++) {
                keylist.add(arr.getString(i));
            }
        }
        return keylist;
    }

    /**
     * 获取分类信息
     * 即搜索页的本子分类
     *
     * @return
     * @throws java.io.IOException 网络出错等
     * @throws PicaException       哔咔请求出错
     */
    public PicaCategoryResult getCategoryInfo() throws Exception {
        header.setTargetURL("https://picaapi.picacomic.com/categories");
        header.setMethod(PicaHeader.Method.GET);
        String content = NetUtil.getResult(header);

        PicaCategoryResult result = new PicaCategoryResult(JSONObject.fromObject(content));
        if (result.hasError()) {
            throw new PicaException(result);
        }
        return result;
    }

    /**
     * 搜索指定关键词，获取搜索结果列表
     * 搜索结果中包含页码
     *
     * @param keyword 关键词
     * @param page    页码，必须大于等于1，如果超出范围将无法获取结果，小于1则哔咔服务器返回异常
     * @return 搜索结果
     * @throws java.io.IOException 网络问题等
     * @throws PicaException       哔咔服务器返回错误
     * @throws Exception           其他异常
     */
    public PicaBooksResult search(String keyword, int page) throws Exception {
        String url =
                String.format("https://picaapi.picacomic.com/comics/search?page=%d&q=%s", page, URLEncoder.encode(keyword, "utf8"));

        header.setTargetURL(url);
        header.setMethod(PicaHeader.Method.GET);
        JSONObject json = JSONObject.fromObject(NetUtil.getResult(header));

        PicaBooksResult result = new PicaBooksResult(json);
        if (result.hasError()) {
            throw new PicaException(result);
        }
        return result;
    }

    /**
     * 搜索指定关键词，默认为搜索第一页
     * 搜索结果中包含页码
     *
     * @param keyword 关键词
     * @return 搜索结果
     * @throws java.io.IOException 网络问题等
     * @throws PicaException       哔咔服务器返回错误
     * @throws Exception           其他异常
     */
    public PicaBooksResult search(String keyword) throws Exception {
        return search(keyword, 1);
    }

}
