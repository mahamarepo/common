package com.mahama.common.utils;

import com.alibaba.fastjson.JSONObject;
import com.mahama.common.enumeration.HttpMethod;
import com.mahama.common.enumeration.MimeType;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import javax.net.ssl.SSLSocketFactory;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Slf4j
public class OkHttpUtil {
    private String baseUrl = "";
    private MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
    private final HashMap<String, List<Cookie>> cookieStore = new HashMap<>();
    private final OkHttpClient httpClient;

    public OkHttpClient getHttpClient() {
        return httpClient;
    }

    public OkHttpUtil url(String url) {
        baseUrl = url;
        return this;
    }

    public OkHttpUtil mediaType(MimeType type) {
        mediaType = MediaType.parse(type.getValue() + "; charset=utf-8");
        return this;
    }

    public class MyCookieJar implements CookieJar {
        @Override
        public void saveFromResponse(HttpUrl httpUrl, List<Cookie> list) {
            cookieStore.put(httpUrl.host(), list);
        }

        @Override
        public List<Cookie> loadForRequest(HttpUrl httpUrl) {
            List<Cookie> cookies = cookieStore.get(httpUrl.host());
            if (cookies == null) {
                cookies = new ArrayList<>();
            }
            return cookies;
        }
    }

    public static OkHttpUtil me() {
        return new OkHttpUtil();
    }

    public OkHttpUtil() {
        httpClient = new OkHttpClient.Builder()
                .connectTimeout(40, TimeUnit.SECONDS)
                .readTimeout(40, TimeUnit.SECONDS)
                .cookieJar(new MyCookieJar())
                .build();
    }

    public OkHttpUtil(SSLSocketFactory sslSocketFactory) {
        httpClient = new OkHttpClient.Builder()
                .connectTimeout(40, TimeUnit.SECONDS)
                .readTimeout(40, TimeUnit.SECONDS)
                .cookieJar(new MyCookieJar())
                .socketFactory(sslSocketFactory)
                .build();
    }

    public String getGetUrl(Map<String, Object> param) {
        try {
            StringBuilder url = new StringBuilder("?");
            Set<Map.Entry<String, Object>> entrys = param.entrySet();
            int index = 0;
            for (Map.Entry<String, Object> entry : entrys) {
                if (entry.getValue() == null) continue;
                if (index > 0) {
                    url.append("&");
                }
                index++;
                url.append(entry.getKey()).append("=").append(entry.getValue());
            }
            return url.toString();
        } catch (Exception err) {
            return "";
        }
    }

    private Headers Hashtable2Headers(Hashtable<String, String> hashtable) {
        Headers.Builder builder = new Headers.Builder();
        hashtable.forEach(builder::add);
        return builder.build();
    }

    public Request getRequest(String url, HttpMethod method) {
        return getRequest(url, method, new JSONObject());
    }

    public Request getRequest(String url, HttpMethod method, Hashtable<String, String> hashtable) {
        return getRequest(url, method, null, Hashtable2Headers(hashtable));
    }

    public Request getRequest(String url, HttpMethod method, Headers headers) {
        return getRequest(url, method, null, headers);
    }

    public Request getRequest(String url, HttpMethod method, JSONObject body, Hashtable<String, String> hashtable) {
        return getRequest(url, method, body, Hashtable2Headers(hashtable));
    }

    public Request getRequest(String url, HttpMethod method, JSONObject body) {
        if (body == null)
            body = new JSONObject();
        RequestBody requestBody = RequestBody.create(body.toString(), mediaType);
        if (!okhttp3.internal.http.HttpMethod.permitsRequestBody(method.getValue())) {
            url = url + getGetUrl(body);
            requestBody = null;
        }
        return new Request.Builder()
                .url(baseUrl + url)
                .method(method.getValue(), requestBody)
                .build();
    }

    public Request getRequest(String url, HttpMethod method, JSONObject body, Headers headers) {
        if (body == null)
            body = new JSONObject();
        RequestBody requestBody = RequestBody.create(body.toString(), mediaType);
        if (!okhttp3.internal.http.HttpMethod.permitsRequestBody(method.getValue())) {
            url = url + getGetUrl(body);
            requestBody = null;
        }
        return new Request.Builder()
                .url(baseUrl + url)
                .method(method.getValue(), requestBody)
                .headers(headers)
                .build();
    }

    public JSONObject get(String url) {
        return handleRequest(getRequest(url, HttpMethod.GET));
    }

