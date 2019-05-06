package com.axlecho.api.pica.others.pages;

import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 代表一个可翻页的页面对象
 * 可获取以下信息：
 * <p>
 * object总数
 * 单页限制数
 * 当前页码
 * 总页码
 */
public class PageInfo {

    private JSONObject data;

    /**
     * 构造函数
     *
     * @param json 必须包含以下几个字段: total,limit,page,pages
     */
    public PageInfo(JSONObject json) {
        List<String> field = new ArrayList<>();
        field.add("total");
        field.add("limit");
        field.add("page");
        field.add("pages");

        for (String s : field) {
            if (!json.containsKey(s)) {
                throw new IllegalArgumentException("非法的页码构造函数:\n" + json.toString());
            }
        }
        this.data = json;
    }

    /**
     * 获取元素总数
     *
     * @return
     */
    public int getTotal() {
        return data.getInt("total");
    }

    /**
     * 获取单页限制
     *
     * @return
     */
    public int getLimit() {
        return data.getInt("limit");
    }

    /**
     * 获取当前页码
     *
     * @return
     */
    public int getPage() {
        return data.getInt("page");
    }

    /**
     * 获取总页码
     *
     * @return
     */
    public int getPages() {
        return data.getInt("pages");
    }

    @Override
    public String toString() {
        return data.toString();
    }
}
