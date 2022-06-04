package com.mahama.common.utils;

import java.io.IOException;

public class BrowseUtil {
    /**
     * 使用默认浏览器打开页面
     */
    public static boolean openUrl(String url) {
        boolean result = true;
        Runtime runtime = Runtime.getRuntime();
        try {
            runtime.exec("cmd /c start " + url);
        } catch (IOException e) {
            e.printStackTrace();
            result = false;
        }
        return result;
    }

    /**
     * 使用Chrome打开页面
     */
    public static boolean openChromeUrl(String url, String chromeDir) {
        return openChromeUrl(url, chromeDir, null);
    }

    /**
     * Chrome使用自定义用户数据打开页面
     */
    public static boolean openChromeUrl(String url, String chromeDir, String userDataDir) {
        boolean result = true;
        Runtime runtime = Runtime.getRuntime();
        try {
            if (StringUtil.isNotNullOrEmpty(userDataDir)) {
                String cmd = "\"" + chromeDir + "\" --user-data-dir=" + userDataDir + " " + url;
                runtime.exec("cmd /c " + cmd);
            } else {
                runtime.exec("cmd /c \"" + chromeDir + "\" " + url);
            }
        } catch (IOException e) {
            e.printStackTrace();
            result = false;
        }
        return result;
    }

    public static void closeChrome() {
        try {
            Runtime.getRuntime().exec("taskkill /F /IM chrome.exe");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