    public JSONObject get(String url, JSONObject jsonObject) {
        return handleRequest(getRequest(url, HttpMethod.GET, jsonObject));
    }

    public JSONObject get(String url, Headers headers) {
        return handleRequest(getRequest(url, HttpMethod.GET, headers));
    }

    public String getByString(String url, JSONObject jsonObject) {
        return handleRequestByString(getRequest(url, HttpMethod.GET, jsonObject));
    }

    public String getByString(String url, Headers headers) {
        return handleRequestByString(getRequest(url, HttpMethod.GET, headers));
    }

    public JSONObject get(String url, Hashtable<String, String> hashtable) {
        return handleRequest(getRequest(url, HttpMethod.GET, hashtable));
    }

    public JSONObject get(String url, JSONObject jsonObject, Headers headers) {
        return handleRequest(getRequest(url, HttpMethod.GET, jsonObject, headers));
    }

    public void asyncGet(String url, JSONObject jsonObject, Headers headers, Callback callback) {
        handleAsyncRequest(getRequest(url, HttpMethod.GET, jsonObject, headers), callback);
    }

    public JSONObject get(String url, JSONObject jsonObject, Hashtable<String, String> hashtable) {
        return handleRequest(getRequest(url, HttpMethod.GET, jsonObject, hashtable));
    }

    public JSONObject post(String url) {
        return handleRequest(getRequest(url, HttpMethod.POST));
    }

    public JSONObject post(String url, JSONObject jsonObject) {
        return handleRequest(getRequest(url, HttpMethod.POST, jsonObject));
    }

    public JSONObject post(String url, Headers headers) {
        return handleRequest(getRequest(url, HttpMethod.POST, headers));
    }

    public JSONObject post(String url, Hashtable<String, String> hashtable) {
        return handleRequest(getRequest(url, HttpMethod.POST, hashtable));
    }

    public JSONObject post(String url, JSONObject jsonObject, Headers headers) {
        return handleRequest(getRequest(url, HttpMethod.POST, jsonObject, headers));
    }

    public JSONObject post(String url, JSONObject jsonObject, Hashtable<String, String> hashtable) {
        return handleRequest(getRequest(url, HttpMethod.POST, jsonObject, hashtable));
    }

    public void asyncPost(String url, JSONObject jsonObject, Headers headers, Callback callback) {
        handleAsyncRequest(getRequest(url, HttpMethod.POST, jsonObject, headers), callback);
    }


    public JSONObject doHttp(String url, HttpMethod method, JSONObject body) {
        return handleRequest(getRequest(url, method, body));
    }

    public JSONObject doHttp(String url, HttpMethod method, JSONObject body, Hashtable<String, String> hashtable) {
        return doHttp(url, method, body, Hashtable2Headers(hashtable));
    }

    public JSONObject doHttp(String url, HttpMethod method, JSONObject body, Headers headers) {
        return handleRequest(getRequest(url, method, body, headers));
    }

    public void handleAsyncRequest(Request request, Callback callback) {
        httpClient.newCall(request).enqueue(callback);
    }

    public JSONObject handleRequest(Request request) {
        try {
            Response response = httpClient.newCall(request).execute();
            if (response.code() == 200) {
                return getResponseJson(response);
            } else {
                log.error("HTTP请求异常：{} {}", request, response);
                return null;
            }
        } catch (Exception e) {
            log.error("HTTP请求异常-E：" + e);
            return null;
        }
    }

    public String handleRequestByString(Request request) {
        try {
            Response response = httpClient.newCall(request).execute();
            if (response.code() == 200) {
                return new String(Objects.requireNonNull(response.body()).bytes(), StandardCharsets.UTF_8);
            } else {
                log.error("HTTP请求异常：{} {}", request, response);
                return null;
            }
        } catch (Exception e) {
            log.error("HTTP请求异常-E：" + e);
            return null;
        }
    }

    public JSONObject getResponseJson(Response response) {
        try {
            String st = new String(Objects.requireNonNull(response.body()).bytes(), StandardCharsets.UTF_8);
            if (st.startsWith("<")) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("html", st);
                jsonObject.put("requestCookie", httpClient.cookieJar());
                return jsonObject;
            }
            return JSONObject.parseObject(st);
        } catch (Exception e) {
            log.error("HTTP解析数据异常：" + e);
            return null;
        }
    }
}
