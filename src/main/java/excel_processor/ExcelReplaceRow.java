//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package excel_processor;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

public class ExcelReplaceRow {
    public String targetFile;

    public ExcelReplaceRow(String targetFile) {
        this.targetFile = targetFile;
    }

    public Workbook readExcelTemplate() {
        try {
            InputStream inputStream = new FileInputStream(this.targetFile);

            XSSFWorkbook var2;
            try {
                var2 = new XSSFWorkbook(inputStream);
            } catch (Throwable var5) {
                try {
                    inputStream.close();
                } catch (Throwable var4) {
                    var5.addSuppressed(var4);
                }

                throw var5;
            }

            inputStream.close();
            return var2;
        } catch (IOException var6) {
            var6.printStackTrace();
            throw new RuntimeException("读取 Excel 文件失败", var6);
        }
    }

    public void processRows(List<String> key, int row_num, List<Record> recordsList) {
        Workbook workbook = null;

        try {
            workbook = this.readExcelTemplate();
            Sheet sheet = workbook.getSheetAt(0);
            Row row = sheet.getRow(row_num - 1);
            if (row == null) {
                row = sheet.createRow(row_num - 1);
            }

            for(int i = 0; i < 6; ++i) {
                Cell cell = row.getCell(i);
                if (cell == null) {
                    cell = row.createCell(i);
                }

                cell.setCellValue((String)key.get(i));
            }

            Iterator var28 = recordsList.iterator();

            while(true) {
                while(var28.hasNext()) {
                    Record record = (Record)var28.next();
                    char ninthChar = record.date.charAt(8);
                    char tenthChar = record.date.charAt(9);
                    String var10000 = String.valueOf(ninthChar);
                    int day = Integer.parseInt(var10000 + String.valueOf(tenthChar));
                    System.out.println("Day(用于计算插入列位置): " + day);
                    Cell cur_cell = row.getCell(day + 6 - 1);
                    String result;
                    String existingContent;
                    if (record.doexecuteby.equals("NULL")) {
                        result = "-";
                    } else {
                        existingContent = record.doexecuteby;
                        result = existingContent.split("\\s*\\(")[0];
                    }

                    existingContent = cur_cell.getStringCellValue();
                    if (existingContent != null && !existingContent.trim().isEmpty()) {
                        cur_cell.setCellValue(existingContent + " " + result);
                    } else {
                        cur_cell.setCellValue(result);
                    }
                }

                FileOutputStream fileOutputStream = new FileOutputStream(this.targetFile);

                try {
                    workbook.write(fileOutputStream);
                } catch (Throwable var25) {
                    try {
                        fileOutputStream.close();
                    } catch (Throwable var24) {
                        var25.addSuppressed(var24);
                    }

                    throw var25;
                }

                fileOutputStream.close();
                return;
            }
        } catch (IOException var26) {
            var26.printStackTrace();
            throw new RuntimeException("处理 Excel 文件失败", var26);
        } finally {
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (IOException var23) {
                    var23.printStackTrace();
                }
            }

        }
    }

    public void processRowsConfirm(int row_num, List<Record> recordsList) {
        Workbook workbook = null;

        try {
            workbook = this.readExcelTemplate();
            Sheet sheet = workbook.getSheetAt(0);
            Row row = sheet.getRow(row_num - 1);
            if (row == null) {
                row = sheet.createRow(row_num - 1);
            }

            Iterator var6 = recordsList.iterator();

            while(true) {
                while(var6.hasNext()) {
                    Record record = (Record)var6.next();
                    char ninthChar = record.date.charAt(8);
                    char tenthChar = record.date.charAt(9);
                    String var10000 = String.valueOf(ninthChar);
                    int day = Integer.parseInt(var10000 + String.valueOf(tenthChar));
                    System.out.println("Day(用于计算插入列位置): " + day);
                    Cell cur_cell = row.getCell(day + 6 - 1);
                    String result;
                    String existingContent;
                    if (record.checkexecuteby.equals("NULL")) {
                        result = "-";
                    } else {
                        existingContent = record.checkexecuteby;
                        result = existingContent.split("\\s*\\(")[0];
                    }

                    existingContent = cur_cell.getStringCellValue();
                    if (existingContent != null && !existingContent.trim().isEmpty()) {
                        cur_cell.setCellValue(existingContent + " " + result);
                    } else {
                        cur_cell.setCellValue(result);
                    }
                }

                FileOutputStream fileOutputStream = new FileOutputStream(this.targetFile);

                try {
                    workbook.write(fileOutputStream);
                } catch (Throwable var24) {
                    try {
                        fileOutputStream.close();
                    } catch (Throwable var23) {
                        var24.addSuppressed(var23);
                    }

                    throw var24;
                }

                fileOutputStream.close();
                return;
            }
        } catch (IOException var25) {
            var25.printStackTrace();
            throw new RuntimeException("处理 Excel 文件失败", var25);
        } finally {
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (IOException var22) {
                    var22.printStackTrace();
                }
            }

        }
    }
}
