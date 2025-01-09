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
import java.util.List;

public class ExcelReplaceRow {
    public String targetFile;

    public ExcelReplaceRow(String targetFile) {
        this.targetFile = targetFile;
    }

    public Workbook readExcelTemplate() {
        try {
            // 创建文件输入流
            InputStream fileInputStream = new FileInputStream(this.targetFile);

            XSSFWorkbook workbook;
            try {
                // 使用文件输入流创建 XSSFWorkbook 对象
                workbook = new XSSFWorkbook(fileInputStream);
            } catch (Throwable exceptionDuringWorkbookCreation) {
                try {
                    // 如果创建 Workbook 时抛出异常，关闭输入流
                    fileInputStream.close();
                } catch (Throwable exceptionDuringStreamClose) {
                    // 将关闭流时的异常添加为被抑制的异常
                    exceptionDuringWorkbookCreation.addSuppressed(exceptionDuringStreamClose);
                }
                throw exceptionDuringWorkbookCreation;
            }

            // 关闭文件输入流
            fileInputStream.close();
            return workbook;
        } catch (IOException ioException) {
            // 捕获 IO 异常并打印堆栈信息
            ioException.printStackTrace();
            throw new RuntimeException("读取 Excel 文件失败", ioException);
        }
    }


    public void processRows(List<String> headers, int rowNumber, List<Record> recordList) {
        Workbook workbook = null;

        try {
            // 读取 Excel 模板
            workbook = this.readExcelTemplate();
            Sheet sheet = workbook.getSheetAt(0);
            Row row = sheet.getRow(rowNumber - 1);
            if (row == null) {
                row = sheet.createRow(rowNumber - 1);
            }

            // 设置表头内容
            for (int columnIndex = 0; columnIndex < 6; ++columnIndex) {
                Cell cell = row.getCell(columnIndex);
                if (cell == null) {
                    cell = row.createCell(columnIndex);
                }
                cell.setCellValue(headers.get(columnIndex));
            }

            // 遍历记录列表
            for (Record record : recordList) {
                char dayTensDigit = record.date.charAt(8);
                char dayUnitsDigit = record.date.charAt(9);
                int day = Integer.parseInt(String.valueOf(dayTensDigit) + dayUnitsDigit);
                System.out.println("Day (用于计算插入列位置): " + day);

                // 计算目标列并获取单元格
                Cell targetCell = row.getCell(day + 6 - 1);
                if (targetCell == null) {
                    targetCell = row.createCell(day + 6 - 1);
                }

                // 处理记录的执行者信息
                String formattedExecutor;
                if ("NULL".equals(record.doexecuteby)) {
                    formattedExecutor = "-";
                } else {
                    String executorInfo = record.doexecuteby;
                    formattedExecutor = executorInfo.split("\\s*\\(")[0];
                }

                // 合并已有内容与新内容
                String existingContent = targetCell.getStringCellValue();
                if (existingContent != null && !existingContent.trim().isEmpty()) {
                    targetCell.setCellValue(existingContent + " " + formattedExecutor);
                } else {
                    targetCell.setCellValue(formattedExecutor);
                }
            }

            // 保存文件
            try (FileOutputStream fileOutputStream = new FileOutputStream(this.targetFile)) {
                workbook.write(fileOutputStream);
            }

        } catch (IOException exception) {
            exception.printStackTrace();
            throw new RuntimeException("处理 Excel 文件失败", exception);
        } finally {
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }
        }
    }


    public void processRowsConfirm(int rowNumber, List<Record> recordList) {
        Workbook workbook = null;

        try {
            // 读取 Excel 文件
            workbook = this.readExcelTemplate();
            Sheet sheet = workbook.getSheetAt(0);

            // 获取或创建指定行
            Row row = sheet.getRow(rowNumber - 1);
            if (row == null) {
                row = sheet.createRow(rowNumber - 1);
            }

            // 遍历记录列表
            for (Record record : recordList) {
                // 从日期中提取天数部分
                char ninthChar = record.date.charAt(8);
                char tenthChar = record.date.charAt(9);
                String dayString = String.valueOf(ninthChar) + tenthChar;
                int day = Integer.parseInt(dayString);
                System.out.println("Day (用于计算插入列位置): " + day);

                // 获取或创建对应的单元格
                Cell cell = row.getCell(day + 6 - 1);
                if (cell == null) {
                    cell = row.createCell(day + 6 - 1);
                }

                // 根据 checkexecuteby 的值生成结果
                String result;
                if ("NULL".equals(record.checkexecuteby)) {
                    result = "-";
                } else {
                    String existingValue = record.checkexecuteby;
                    result = existingValue.split("\\s*\\(")[0];
                }

                // 写入或追加到单元格内容
                String existingContent = cell.getStringCellValue();
                if (existingContent != null && !existingContent.trim().isEmpty()) {
                    cell.setCellValue(existingContent + " " + result);
                } else {
                    cell.setCellValue(result);
                }
            }

            // 将结果写回到文件
            try (FileOutputStream fileOutputStream = new FileOutputStream(this.targetFile)) {
                workbook.write(fileOutputStream);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("处理 Excel 文件失败", e);
        } finally {
            // 确保关闭工作簿
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void fillEmptyCellsFromRow5() {
        Workbook workbook = null;

        try {
            // 读取 Excel 文件
            workbook = this.readExcelTemplate();
            Sheet sheet = workbook.getSheetAt(0);

            // 从第5行开始遍历
            for (int rowIndex = 4; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                if (row == null) {
                    // 如果行为空，结束遍历
                    break;
                }

                // 检查第1到6列是否有值
                boolean hasAllValues = true;
                for (int colIndex = 0; colIndex < 6; colIndex++) {
                    Cell cell = row.getCell(colIndex);
                    if (cell == null || cell.getStringCellValue().trim().isEmpty()) {
                        hasAllValues = false;
                        break;
                    }
                }

                if (!hasAllValues) {
                    // 如果第1到6列没有值，结束遍历
                    break;
                }

                // 从第7列到第37列检查并填充空单元格
                for (int colIndex = 6; colIndex < 37; colIndex++) {
                    Cell cell = row.getCell(colIndex);
                    if (cell == null || cell.getStringCellValue().trim().isEmpty()) {
                        if (cell == null) {
                            cell = row.createCell(colIndex);
                        }
                        cell.setCellValue("--");
                    }
                }
            }

            // 保存文件
            try (FileOutputStream fileOutputStream = new FileOutputStream(this.targetFile)) {
                workbook.write(fileOutputStream);
            }

        } catch (IOException exception) {
            exception.printStackTrace();
            throw new RuntimeException("处理 Excel 文件失败", exception);
        } finally {
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }
        }
    }

}
