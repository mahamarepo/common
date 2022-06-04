package com.mahama.common.utils;

import java.io.IOException;
import java.util.Scanner;

public class WmicUtil {
    /**
     * CPU序列号
     */
    public static String cpuProcessorId() {
        return getValue(new String[]{"wmic", "cpu", "get", "ProcessorId"});
    }

    public static String UUID() {
        return getValue(new String[]{"wmic", "csproduct", "get", "UUID"});
    }

    /**
     * 主板序列号
     */
    public static String biosSerialNumber() {
        return getValue(new String[]{"wmic", "bios", "get", "SerialNumber"});
    }

    private static String getValue(String[] cmdArray) {
        try {
            Process process = Runtime.getRuntime().exec(cmdArray);
            process.getOutputStream().close();
            Scanner sc = new Scanner(process.getInputStream());
            if (sc.hasNext()) {
                sc.next();
                StringBuilder valueBuilder = new StringBuilder();
                while (sc.hasNext()) {
                    valueBuilder.append(sc.next());
                }
                return valueBuilder.toString();
            } else {
                return "";
            }
        } catch (IOException e) {
            return "";
        }
    }
}
