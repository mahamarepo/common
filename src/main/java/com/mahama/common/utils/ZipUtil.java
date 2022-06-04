package com.mahama.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * 压缩包工具类
 */
@Slf4j
public class ZipUtil {

    /**
     * 把文件集合打成zip压缩包
     *
     * @param srcFiles 压缩文件集合
     * @param zipFile  zip文件名
     * @throws RuntimeException 异常
     */
    public static void toZip(File[] srcFiles, File zipFile) throws RuntimeException {
        long start = System.currentTimeMillis();
        if (zipFile == null) {
            log.error("压缩包文件名为空！");
            return;
        }
        if (!zipFile.getName().endsWith(".zip")) {
            log.error("压缩包文件名异常，zipFile={}", zipFile.getPath());
            return;
        }
        ZipOutputStream zos = null;
        try {
            FileOutputStream out = new FileOutputStream(zipFile);
            zos = new ZipOutputStream(out);
            zos.setEncoding("GBK");
            for (File srcFile : srcFiles) {
                byte[] buf = new byte[1024];
                zos.putNextEntry(new ZipEntry(srcFile.getName()));
                int len;
                FileInputStream in = new FileInputStream(srcFile);
                while ((len = in.read(buf)) != -1) {
                    zos.write(buf, 0, len);
                }
                in.close();
            }
            zos.setComment("如果无法解压请尝试重新导出");
            zos.close();
            out.close();
            long end = System.currentTimeMillis();
            log.info("压缩完成，耗时：" + (end - start) + " ms");
        } catch (Exception e) {
            log.error("文件压缩时出错：： ", e);
            throw new RuntimeException("文件压缩时出错： ", e);
        }
    }

    public static void delete(File file) {
        File[] listFiles = file.listFiles();
        if (listFiles != null) {
            for (File f : listFiles) {
                if (f.isDirectory()) {
                    delete(f);
                } else {
                    f.delete();
                }
            }
        }
        file.delete();
    }
}
