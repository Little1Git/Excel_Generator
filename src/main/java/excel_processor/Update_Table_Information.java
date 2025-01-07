//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package excel_processor;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Update_Table_Information {
    public Update_Table_Information() {
    }

    public static void update_table_info(String filePath, String Update_time, String Machine_Name, String Month, String Year) throws IOException {
        FileInputStream fis = new FileInputStream(filePath);
        XSSFWorkbook workbook = new XSSFWorkbook(fis);
        XSSFSheet sheet = workbook.getSheetAt(0);
        updateMergedCell(sheet, 0, 20, 23, Update_time);
        updateMergedCell(sheet, 1, 0, 5, Machine_Name);
        updateMergedCell(sheet, 2, 16, 19, Month);
        updateMergedCell(sheet, 2, 20, 25, Year);
        fis.close();
        FileOutputStream fos = new FileOutputStream(filePath);
        workbook.write(fos);
        fos.close();
        workbook.close();
        System.out.println("更新了表格信息");
    }

    public static void update_table_info2(String filePath, String document, String prepare, String review,String approve, String edition) throws IOException {
        FileInputStream fis = new FileInputStream(filePath);
        XSSFWorkbook workbook = new XSSFWorkbook(fis);
        XSSFSheet sheet = workbook.getSheetAt(0);
        updateMergedCell(sheet, 0, 12, 16, document);
        updateMergedCell(sheet, 1, 10, 15, prepare);
        updateMergedCell(sheet, 1, 20, 25,review);
        updateMergedCell(sheet, 1, 30, 36,approve);
        updateMergedCell(sheet, 0, 27, 29,edition);
        fis.close();
        FileOutputStream fos = new FileOutputStream(filePath);
        workbook.write(fos);
        fos.close();
        workbook.close();
        System.out.println("更新了表格信息2");
    }

    private static void updateMergedCell(Sheet sheet, int rowIndex, int startCol, int endCol, String value) {
        int mergedRegions = sheet.getNumMergedRegions();

        for(int i = 0; i < mergedRegions; ++i) {
            CellRangeAddress range = sheet.getMergedRegion(i);
            if (range.getFirstRow() == rowIndex && range.getFirstColumn() == startCol && range.getLastColumn() == endCol) {
                Row row = sheet.getRow(range.getFirstRow());
                if (row == null) {
                    row = sheet.createRow(range.getFirstRow());
                }

                Cell cell = row.getCell(range.getFirstColumn());
                if (cell == null) {
                    cell = row.createCell(range.getFirstColumn());
                }

                cell.setCellValue(value);
                CellStyle style = cell.getCellStyle();
                if (style == null) {
                    style = sheet.getWorkbook().createCellStyle();
                }

                style.setAlignment(HorizontalAlignment.CENTER);
                style.setVerticalAlignment(VerticalAlignment.CENTER);
                cell.setCellStyle(style);
                break;
            }
        }

    }
}
