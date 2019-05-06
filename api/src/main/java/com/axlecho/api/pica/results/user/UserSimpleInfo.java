package com.axlecho.api.pica.results.user;

import com.axlecho.api.pica.resources.Media;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 代表一个简单用户信息
 * 通常存在于以下几个地方：
 * 个人中心——自己的资料
 * 本子上传者
 * <p>
 * 注意：所有get方法不一定可用！具体返回值请参阅各个方法说明
 * 通常情况下，无法取得的对象返回null，基本数据类型返回-1或false
 *
 * @author FlanN
 * @date 2019/3/20
 */
public class UserSimpleInfo {


    protected JSONObject user;

    public UserSimpleInfo(JSONObject user) {
        this.user = user;
    }


    /**
     * 判断是否已签到
     * 注意：仅当个人资料是自己的资料时才能获取到！
     * 无法获取到别人的签到信息！！
     *
     * @return 获取失败固定返回false
     */
    public boolean isPunched() {
        if (user.containsKey("isPunched")) {
            return user.getBoolean("isPunched");
        }
        return false;
    }

    /**
     * 获取用户的内部ID（唯一）
     * 样例：582995d32cff435850358afa
     *
     * @return 不存在此字段返回null
     */
    public String getID() {
        if (user.containsKey("_id")) {
            return user.getString("_id");
        }
        return null;
    }


    /**
     * 获取用户设置的生日
     * （截止2019/01/28，用户暂无法自行更改）
     * 样例：1992-06-06T00:00:00.000Z
     */
    public String getBitrhday() {
        if (user.containsKey("birthday")) {
            return user.getString("birthday");
        }
        return null;
    }

    /**
     * 获取用户的注册邮箱
     */
    public String getEmail() {
        if (user.containsKey("email")) {
            return user.getString("email");
        }
        return null;
    }

    /**
     * 获取用户设置的性别
     * m或者f?有存在字段为bot的性别
     */
    public String getGender() {
        if (user.containsKey("gender")) {
            return user.getString("gender");
        }
        return null;
    }

    /**
     * 获取用户显示的名称
     * 用户对外显示的名称
     */
    public String getName() {
        if (user.containsKey("name")) {
            return user.getString("name");
        }
        return null;
    }


    /**
     * 获取用户的头衔
     * 样例：萌新
     */
    public String getTitle() {
        if (user.containsKey("title")) {
            return user.getString("title");
        }
        return null;
    }

    /**
     * 获取用户的个人签名
     */
    public String getSlogan() {
        if (user.containsKey("slogan")) {
            return user.getString("slogan");
        }
        return null;
    }

    /**
     * 未知，可能是管理员认证？
     * 普通用户是false，暂时没看到为true的
     */
    public boolean isVerified() {
        if (user.containsKey("ip")) {
            return user.getBoolean("verified");
        }
        return false;
    }

    /**
     * 获取的总经验值
     *
     * @return 当不存在此字段返回-1
     */
    public int getExp() {
        if (user.containsKey("exp")) {
            return user.getInt("exp");
        }
        return -1;
    }

    /**
     * 当前等级
     *
     * @return 当不存在此字段返回-1
     */
    public int getLevel() {
        if (user.containsKey("level")) {
            return user.getInt("level");
        }
        return -1;
    }

    /**
     * 未知，可能是个人资料创建的时间？
     * 这个时间会比activation_date早
     * 可能是刚注册，但尚未在邮箱内确认的时间?
     * 样例：2016-11-14T10:45:39.139Z
     */
    public String getCreated_at() {
        if (user.containsKey("created_at")) {
            return user.getString("created_at");
        }
        return null;
    }

    /**
     * 未知
     * 例:knight
     * 暂未发现其他字段
     * 注意:仅个人中心里自己的资料包含，其他用户没有这个字段（无法获取）
     *
     * @return 如果获取失败或为空则返回一个长度为0的List，不会返回null
     */
    public List<String> getCharacters() {
        List<String> result = new ArrayList<>();
        if (user.containsKey("characters")) {
            JSONArray arrJs = user.getJSONArray("characters");
            for (int i = 0; i < arrJs.size(); i++) {
                result.add(arrJs.getString(i));
            }
        }
        return result;
    }


    /**
     * 获取用户头像
     *
     * @return
     */
    public Media getAvatar() {
        if (user.containsKey("avatar")) {
            return new Media(user.getJSONObject("avatar"));
        }
        return null;
    }

    /**
     * 获取json
     *
     * @return
     */
    public JSONObject getJSON() {
        return user;
    }

    @Override
    public String toString() {
        return user.toString();
    }
}
