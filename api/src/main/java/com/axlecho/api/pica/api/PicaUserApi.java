package com.axlecho.api.pica.api;

import com.axlecho.api.pica.PicaHeader;
import com.axlecho.api.pica.exceptions.PicaException;
import com.axlecho.api.pica.results.book.PicaBooksResult;
import com.axlecho.api.pica.results.user.PicaPunchinResult;
import com.axlecho.api.pica.results.user.PicaUserResult;
import com.axlecho.api.pica.utils.NetUtil;
import net.sf.json.JSONObject;

/**
 * 用户相关的接口
 *
 * @author FlanN
 */
public class PicaUserApi {

    private PicaHeader header;

    public PicaUserApi(PicaHeader header) {
        this.header = header;
    }

    /**
     * 获取我的个人信息
     * 注意：如果想要获取更精确的信息，推荐使用 xxx方法，但信息中不会包含是否签到等字段
     *
     * @return 个人信息，请使用getUserSimpleInfo方法，否则某些方法将无法使用
     */
    public PicaUserResult getMyProfile() throws Exception {
        header.setTargetURL("https://picaapi.picacomic.com/users/profile");
        header.setMethod(PicaHeader.Method.GET);
        return getUserResult();
    }

    /**
     * 获取指定用户的个人信息
     *
     * @param userID 用户的个人ID，可以是自己的
     * @return
     * @throws net.sf.json.JSONException json解析错误，通常是网络问题导致
     * @throws java.io.IOException       网络问题
     * @throws PicaException             请求有误
     * @throws Exception                 其他异常
     */
    public PicaUserResult getUserProfile(String userID) throws Exception {
        header.setTargetURL(String.format("https://picaapi.picacomic.com/users/%s/profile", userID));
        header.setMethod(PicaHeader.Method.GET);
        return getUserResult();
    }


    /**
     * @param pageID
     * @return
     * @throws net.sf.json.JSONException json解析错误，通常是网络问题导致
     * @throws java.io.IOException       网络问题
     * @throws PicaException             请求有误
     * @throws Exception                 其他异常
     */
    public PicaBooksResult getMyFavoriteBooks(int pageID) throws Exception {
        header.setTargetURL(String.format("https://picaapi.picacomic.com/users/favourite?page=%d", pageID));
        header.setMethod(PicaHeader.Method.GET);
        String content = NetUtil.getResult(header);
        PicaBooksResult result = new PicaBooksResult(JSONObject.fromObject(content));
        if (result.hasError()) {
            throw new PicaException(result);
        }
        return result;
    }

    /**
     * 签到
     *
     * @return 签到结果
     * @throws net.sf.json.JSONException json解析错误，通常是网络问题导致
     * @throws java.io.IOException       网络问题
     * @throws PicaException             请求有误/重复签到，error:1023 message:too many requests
     * @throws Exception                 其他异常
     */
    public PicaPunchinResult punchin() throws Exception {
        header.setTargetURL("https://picaapi.picacomic.com/users/punch-in");
        header.setMethod(PicaHeader.Method.POST);
        String content = NetUtil.getResult(header);
        PicaPunchinResult result = new PicaPunchinResult(JSONObject.fromObject(content));
        if (result.hasError()) {
            throw new PicaException(result);
        }
        return result;
    }


    /**
     * 获取哔咔个人信息
     *
     * @return
     * @throws net.sf.json.JSONException json解析错误，通常是网络问题导致
     * @throws java.io.IOException       网络问题
     * @throws PicaException             请求有误
     * @throws Exception                 其他异常
     */
    private PicaUserResult getUserResult() throws Exception {
        String content = NetUtil.getResult(header);
        PicaUserResult result = new PicaUserResult(JSONObject.fromObject(content));
        if (result.hasError()) {
            throw new PicaException(result);
        }
        return result;
    }

}
