package excel_processor;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ExcelCopyRow {

    private static final String TEMPLATE_PATH = "templates/input.xlsx";

    public static void copyRowsInSheet(int copyTimes, String outputFile) {
        int targetRowIndex = 4;

        try (Workbook workbook = readExcelTemplate();
             FileOutputStream fos = new FileOutputStream(outputFile)) {

            Sheet sheet = workbook.getSheetAt(0);
            int lastRowNum = sheet.getLastRowNum();

            for (int i = 0; i < copyTimes; i++) {
                sheet.shiftRows(targetRowIndex + 1 + i, lastRowNum + 1, 1);
                Row sourceRow = sheet.getRow(targetRowIndex);
                Row newRow = sheet.createRow(targetRowIndex + 1 + i);
                copyRow(sourceRow, newRow);
                lastRowNum++;
            }


            workbook.write(fos);
            System.out.println("操作完成，文件已保存到: " + outputFile);

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("处理 Excel 文件时发生错误", e);
        }
    }

    private static Workbook readExcelTemplate() throws IOException {
        try (InputStream inputStream = ExcelCopyRow.class.getClassLoader().getResourceAsStream(TEMPLATE_PATH)) {
            if (inputStream == null) {
                throw new IOException("Excel 模板文件未找到: " + TEMPLATE_PATH);
            }
            return new XSSFWorkbook(inputStream);
        }
    }

    private static void copyRow(Row sourceRow, Row targetRow) {
        for (int i = 0; i < sourceRow.getLastCellNum(); i++) {
            Cell sourceCell = sourceRow.getCell(i);
            if (sourceCell != null) {
                Cell targetCell = targetRow.createCell(i);
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