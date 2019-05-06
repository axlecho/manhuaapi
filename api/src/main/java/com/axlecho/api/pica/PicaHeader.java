package com.axlecho.api.pica;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

/**
 * 哔咔漫画标准请求头
 * <p>
 * 注意，调用方会修改请求头中的数据，例如调用方会修改目标url，请求方式等。
 * 如不愿意被调用方修改，请使用clone获取一个拷贝
 *
 * @author FlanN
 * @date 2019/3/4
 */
public class PicaHeader implements Cloneable {
    /**
     * 各个请求头
     */
    private Map<String, String> header = new TreeMap<>();

    /**
     * 客户端某个硬编码的值，用于计算请求签名
     */
    private String secret_key = null;

    /**
     * 请求的目标URL
     */
    private String targetURL = null;

    /**
     * 指定请求模式，默认为get，部分请求需要设定为post，调用方会自行设置，一般无需手动设置
     */
    private Method method = Method.GET;

    /**
     * 是否为debug模式
     * debug模式时，获取header map将不会自动填写时间戳、签名等数据；
     */
    public boolean isDebug = false;

    /**
     * 默认构造函数
     * 截至2019/3/4可用
     */
    public PicaHeader() {
        setSecret_key("~n}$S9$lGts=U)8zfL/R.PM9;4[3|@/CEsl~Kk!7?BYZ:BAa5zkkRBL7r|1/*Cr");
        setApi_Key("C69BAF41DA5ABD1FFEDC6D2FEA56B");
        setApp_Version("2.1.0.7");
        setApp_Channel(1);
        setBuild_Version(40);
        setAccept("application/vnd.picacomic.com.v1+json");
        setApp_Platform("android");
        setApp_UUID(UUID.randomUUID().toString());
        setUser_Agent("okhttp/3.8.1");
        setHost("picaapi.picacomic.com");
        //为了不给哔咔官方造成困扰，提供一个供官方辨识的请求头
        //如果不愿意添加此请求头，请使用removeCustomHeader(String key)方法移除
        setCustomHeader("sources", "PicaComic-api v2.0.0 beta;");
    }

    /**
     * 获取HTTP请求头
     * 获取后请尽快使用，避免时间戳过期
     *
     * @return
     */
    public Map<String, String> getHeader() throws Exception {
        if (!isDebug) {
            setNonce();
            setTime(System.currentTimeMillis() / 1000);
            setSignature(getSignature());
        }
        return header;
    }

    /**
     * 此方法将会清空整个header，可以自行设置参数
     */
    public void resetHeader() {
        header = new TreeMap<>();
    }


    /**
     * 添加自定义的请求头
     * 如果已经定义了，将会覆盖掉内置的请求头
     *
     * @param key
     * @param value
     */
    public void setCustomHeader(String key, String value) {
        header.put(key, value);
    }

    /**
     * 移除请求头
     * 一般不需要使用
     *
     * @param key
     */
    public void removeCustomHeader(String key) {
        header.remove(key);
    }

    /**
     * 设定请求的时间戳
     * 单位：秒
     * 注意：和 北京时间(GMT+8) 相差不得超过300秒，否则服务端会返回时间戳过期错误
     *
     * @param timestamp
     */
    public void setTime(long timestamp) {
        header.put("time", String.valueOf(timestamp));
    }

    /**
     * 返回请求的时间
     * 一般无需调用
     *
     * @return
     */
    public long getTime() {
        return Long.valueOf(header.get("time"));
    }

    /**
     * 获取Host
     * 默认为"picaapi.picacomic.com"
     *
     * @param host
     */
    public void setHost(String host) {
        header.put("Host", host);
    }

    public String getHost() {
        return header.get("Host");
    }

    /**
     * 设定UserAgent
     * 默认为 okhttp/3.8.1
     * 不设定可能导致部分请求被服务器拒绝
     *
     * @param userAgent
     */
    public void setUser_Agent(String userAgent) {
        header.put("User-Agent", userAgent);
    }

    public String getUser_Agent() {
        return header.get("User-Agent");
    }

    /**
     * 设定客户端的UUID
     * 目前没发现有啥作用
     * 会默认设置一个随机的UUID
     *
     * @param uuid
     */
    public void setApp_UUID(String uuid) {
        header.put("app-uuid", uuid);
    }

    public String getApp_UUID() {
        return header.get("app-uuid");
    }

    /**
     * 指定客户端平台
     * 未测试ios，默认为android
     * ios平台的值为ios
     *
     * @param platform
     */
    public void setApp_Platform(String platform) {
        header.put("app-platform", platform);
    }

    public String getApp_Platform() {
        return header.get("app-platform");
    }

