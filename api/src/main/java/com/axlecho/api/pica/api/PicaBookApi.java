package com.axlecho.api.pica.api;


import com.axlecho.api.pica.PicaHeader;
import com.axlecho.api.pica.exceptions.PicaException;
import com.axlecho.api.pica.resources.BookSimpleInfo;
import com.axlecho.api.pica.results.PicaActionResult;
import com.axlecho.api.pica.results.book.PicaBookDetailInfoResult;
import com.axlecho.api.pica.results.book.PicaBookMediaResult;
import com.axlecho.api.pica.results.book.PicaEpisodeInfoResult;
import com.axlecho.api.pica.utils.NetUtil;

import net.sf.json.JSONObject;

import java.io.IOException;

/**
 * 操作本子的API
 * @author FlanN
 */
public class PicaBookApi {


    private PicaHeader header;

    public PicaBookApi(PicaHeader header) {
        this.header = header;
    }

    /**
     * 获取本子详细信息
     *
     * @param bookID 本子ID
     * @return
     * @throws IOException   网络问题等
     * @throws PicaException 哔咔服务器返回错误信息
     * @throws Exception     其他异常
     */
    public PicaBookDetailInfoResult getBookDetailInfo(String bookID) throws Exception {
        header.setTargetURL("https://picaapi.picacomic.com/comics/" + bookID);
        header.setMethod(PicaHeader.Method.GET);
        JSONObject json = JSONObject.fromObject(NetUtil.getResult(header));
        PicaBookDetailInfoResult result = new PicaBookDetailInfoResult(json);
        if (result.hasError()) {
            throw new PicaException(result);
        }
        return result;
    }

    /**
     * 获取本子详细信息
     *
     * @param simpleInfo 本子简单信息
     * @return
     * @throws IOException   网络问题等
     * @throws PicaException 哔咔服务器返回错误信息
     * @throws Exception     其他异常
     */
    public PicaBookDetailInfoResult getBookDetailInfo(BookSimpleInfo simpleInfo) throws Exception {
        return getBookDetailInfo(simpleInfo.get_id());
    }


    /**
     * 获取本子所有章节的信息
     * 每个章节的标题，ID等
     *
     * @param bookID 本子的唯一ID
     * @param page   页码，可从本子信息里获取，一般来说40章节为1页，目前哔咔尚未看到有超过40章的本子
     * @return
     * @throws IOException   网络问题
     * @throws PicaException 哔咔服务器返回错误
     * @throws Exception     请求失败抛出
     */
    public PicaEpisodeInfoResult getEpisodeInfo(String bookID, int page) throws Exception {
        header.setTargetURL(String.format("https://picaapi.picacomic.com/comics/%s/eps?page=%d", bookID, page));
        header.setMethod(PicaHeader.Method.GET);
        JSONObject json = JSONObject.fromObject(NetUtil.getResult(header));
        PicaEpisodeInfoResult result = new PicaEpisodeInfoResult(json);
        if (result.hasError()) {
            throw new PicaException(result);
        }
        return result;
    }

    /**
     * 获取本子所有章节的信息
     * 每个章节的标题，ID等
     * 默认为获取第一页
     *
     * @param bookID 本子的唯一ID
     * @return
     * @throws IOException   网络问题
     * @throws PicaException 哔咔服务器返回错误
     *                       请求非法ID本子时将抛出 error:"1022"，message:"invalid id"
     * @throws Exception     请求失败抛出
     */
    public PicaEpisodeInfoResult getEpisodeInfo(String bookID) throws Exception {
        return getEpisodeInfo(bookID, 1);
    }


    /**
     * 获取本子的图片资源信息
     *
     * @param bookID    本子ID
     * @param episodeID 章节ID(即Order)
     * @param page      本子页码(目前一页限制为40张图);
     * @return
     * @throws IOException   网络问题
     * @throws PicaException 哔咔服务器返回错误
     *                       请求非法ID本子时将抛出 error:"1022"，message:"invalid id"
     *                       请求非法章节ID抛出 {"code":500,"error":"Cannot read property '_id' of null","message":"--","detail":":("}
     *                       请求非法页码(0及以下)抛出 {"code":400,"error":"1016","message":"invalid query","detail":":("}
     *                       请求超出范围的页码不会抛出异常，但getMedia()将返回空数组，不包含本子信息
     * @throws Exception     请求失败抛出
     */
    public PicaBookMediaResult getBookMedia(String bookID, int episodeID, int page) throws Exception {
        String url =
                String.format(
                        "https://picaapi.picacomic.com/comics/%s/order/%d/pages?page=%d"
                        , bookID
                        , episodeID
                        , page);

        header.setTargetURL(url);
        header.setMethod(PicaHeader.Method.GET);
        String data = NetUtil.getResult(header);
        JSONObject json = JSONObject.fromObject(data);
        PicaBookMediaResult result = new PicaBookMediaResult(json);
        if (result.hasError()) {
            throw new PicaException(result);
        }
        return result;
    }


    /**
     * 点赞本子
     *
     * @param bookID 本子的ID
     * @return
     * @throws IOException   网络问题
     * @throws PicaException 请求出现错误
     * @throws Exception     其他异常
     */
    public PicaActionResult setLike(String bookID) throws Exception {
        header.setTargetURL(String.format("https://picaapi.picacomic.com/comics/%s/like", bookID));
        header.setMethod(PicaHeader.Method.POST);
        return getActionResult(header);
    }

    /**
     * 收藏本子
     *
     * @param bookID 本子ID
     * @return
     * @throws IOException   网络问题
     * @throws PicaException 请求出现错误
     * @throws Exception     其他异常
     */
    public PicaActionResult setFavorite(String bookID) throws Exception {
        header.setTargetURL(String.format("https://picaapi.picacomic.com/comics/%s/favourite", bookID));
        header.setMethod(PicaHeader.Method.POST);
        return getActionResult(header);
    }


    private static PicaActionResult getActionResult(PicaHeader header) throws Exception {
        String data = NetUtil.getResult(header);
        JSONObject json = JSONObject.fromObject(data);
        PicaActionResult result = new PicaActionResult(json);
        if (result.hasError()) {
            throw new PicaException(result);
        }
        return result;
    }

}
