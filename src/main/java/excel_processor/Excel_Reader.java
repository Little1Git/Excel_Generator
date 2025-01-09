
package excel_processor;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Excel_Reader {
    public Excel_Reader() {
    }

    public static List<Record> readExcel(String filePath) {
        List<Record> records = new ArrayList();

        try {
            FileInputStream fis = new FileInputStream(filePath);

            try {
                Workbook workbook = new XSSFWorkbook(fis);

                try {
                    Sheet sheet = workbook.getSheetAt(0);
                    Iterator<Row> rowIterator = sheet.iterator();
                    if (rowIterator.hasNext()) {
                        rowIterator.next();
                    }

                    while(rowIterator.hasNext()) {
                        Row row = (Row)rowIterator.next();
                        Record record = new Record();

                        System.out.println(getCellValue(row.getCell(2)));
                        record.setA_id(getCellValue(row.getCell(0)));
                        record.setCheckvalue(getCellValue(row.getCell(1)));
                        record.setDate(getCellValue(row.getCell(2)));
                        record.setRemark(getCellValue(row.getCell(3)));
                        record.setDoexecuteby(getCellValue(row.getCell(4)));
                        record.setLineindex(getCellValue(row.getCell(5+1)));
                        record.setCheckexecuteby(getCellValue(row.getCell(6+1)));
                        record.setStationindex(getCellValue(row.getCell(7+1)));
                        record.setLinename(getCellValue(row.getCell(8+3)));
                        record.setShift(getCellValue(row.getCell(9+3)));
                        record.setStationname(getCellValue(row.getCell(10+3)));
                        record.setSubmitdate(getCellValue(row.getCell(11+3)));
                        record.setNo(getCellValue(row.getCell(12+3)));
                        record.setWhattomaintain(getCellValue(row.getCell(13+3)));
                        record.setInterval(getCellValue(row.getCell(14+3)).substring(1));
                        record.setCosttime(getCellValue(row.getCell(15+3)));
                        record.setDoby(getCellValue(row.getCell(16+3)));
                        record.setCheckby(getCellValue(row.getCell(17+3)));

                        records.add(record);
                    }
                } catch (Throwable var10) {
                    try {
                        workbook.close();
                    } catch (Throwable var9) {
                        var10.addSuppressed(var9);
                    }

                    throw var10;
                }

                workbook.close();
            } catch (Throwable var11) {
                try {
                    fis.close();
                } catch (Throwable var8) {
                    var11.addSuppressed(var8);
                }

                throw var11;
            }

            fis.close();
        } catch (IOException var12) {
            var12.printStackTrace();
        }

        return records;
    }

    public static String getCellValue(Cell cell) {
        if (cell == null) {
            return "";
        } else {
            switch (cell.getCellType()) {
                case STRING:
                    return cell.getStringCellValue();
                case NUMERIC:
                    if (DateUtil.isCellDateFormatted(cell)) {
                        return cell.getDateCellValue().toString();
                    }

                    return String.valueOf(cell.getNumericCellValue());
                case BOOLEAN:
                    return String.valueOf(cell.getBooleanCellValue());
                case FORMULA:
                    return cell.getCellFormula();
                case BLANK:
                    return "";
                default:
                    return "";
            }
        }
    }

    public static void main(String[] args) {

//        String filePath = "C:/Users/AQY2SZH/Desktop/checklist_old.xlsx";
//        List<Record> records2 = readExcel(filePath);

        String filePath = "C:/Users/AQY2SZH/Downloads/checkitem.xlsx";
        List<Record> records = readExcel(filePath);

//        List<Record> records = new ArrayList<>();
//
        // 获取当前时间
        Date currentDate = new Date();
        String cur = currentDate.toString();

        records.add(new Record("id1","check1", cur,
                "remark1","doexe1","lineindex1","checkexe1","stationindex1","linename1","shift1","stationname1",cur,
                "no1","maintain1","Daily","10","doby1","checkby1"));
        records.add(new Record("id2","check2", cur,
                "remark2","doexe2","lineindex2","checkexe2","stationindex2","linename2","shift2","stationname2",cur,
                "no2","maintain2","Daily","9","doby2","checkby2"));
        records.add(new Record("id3","check3", cur,
                "remark3","doexe3","lineindex3","checkexe3","stationindex1","linename3","shift3","stationname1",cur,
                "no3","maintain3","Daily","8","doby3","checkby3"));


        // 示例 OtherInfo 列表
        List<OtherInfo> otherInfoList = Arrays.asList(
//                new OtherInfo("001", "Document1", "Prepare1", "Review1", "Approve1", "Edition1"),
//                new OtherInfo("002", "Document2", "Prepare2", "Review2", "Approve2", "Edition2"),
//                new OtherInfo("003", "Document3", "Prepare3", "Review3", "Approve3", "Edition3"),
                new OtherInfo("stationindex1", "Document3", "Prepare3", "Review3", "Approve3", "Edition3"),
                new OtherInfo("stationindex2", "Document3", "Prepare3", "Review3", "Approve3", "Edition3"),
                new OtherInfo("S003221", "Document3", "Prepare3", "Review3", "Approve3", "Edition3")
                );

        Generate_Excel(records,otherInfoList);
    }

    public static void Generate_Excel(List<Record> records, List<OtherInfo> otherInfoList) {
        String father_path="data/files/excel";
        // 获取当前执行路径
        String currentPath = System.getProperty("user.dir");
        // 输出当前路径
        System.out.println("当前执行路径: " + currentPath);
//        String father_path="/opt/mendix/build/data/files/excel";
        exist_or_create(father_path);

        // 使用 Stream API 创建 Map
        Map<String, OtherInfo> otherInfoMap = otherInfoList.stream()
                .collect(Collectors.toMap(OtherInfo::getStation_index, otherInfo -> otherInfo));

        Map<String, List<Record>> groupedRecords_by_station = Record.groupByStationName(records);//stationname分类

// 用于confirm表格的,按间隔分组,同名去重
        Map<String, Map<String, List<Record>>> stations_intervals = Record.group_by_stations_and_interval(groupedRecords_by_station);// confirm
        Map<String, Map<List<String>, List<Record>>> classifiedRecords_by_row = Record.classifyRecords(groupedRecords_by_station);// 二维表
        int num_of_stations = classifiedRecords_by_row.size();
        System.out.println("num_of_stations: " + num_of_stations);
        classifiedRecords_by_row.forEach((stationName, Map_of_Row) -> {
            System.out.println("Station Name: " + stationName);
            int num_of_Rows = Map_of_Row.size();
            System.out.println("num_of_Rows: " + num_of_Rows);

//            获取第一个元素
            Map.Entry<List<String>, List<Record>> firstEntry = (Map.Entry)Map_of_Row.entrySet().iterator().next();
            Record firstRecord = (Record)((List)firstEntry.getValue()).get(0);
            String stationindex = firstRecord.stationindex;
            String xlsx_date = firstRecord.date;
            // 提取 "Jan"
            String month = xlsx_date.substring(4, 7);
            // 提取 "2025"
            String year = xlsx_date.substring(24, 28);
            String xlsx_name = stationName.replaceAll("[/\\\\:*?\"<>|]", "_");
            xlsx_name += "_" + stationindex + "_" + year + "_" + convertMonthToNumber(month);

            String outputFile = father_path + "/" + xlsx_name + ".xlsx";
            System.out.println(outputFile);

            ExcelCopyRow.copyRowsInSheet(num_of_Rows, outputFile);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String Update_time = dateFormat.format(new Date());
            String Machine_Name = "Machine Name: " + stationName;


            System.out.println("Station: " + stationName + ", Date: " + firstRecord.getDate());
            String table_date = firstRecord.getDate();
            String monthAbbreviation = table_date.substring(4, 7);
            String day = table_date.substring(8, 10);
            String monthFullName = convertMonthToFullName(monthAbbreviation);
            String Year = table_date.substring(table_date.length() - 4);

            try {
                Update_Table_Information.update_table_info(outputFile, Update_time, Machine_Name, monthFullName, Year);
                String stationIndex = null;
                if (!Map_of_Row.isEmpty()) {
                    // 获取 Map 的第一个 entry
                    Map.Entry<List<String>, List<Record>> firstEntry3 = Map_of_Row.entrySet().iterator().next();

                    // 获取 List<Record> 的第一个元素
                    List<Record> records3 = firstEntry3.getValue();
                    if (records3 != null && !records3.isEmpty()) {
                        Record firstRecord3 = records3.get(0);
                        stationIndex = firstRecord3.getStationindex(); // 假设存在 getter 方法
                        System.out.println("Stationindex: " + stationIndex);
                    } else {
                        System.out.println("The value list is empty.");
                    }
                } else {
                    System.out.println("The map is empty or null.");
                }
                // 检查 stationIndex 是否为空
                if (stationIndex == null || stationIndex.isEmpty()) {
                    throw new IllegalArgumentException("Record列表存在stationIndex = null的元素");
                }
                OtherInfo info=otherInfoMap.get(stationIndex);
                String document=info.getDocument();
                String prepare=info.getPrepare();
                String review=info.getReview();
                String approve=info.getApprove();
                String edition=info.getEdition();
                Update_Table_Information.update_table_info2(outputFile,document,prepare,review,approve,edition);
            } catch (IOException var24) {
                var24.printStackTrace();
            }

            AtomicInteger row_num = new AtomicInteger(5);
            ExcelReplaceRow _insert = new ExcelReplaceRow(outputFile);
            Map_of_Row.forEach((key_of_row, recordList) -> {
                _insert.processRows(key_of_row, row_num.get(), recordList);
                row_num.getAndIncrement();
                System.out.println("\tRow: " + key_of_row);
                Iterator var4 = recordList.iterator();

                while(var4.hasNext()) {
                    Record record = (Record)var4.next();
                    System.out.println("\t\t" + record);
                }

            });
            // confirm
            row_num.getAndIncrement();
            row_num.getAndIncrement();
            Map<String, List<Record>> interval_confirm = (Map)stations_intervals.get(stationName);
            List<Record> dayly = (List)interval_confirm.get("Daily");
            List<Record> weekly = (List)interval_confirm.get("Weekly");
            List<Record> bweekly = (List)interval_confirm.get("Bi_Weekly");
            List<Record> monthly = (List)interval_confirm.get("Monthly");
            List<Record> shiftly = (List)interval_confirm.get("Shiftly");
            insert_one_row_confirm(dayly, _insert, row_num);
            row_num.getAndIncrement();
            insert_one_row_confirm(weekly, _insert, row_num);
            insert_one_row_confirm(bweekly, _insert, row_num);
            row_num.getAndIncrement();
            insert_one_row_confirm(monthly, _insert, row_num);
            row_num.getAndIncrement();
            insert_one_row_confirm(shiftly, _insert, row_num);
        });
    }

    private static void insert_one_row_confirm(List<Record> dayly, ExcelReplaceRow _insert, AtomicInteger row_num) {
        if (dayly != null && !dayly.isEmpty()) {
            _insert.processRowsConfirm(row_num.get(), dayly);
        }

    }

    public static void exist_or_create(String outputFolderPath) {

        File outputFolder = new File(outputFolderPath);
        if (!outputFolder.exists()) {
            try {
                boolean created = outputFolder.mkdirs();
                if (created) {
                    System.out.println("文件夹创建成功：" + outputFolderPath);
                } else {
                    System.out.println("文件夹创建失败：" + outputFolderPath);
                }
            } catch (SecurityException var3) {
                System.err.println("创建文件夹时出现安全异常：" + var3.getMessage());
            }
        } else {
            System.out.println("文件夹已存在：" + outputFolderPath);
        }

    }

    private static String convertMonthToFullName(String monthAbbr) {
        Map<String, String> monthMap = new HashMap();
        monthMap.put("Jan", "January");
        monthMap.put("Feb", "February");
        monthMap.put("Mar", "March");
        monthMap.put("Apr", "April");
        monthMap.put("May", "May");
        monthMap.put("Jun", "June");
        monthMap.put("Jul", "July");
        monthMap.put("Aug", "August");
        monthMap.put("Sep", "September");
        monthMap.put("Oct", "October");
        monthMap.put("Nov", "November");
        monthMap.put("Dec", "December");
        return (String)monthMap.getOrDefault(monthAbbr, "Invalid Month");
    }
    private static String convertMonthToNumber(String monthAbbr) {
        Map<String, String> monthMap = new HashMap();
        monthMap.put("Jan", "01");
        monthMap.put("Feb", "02");
        monthMap.put("Mar", "03");
        monthMap.put("Apr", "04");
        monthMap.put("May", "05");
        monthMap.put("Jun", "06");
        monthMap.put("Jul", "07");
        monthMap.put("Aug", "08");
        monthMap.put("Sep", "09");
        monthMap.put("Oct", "10");
        monthMap.put("Nov", "11");
        monthMap.put("Dec", "12");
        return (String)monthMap.getOrDefault(monthAbbr, "Invalid Month");
    }
}
