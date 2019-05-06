package com.axlecho.api.pica.resources;

import com.axlecho.api.pica.results.user.UserSimpleInfo;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 代表每一本本子的详细信息
 *
 * @author FlanN
 */
public class BookDetailInfo extends BookSimpleInfo {

    private JSONObject comic;

    /**
     * 应当是comic键中的value
     *
     * @param comic
     */
    public BookDetailInfo(JSONObject comic) {
        super(comic);
        this.comic = comic;
    }


    /**
     * 获取本子的上传者
     * 仅以下字段有效:
     * id
     * 性别
     * 名称
     * 签名
     * title
     * verified
     * exp
     * level
     * characters(头衔？)
     * avatar
     *
     * @return
     */
    public UserSimpleInfo getCreator() {
        if (comic.containsKey("_creator")) {
            return new UserSimpleInfo(comic.getJSONObject("_creator"));
        }
        return null;
    }

    /**
     * 获取本子的描述信息
     *
     * @return
     */
    public String getDescription() {
        if (comic.containsKey("description")) {
            return comic.getString("description");
        }
        return null;
    }

    /**
     * 获取本子的汉化组信息
     *
     * @return
     */
    public String getChineseTeam() {
        if (comic.containsKey("chineseTeam")) {
            return comic.getString("chineseTeam");
        }
        return null;
    }

    /**
     * 获取本子的标签
     * 例："萌","蘿莉","全彩","貧乳"
     *
     * @return 如果不存在返回一个空的String数组
     */
    public List<String> getTags() {
        List<String> list = new ArrayList<>();
        if (comic.containsKey("tags")) {
            JSONArray arr = comic.getJSONArray("tags");
            String[] str = new String[arr.size()];
            for (int i = 0; i < arr.size(); i++) {
                list.add(arr.getString(i));
            }
        }
        return list;
    }

    /**
     * 获取更新的时间？
     * 例：2019-01-25T15:07:06.613Z
     *
     * @return
     */
    public String getUpdated_at() {
        if (comic.containsKey("updated_at")) {
            return comic.getString("updated_at");
        }
        return null;
    }

    /**
     * 获取本子的创建时间
     *
     * @return
     */
    public String getCreated_at() {
        if (comic.containsKey("created_at")) {
            return comic.getString("created_at");
        }
        return null;
    }

    /**
     * 获取总观看次数
     * 指的是所有人观看的次数
     *
     * @return 如果无法获取返回-1
     */
    public int getViewsCount() {
        if (comic.containsKey("viewsCount")) {
            return comic.getInt("viewsCount");
        }
        return -1;
    }

    /**
     * 自己是否收藏了此本子
     *
     * @return 如果无法获取返回false
     */
    public boolean isFavourite() {
        if (comic.containsKey("isFavourite")) {
            return comic.getBoolean("isFavourite");
        }
        return false;
    }

    /**
     * 自己是否点赞了此本子
     *
     * @return 如果无法获取固定返回false；
     */
    public boolean isLiked() {
        if (comic.containsKey("isLiked")) {
            return comic.getBoolean("isLiked");
        }
        return false;
    }

    /**
     * 获取评论数量
     *
     * @return 如果无法获取返回-1
     */
    public int getCommentsCount() {
        if (comic.containsKey("commentsCount")) {
            return comic.getInt("commentsCount");
        }
        return -1;
    }


    @Override
    public String toString() {
        return this.comic.toString();
    }
}
