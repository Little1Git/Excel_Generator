//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package excel_processor;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;
import java.util.stream.Collectors;

@Data // 自动生成 getter、setter、toString、equals、hashCode
@AllArgsConstructor // 生成全参构造函数
@NoArgsConstructor // 生成无参构造函数
public class Record {
    public String a_id;
    public String checkvalue;
    public String date;
    public String remark;
    public String doexecuteby;
    public String lineindex;
    public String checkexecuteby;
    public String stationindex;
    public String linename;
    public String shift;
    public String stationname;
    public String submitdate;
    public String no;
    public String whattomaintain;
    public String interval;
    public String costtime;
    public String doby;
    public String checkby;

    public static Map<String, List<Record>> groupByStationName(List<Record> records) {
        return (Map)records.stream().collect(Collectors.groupingBy(Record::getStationname));
    }

    public static Map<String, Map<List<String>, List<Record>>> classifyRecords(Map<String, List<Record>> groupedRecords) {
        Map<String, Map<List<String>, List<Record>>> result = new HashMap();
        Iterator var2 = groupedRecords.entrySet().iterator();

        while(var2.hasNext()) {
            Map.Entry<String, List<Record>> entry = (Map.Entry)var2.next();
            String stationName = (String)entry.getKey();
            List<Record> records = (List)entry.getValue();
            Map<List<String>, List<Record>> classified = (Map)records.stream().collect(Collectors.groupingBy((record) -> {
                return Arrays.asList(record.getNo(), record.getWhattomaintain(), record.getInterval(), record.getCosttime(), record.getDoby(), record.getCheckby());
            }));
            result.put(stationName, classified);
        }

        return result;
    }

    public static Map<String, Map<String, List<Record>>> group_by_stations_and_interval(Map<String, List<Record>> groupedRecordsByStation) {
        Map<String, Map<String, List<Record>>> map_of_stations_confirm = new HashMap();
//        过滤掉 recordList 中 Checkexecuteby 字段为 null 或 "NULL" 的记录。
        groupedRecordsByStation.forEach((stationName, recordList) -> {
            List<Record> checkexecutebyNotNull = (List)recordList.stream().filter((record) -> {
                return record.getCheckexecuteby() != null && !"NULL".equals(record.getCheckexecuteby());
            }).collect(Collectors.toList());
            Map<String, List<Record>> groupedByInterval = (Map)checkexecutebyNotNull.stream().collect(Collectors.groupingBy(Record::getInterval));
            map_of_stations_confirm.put(stationName, groupedByInterval);
            System.out.println("Station: " + stationName);
            groupedByInterval.forEach((interval, recordsInInterval) -> {
                System.out.println("  Interval: " + interval);
                recordsInInterval.forEach((record) -> {
                    System.out.println("    " + record);
                });
            });
        });

//        对每个站点的每个 Interval 分组中的记录进行去重（基于 Checkexecuteby + date 字段）

        map_of_stations_confirm.forEach((stationName, stationMap) -> {
            stationMap.forEach((interval, records) -> {
                Map<RecordKey, Record> uniqueByCheckexecutebyAndDate = new HashMap<>();
                records.forEach((record) -> {
                    RecordKey key = new RecordKey(record.getCheckexecuteby(), record.getDate());
                    uniqueByCheckexecutebyAndDate.put(key, record);
                });
                stationMap.put(interval, new ArrayList<>(uniqueByCheckexecutebyAndDate.values()));
//                替换原来的记录列表
//                由于 stationMap 是 map_of_stations_confirm 的一部分，因此 map_of_stations_confirm 的内容也被修改了。
            });
        });
        // 遍历打印
        map_of_stations_confirm.forEach((stationName, stationMap) -> {
            System.out.println("Station: " + stationName);
            stationMap.forEach((interval, records) -> {
                System.out.println("    Unique Records for Interval: " + interval);
                records.forEach((value) -> {
                    System.out.println("        Record: " + value);
                });
            });
        });
        return map_of_stations_confirm;
    }

}
