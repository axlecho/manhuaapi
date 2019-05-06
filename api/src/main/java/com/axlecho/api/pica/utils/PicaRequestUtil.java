package com.axlecho.api.pica.utils;

import com.axlecho.api.pica.PicaHeader;
import com.axlecho.api.pica.results.PicaResult;
import net.sf.json.JSONObject;

import java.io.IOException;

public class PicaRequestUtil {

    public static PicaResult getPicaResult(PicaHeader header) throws IOException, Exception {
        String returnStr = NetUtil.getResult(header);
        JSONObject json = JSONObject.fromObject(returnStr);
        return new PicaResult(json);
    }

}
