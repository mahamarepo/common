package com.mahama.common.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.UnsupportedEncodingException;
import java.util.*;

public class AutographUtil {
    public static String getRspBody(JSONObject body) {
        String rspbody = null;
        StringBuilder sb = new StringBuilder();
        List<Map.Entry<String, Object>> params;
        if (body == null || body.isEmpty()) {
            sb.append("\n");
        } else {
            params = new ArrayList<Map.Entry<String, Object>>(body.entrySet());
            Collections.sort(params, new Comparator<Map.Entry<String, Object>>() {
                public int compare(Map.Entry<String, Object> o1, Map.Entry<String, Object> o2) {
                    return o1.getKey().compareTo(o2.getKey());
                }
            });

            for (Map.Entry<String, Object> param : params) {
                if (param == null) {
                    continue;
                }
                sb.append(param.getKey()).append("=").append(param.getValue()).append("&");
            }
            sb.delete(sb.length() - 1, sb.length());
            sb.append("\n");
        }
        rspbody = getURLEncoderString(sb.toString());
        return rspbody;
    }

    public static String generateSignature(JSONObject body) {
        return DigestUtils.md5Hex(getRspBody(body));
    }
    public static String generateSignature(String body) {
        return DigestUtils.md5Hex(body);
    }

    public static String getURLEncoderString(String str) {
        String result = "";
        if (null == str) {
            return "";
        }
        try {
            result = java.net.URLEncoder.encode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

}
