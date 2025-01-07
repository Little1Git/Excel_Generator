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

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

public class Excel_Copy_Row {
    public Excel_Copy_Row() {
    }
//    public static void main(String[] args) {
//        // 获取当前时间
//        Date currentDate = new Date();
//
//        // 输出当前时间
//        System.out.println(currentDate.toString());
//    }

    public static void copy_rows_in_right_sheet(int copyTimes, String outputFile) {
        int targetRowIndex = 4;

        try {
            Workbook workbook = readExcelTemplate();

            try {
                Sheet sheet = workbook.getSheetAt(0);
                int lastRowNum = sheet.getLastRowNum();
                int i = 0;

                while(true) {
                    if (i >= copyTimes) {
                        FileOutputStream fos = new FileOutputStream(outputFile);

                        try {
                            workbook.write(fos);
                        } catch (Throwable var11) {
                            try {
                                fos.close();
                            } catch (Throwable var10) {
                                var11.addSuppressed(var10);
                            }

                            throw var11;
                        }

                        fos.close();
                        System.out.println("操作完成，文件已保存到: " + outputFile);
                        break;
                    }

                    sheet.shiftRows(targetRowIndex + 1 + i, lastRowNum + 1, 1);
                    Row sourceRow = sheet.getRow(targetRowIndex);
                    Row newRow = sheet.createRow(targetRowIndex + 1 + i);
                    copyRow(sourceRow, newRow);
                    ++lastRowNum;
                    ++i;
                }
            } catch (Throwable var12) {
                if (workbook != null) {
                    try {
                        workbook.close();
                    } catch (Throwable var9) {
                        var12.addSuppressed(var9);
                    }
                }

                throw var12;
            }

            if (workbook != null) {
                workbook.close();
            }
        } catch (IOException var13) {
            var13.printStackTrace();
        }

    }

    public static Workbook readExcelTemplate() {
        try {
            InputStream inputStream = Excel_Copy_Row.class.getClassLoader().getResourceAsStream("templates/input.xlsx");

            XSSFWorkbook var1;
            try {
                if (inputStream == null) {
                    throw new IOException("Excel 模板文件未找到");
                }

                var1 = new XSSFWorkbook(inputStream);
            } catch (Throwable var4) {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (Throwable var3) {
                        var4.addSuppressed(var3);
                    }
                }

                throw var4;
            }

            if (inputStream != null) {
                inputStream.close();
            }

            return var1;
        } catch (IOException var5) {
            var5.printStackTrace();
            throw new RuntimeException("读取 Excel 模板失败", var5);
        }
    }

    public static void copyRow(Row sourceRow, Row targetRow) {
        for(int i = 0; i < sourceRow.getLastCellNum(); ++i) {
            Cell sourceCell = sourceRow.getCell(i);
            Cell targetCell = targetRow.createCell(i);
            if (sourceCell != null) {
                targetCell.setCellStyle(sourceCell.getCellStyle());
                switch (sourceCell.getCellType()) {
                    case STRING:
                        targetCell.setCellValue(sourceCell.getStringCellValue());
                        break;
                    case NUMERIC:
                        targetCell.setCellValue(sourceCell.getNumericCellValue());
                        break;
                    case BOOLEAN:
                        targetCell.setCellValue(sourceCell.getBooleanCellValue());
                        break;
                    case FORMULA:
                        targetCell.setCellFormula(sourceCell.getCellFormula());
                        break;
                    default:
                        targetCell.setCellValue(sourceCell.toString());
                }
            }
        }

    }
}
