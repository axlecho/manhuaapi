package com.axlecho.api.pica.results.user;

import net.sf.json.JSONObject;

/**
 * 代表用户的详细信息
 *
 * @author FlanN
 * @date 2019/3/20
 */
public class UserDetailInfo extends UserSimpleInfo {

    public UserDetailInfo(JSONObject user) {
        super(user);
    }


    /**
     * 获取用户的密码（已加密）
     *
     * @return 样例：$2a$08$KDFYiXdfLW6tX8hFSOT.2ubCw5qvNzCqHCEdNigCvD1vPfrQQ9vsy
     */
    public String getPassword() {
        if (user.containsKey("password")) {
            return user.getString("password");
        }
        return null;
    }

    /**
     * 未知
     *
     * @return 样例：8aeff762-aff9-4aed-8353-4227df29a96f
     */
    public String getActivation_code() {
        if (user.containsKey("activation_code")) {
            return user.getString("activation_code");
        }
        return null;
    }

    /**
     * 可能是激活账户的时间
     *
     * @return 样例：2016-11-24T01:49:16.255Z
     */
    public String getActivation_date() {
        if (user.containsKey("activation_date")) {
            return user.getString("activation_date");
        }
        return null;
    }

    /**
     * 最后登录时间
     *
     * @return 样例：2018-12-19T02:54:06.347Z
     */
    public String getLast_login_date() {
        if (user.containsKey("last_login_date")) {
            return user.getString("last_login_date");
        }
        return null;
    }

    /**
     * 用户的IP地址
     * 尚不确定是注册IP还是最后登录的IP
     *
     * @return 样例: 123.45.67.89
     */
    public String getIP() {
        if (user.containsKey("ip")) {
            return user.getString("ip");
        }
        return null;
    }


    /**
     * 未知，或许是个人资料更新的时间？
     *
     * @return 样例：2019-01-27T14:21:36.891Z
     */
    public String getUpdated_at() {
        if (user.containsKey("updated_at")) {
            return user.getString("updated_at");
        }
        return null;
    }

    /**
     * 未知，可能是激活邮件发送次数？
     *
     * @return 似乎都是0
     */
    public int getResendCount() {
        if (user.containsKey("resendCount")) {
            return user.getInt("resendCount");
        }
        return -1;
    }

    /**
     * 未知，可能是忘记密码的次数？
     *
     * @return 似乎都是0
     */
    public int getForgotCount() {
        if (user.containsKey("forgotCount")) {
            return user.getInt("forgotCount");
        }
        return -1;
    }

    /**
     * 似乎是用户头像的特效框架
     *
     * @return 返回样例：https://www.picacomic.com/characters/frame_knight_500_999.png?r=3
     */
    public String getCharacter() {
        if (user.containsKey("character")) {
            return user.getString("character");
        }
        return null;
    }
}