    /**
     * 设置接受的类型
     *
     * @param accept
     */
    public void setAccept(String accept) {
        header.put("accept", accept);
    }

    public String getAccept() {
        return header.get("accept");
    }


    public String getApp_Channel() {
        return header.get("app-channel");
    }

    /**
     * 可能是请求的分流服务器？
     * 默认为1，其他未测试
     *
     * @param channel
     */
    public void setApp_Channel(String channel) {
        header.put("app-channel", channel);
    }

    /**
     * 可能是请求的分流服务器？
     * 默认为1，其他未测试
     *
     * @param channel
     */
    public void setApp_Channel(int channel) {
        setApp_Channel(String.valueOf(channel));
    }

    /**
     * 提交类型
     * 部分请求需要此字段
     * 例：
     * 登录：application/json; charset=UTF-8 无此字段将会报too many requests
     *
     * @param contentType
     */
    public void setContent_Type(String contentType) {
        header.put("Content-Type", contentType);
    }

    /**
     * 设定authorization
     * 通过登陆方法返回
     * 绝大部分请求需要此字段
     *
     * @param authorization
     */
    public void setAuthorization(String authorization) {
        header.put("authorization", authorization);
    }

    /**
     * 获取身份验证字段
     *
     * @return
     */
    public String getAuthorization() {
        return header.get("authorization");
    }


    /**
     * 设定客户端的api-key
     * 一般无需变动
     *
     * @param api_key
     */
    public void setApi_Key(String api_key) {
        header.put("api-key", api_key);
    }

    public String getApi_Key() {
        return header.get("api-key");
    }

    /**
     * 设定客户端哔咔版本
     *
     * @param version 例如2.1.0.7
     */
    public void setApp_Version(String version) {
        header.put("app-version", version);
    }

    public String getApp_Version() {
        return header.get("app-version");
    }

    public void setBuild_Version(String buildVer) {
        header.put("app-build-version", buildVer);
    }

    public void setBuild_Version(int buildVer) {
        setBuild_Version(String.valueOf(buildVer));
    }

    public String getBuild_Version() {
        return header.get("app-build-version");
    }


    public String getNonce() {
        return header.get("nonce");
    }

    /**
     * 添加噪声
     *
     * @param nonce
     */
    public void setNonce(String nonce) {
        header.put("nonce", nonce);
    }

    /**
     * 添加噪声
     * 将会生成一个随机的UUID
     */
    public void setNonce() {
        setNonce(UUID.randomUUID().toString().replace("-", ""));
    }


    /**
     * 设定secret_key
     * 用于计算请求的签名
     * 已在构造函数中设定默认值
     *
     * @param secret_key
     */
    public void setSecret_key(String secret_key) {
        this.secret_key = secret_key;
    }

    public String getSecret_key() {
        return secret_key;
    }


    /**
     * 获取请求签名
     *
     * @return
     */
    public String getSignature() throws InvalidKeyException, NoSuchAlgorithmException {
        if (getTargetURL() == null) {
            throw new NullPointerException("target URL can not be null");
        }
        String url = getTargetURL();
        url = url.replace("https://picaapi.picacomic.com/", "");
        url = url + getTime() + getNonce() + getMethod() + getApi_Key();
        url = url.toLowerCase();
        return HMACSHA256(url.getBytes(), getSecret_key().getBytes());
    }

    /**
     * 设置请求签名
     *
     * @param signature
     */
    public void setSignature(String signature) {
        header.put("signature", signature);
    }

    private static String HMACSHA256(byte[] data, byte[] key) throws NoSuchAlgorithmException, InvalidKeyException {
        SecretKeySpec signingKey = new SecretKeySpec(key, "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(signingKey);
        return byte2hex(mac.doFinal(data));
    }

    private static String byte2hex(byte[] b) {
        StringBuilder hs = new StringBuilder();
        String stmp;
        for (int n = 0; b != null && n < b.length; n++) {
            stmp = Integer.toHexString(b[n] & 0XFF);
            if (stmp.length() == 1) {
                hs.append('0');
            }
            hs.append(stmp);
        }
        return hs.toString();
    }


    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    /**
     * 代表请求的模式
     */
    public enum Method {
        /**
         * GET请求
         */
        GET,
        /**
         * POST请求
         */
        POST
    }

    /**
     * 获取请求的目标URL
     *
     * @return
     */
    public String getTargetURL() {
        return targetURL;
    }

    /**
     * 设定请求的目标URL
     *
     * @param targetURL
     */
    public void setTargetURL(String targetURL) {
        this.targetURL = targetURL;
    }


    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public void print() {
        System.out.println("===============================");
        for (String s : header.keySet()) {
            String value = header.get(s);
            System.out.println(s + "\t|" + value);
        }
        System.out.println("===============================");
    }
}
