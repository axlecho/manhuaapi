package com.axlecho.api.pica.api;


import com.axlecho.api.pica.PicaHeader;
import com.axlecho.api.pica.exceptions.PicaException;
import com.axlecho.api.pica.results.PicaLoginResult;
import com.axlecho.api.pica.utils.NetUtil;
import net.sf.json.JSONObject;

/**
 * 代表哔咔漫画登录类
 */
public class PicaLoginApi {

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;


    public PicaLoginApi(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * 执行登录
     *
     * @return 登录成功返回header的authorization字段，用于大部分请求的鉴权，不需要重复登录！
     * @throws PicaException 服务器返回请求异常，例如密码错误等
     * @throws Exception 其他异常
     */
    public PicaLoginResult login() throws Exception {
        PicaHeader header = new PicaHeader();
        header.setTargetURL("https://picaapi.picacomic.com/auth/sign-in");
        header.setMethod(PicaHeader.Method.POST);
        //登录不加会报错
        header.setContent_Type("application/json; charset=UTF-8");

        JSONObject param = new JSONObject();
        param.put("email", username);
        param.put("password", password);

        PicaLoginResult result =
                new PicaLoginResult(
                        JSONObject.fromObject(
                                NetUtil.sendPost(
                                        header.getTargetURL(),
                                        header.getHeader(),
                                        param.toString())));
        //有错误就抛出异常
        if (result.hasError()) {
            throw new PicaException(result);
        }
        return result;
    }
}
