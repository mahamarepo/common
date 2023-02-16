package com.mahama.common.utils;

import com.alibaba.excel.EasyExcel;
import com.mahama.common.builder.ExcelWriterBuilder;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.HashMap;
import java.util.List;

public class ExcelUtil {
    public static class ExcelRow extends HashMap<Integer, Object> {
        public <T> T cell(Integer cell) {
            return (T) get(cell);
        }
    }

    public interface SomeThing {
        void run(ExcelRow row, int rowIndex);

        default void rowForEach(XSSFSheet sheet) {
            XSSFRow row;
            for (int i = (sheet.getFirstRowNum() + 1); i <= (sheet.getPhysicalNumberOfRows() - 1); i++) {
                row = sheet.getRow(i);
                if (row == null) {
                    continue;
                }
                cellForEach(row, i);
            }
        }

        default void cellForEach(XSSFRow row, int rowIndex) {
            XSSFCell cell;
            ExcelRow map = new ExcelRow();
            for (int i = row.getFirstCellNum(); i <= row.getLastCellNum(); i++) {
                cell = row.getCell(i);
                Object value = getCellValue(cell);
                map.put(i, value);
            }
            run(map, rowIndex);
        }
    }

    public static void readExcel(InputStream inputStream, int sheetIndex, SomeThing someThing) throws Exception {
        XSSFWorkbook xwb = new XSSFWorkbook(inputStream);
        XSSFSheet sheet = xwb.getSheetAt(sheetIndex);
        someThing.rowForEach(sheet);
    }

    private static Object getCellValue(XSSFCell cell) {
        Object value = null;
        if (cell != null) {
            switch (cell.getCellType()) {
                case NUMERIC:
                    //日期数据返回LONG类型的时间戳
                    if ("yyyy\"年\"m\"月\"d\"日\";@".equals(cell.getCellStyle().getDataFormatString())) {
//                        System.out.println(cell.getNumericCellValue() + ":日期格式：" + cell.getCellStyle().getDataFormatString());
                        value = DateUtil.getJavaDate(cell.getNumericCellValue());
                    } else {
                        value = cell.getNumericCellValue();
                    }
                    break;
                case BOOLEAN:
                    //布尔类型
                    value = cell.getBooleanCellValue();
                    break;
                case BLANK:
                    //空单元格
                    break;
                case STRING:
                default:
                    value = cell.getStringCellValue();
                    break;
            }
        }
        return value;
    }

    public static ExcelWriterBuilder write(OutputStream outputStream) {
        return new ExcelWriterBuilder(outputStream);
    }

    public static ExcelWriterBuilder write(InputStream inputStream, OutputStream outputStream) {
        return new ExcelWriterBuilder(inputStream, outputStream);
    }

    public static ExcelWriterBuilder write(String filePath) throws IOException {
        return new ExcelWriterBuilder(filePath);
    }

    public static <T> void easyWrite(String filePath, String sheetName, List<T> list) throws IOException {
        File file = new File(filePath);
        easyWrite(new FileOutputStream(file), sheetName, list);
    }

    public static <T> void easyWrite(OutputStream outputStream, String sheetName, List<T> list) {
        easyWrite(outputStream, sheetName, list, false);
    }

    public static <T> void easyWrite(OutputStream outputStream, String sheetName, List<T> list, boolean noTitle) {
        if (list == null || list.size() == 0) {
            return;
        }
        com.alibaba.excel.write.builder.ExcelWriterBuilder writerBuilder;
        if (noTitle) {
            writerBuilder = EasyExcel.write(outputStream);
        } else {
            writerBuilder = EasyExcel.write(outputStream, list.get(0).getClass());
        }
        writerBuilder.sheet(sheetName).doWrite(list);
    }

    public static <T> void easyWrite(String filePath, Integer sheetNo, List<T> list) throws IOException {
        File file = new File(filePath);
        easyWrite(new FileOutputStream(file), sheetNo, list);
    }

    public static <T> void easyWrite(OutputStream outputStream, Integer sheetNo, List<T> list) {
        easyWrite(outputStream, sheetNo, list, false);
    }

    public static <T> void easyWrite(OutputStream outputStream, Integer sheetNo, List<T> list, boolean noTitle) {
        if (list == null || list.size() == 0) {
            return;
        }
        com.alibaba.excel.write.builder.ExcelWriterBuilder writerBuilder;
        if (noTitle) {
            writerBuilder = EasyExcel.write(outputStream);
        } else {
            writerBuilder = EasyExcel.write(outputStream, list.get(0).getClass());
        }
        writerBuilder.sheet(sheetNo).doWrite(list);
    }
}
