package com.axlecho.api.pica.results;

import net.sf.json.JSONObject;

/**
 * 获取哔咔操作的请求结果
 * @author FlanN
 * @date 2019/03/09
 */
public class PicaActionResult extends PicaResult {

    public PicaActionResult(JSONObject result) {
        super(result);
    }

    /**
     * 注意，截止2019/3/9 已经没有此字段
     * 如果需要检查是否点赞/取消点赞需要重新获取本子信息
     *
     * 点赞：like/unlike
     * 收藏: favorite/un_favourite
     *
     * @return
     */
    public String getAction() {
        return getData().getString("action");
    }

}
