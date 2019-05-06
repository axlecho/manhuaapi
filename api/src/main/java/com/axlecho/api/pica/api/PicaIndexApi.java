package com.axlecho.api.pica.api;

import com.axlecho.api.pica.PicaHeader;
import com.axlecho.api.pica.exceptions.PicaException;
import com.axlecho.api.pica.results.announcement.PicaAnnouncementResult;
import com.axlecho.api.pica.results.banner.PicaBannerResult;
import com.axlecho.api.pica.results.init.PicaInitResult;
import com.axlecho.api.pica.utils.NetUtil;
import net.sf.json.JSONObject;

import java.io.IOException;

/**
 * 哔咔进入主页时初始化的api集合
 * 获取广告，获取最新版本等
 */
public class PicaIndexApi {


    private PicaHeader header;

    public PicaIndexApi(PicaHeader header) {
        this.header = header;
    }


    /**
     * 获取主页上方公告栏广告横幅
     *
     * @return
     * @throws PicaException       哔咔请求出错时抛出
     * @throws java.io.IOException 网络异常
     */
    public PicaBannerResult getBanner() throws PicaException, Exception {
        header.setTargetURL("https://picaapi.picacomic.com/banners");
        header.setMethod(PicaHeader.Method.GET);
        JSONObject data = JSONObject.fromObject(NetUtil.getResult(header));
        PicaBannerResult result = new PicaBannerResult(data);

        if (result.hasError()) {
            throw new PicaException(result);
        }

        return result;
    }

    /**
     * 获取公告
     *
     * @param page 公告页码
     * @return
     * @throws PicaException 哔咔请求错误时抛出
     * @throws IOException   网络错误等抛出
     */
    public PicaAnnouncementResult getAnnouncement(int page) throws Exception {
        header.setTargetURL("https://picaapi.picacomic.com/announcements?page=" + page);
        header.setMethod(PicaHeader.Method.GET);
        JSONObject data = JSONObject.fromObject(NetUtil.getResult(header));
        PicaAnnouncementResult result = new PicaAnnouncementResult(data);
        if (result.hasError()) {
            throw new PicaException(result);
        }
        return result;
    }

    /**
     * 获取初始化信息
     *
     * @param platform 平台，安卓为android,苹果为ios
     * @return
     */
    public PicaInitResult getInit(String platform) throws Exception {
        header.setTargetURL("https://picaapi.picacomic.com/init?platform=" + platform);
        header.setMethod(PicaHeader.Method.GET);
        JSONObject data = JSONObject.fromObject(NetUtil.getResult(header));
        PicaInitResult result = new PicaInitResult(data);
        if (result.hasError()) {
            throw new PicaException(result);
        }
        return result;
    }


}
