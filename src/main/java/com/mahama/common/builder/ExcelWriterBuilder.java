package com.mahama.common.builder;

import com.mahama.common.utils.StringUtil;
import lombok.SneakyThrows;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.List;

/**
 * @author mahama
 * @date 2022年07月04日
 */
public class ExcelWriterBuilder {
    public interface TitleThing {
        void run(XSSFRow xssfRow, int rowIndex);
    }

    public interface WriteThing<T> {
        void run(XSSFRow xssfRow, T item, int rowIndex);
    }

    private final XSSFWorkbook xssfWorkbook;
    private final OutputStream outputStream;
    private XSSFSheet xssfSheet = null;
    private int rowIndex;


    @SneakyThrows
    public ExcelWriterBuilder(InputStream inputStream, OutputStream outputStream) {
        this.xssfWorkbook = new XSSFWorkbook(inputStream);
        this.outputStream = outputStream;
    }

    public ExcelWriterBuilder(OutputStream outputStream) {
        this.xssfWorkbook = new XSSFWorkbook();
        this.outputStream = outputStream;
    }

    public ExcelWriterBuilder(String filePath) throws IOException {
        this.xssfWorkbook = new XSSFWorkbook();
        File file = new File(filePath);
        this.outputStream = new FileOutputStream(file);
    }

    public ExcelWriterBuilder rowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
        return this;
    }

    public ExcelWriterBuilder createSheet(String sheetName) {
        if (StringUtil.isNullOrEmpty(sheetName)) {
            xssfSheet = xssfWorkbook.createSheet();
        } else {
            xssfSheet = xssfWorkbook.createSheet(sheetName);
        }
        rowIndex = 0;
        return this;
    }

    public ExcelWriterBuilder changeSheet(int index) {
        xssfSheet = xssfWorkbook.getSheetAt(index);
        rowIndex = 0;
        return this;
    }

    public ExcelWriterBuilder changeSheet(String sheetName) {
        xssfSheet = xssfWorkbook.getSheet(sheetName);
        rowIndex = 0;
        return this;
    }

    public ExcelWriterBuilder setTitle(TitleThing titleThing) {
        XSSFRow xssfRow = xssfSheet.createRow(rowIndex);
        titleThing.run(xssfRow, rowIndex);
        rowIndex++;
        return this;
    }

    public <T> ExcelWriterBuilder setData(List<T> list, WriteThing<T> writeThing) {
        XSSFRow xssfRow;
        for (T item : list) {
            xssfRow = xssfSheet.createRow(rowIndex);
            writeThing.run(xssfRow, item, rowIndex);
            rowIndex++;
        }
        return this;
    }

    public void doWrite() throws IOException {
        xssfWorkbook.write(outputStream);
        outputStream.flush();
        outputStream.close();
    }
}
