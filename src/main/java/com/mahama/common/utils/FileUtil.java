package com.mahama.common.utils;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class FileUtil extends FileUtils {
    public static void copyFile(File srcFile, File destDir, String destFileName) throws IOException {
        FileUtils.copyFile(srcFile, new File(destDir.getPath() + "\\" + destFileName));
    }
    public static void copyDirectory(File srcDir, File destDir, String regex, String replaceStr) throws IOException {
        if (destDir.exists()) {
            Assert.isTrue(destDir.isDirectory(), "复制目标非文件夹");
        } else {
            Assert.isTrue(destDir.mkdirs(), "创建目录失败");
        }
        File[] files = srcDir.listFiles();
        Assert.notNull(files, "复制的源文件不存在");
        for (File file : files) {
            if (file.isDirectory()) {
                File copyDest = new File(destDir, file.getName());
                copyDirectory(file, copyDest, regex, replaceStr);
            } else {
                copyFile(file, destDir, file.getName().replaceFirst(regex, replaceStr));
            }
        }
    }
}
