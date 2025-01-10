package excel_processor;

import java.util.*;

public class HashMap_LinkedMap {

    // 自定义排序规则
    private static final List<String> ORDER = Arrays.asList(
            "Shiftly", "Daily", "Weekly", "Bi-Weekly",
            "Monthly", "Bi-Monthly", "Quarterly", "HalfYear",
            "Yearly", "Bi-Yearly"
    );

    public static  LinkedHashMap<List<String>, List<Record>> sortMapByKey(Map<List<String>, List<Record>> inputMap) {
        // 创建一个LinkedHashMap以保持插入顺序
        LinkedHashMap<List<String>, List<Record>> sortedMap = new LinkedHashMap<>();

        // 将键值对存入列表进行排序
        List<Map.Entry<List<String>, List<Record>>> entryList = new ArrayList<>(inputMap.entrySet());
        entryList.sort((entry1, entry2) -> {
            // 获取key中的第三个元素
            String key1 = entry1.getKey().size() > 2 ? entry1.getKey().get(2) : null;
            String key2 = entry2.getKey().size() > 2 ? entry2.getKey().get(2) : null;

            // 按排序规则进行比较
            int index1 = key1 != null ? ORDER.indexOf(key1) : Integer.MAX_VALUE;
            int index2 = key2 != null ? ORDER.indexOf(key2) : Integer.MAX_VALUE;

            return Integer.compare(index1, index2);
        });

        // 将排序后的条目插入到LinkedHashMap中
        for (Map.Entry<List<String>, List<Record>> entry : entryList) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }


    public static void main(String[] args) {
        // 示例用法
        HashMap<List<String>, List<Record>> map = new HashMap<>();

        // 示例数据
        map.put(Arrays.asList("A", "B", "Shiftly"), new ArrayList<>());
        map.put(Arrays.asList("C", "D", "Monthly"), new ArrayList<>());
        map.put(Arrays.asList("E", "F", "Weekly"), new ArrayList<>());
        map.put(Arrays.asList("G", "H", "Yearly"), new ArrayList<>());

        // 排序
        Map<List<String>, List<Record>> sortedMap = sortMapByKey(map);

        // 输出结果
        for (Map.Entry<List<String>, List<Record>> entry : sortedMap.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }
}
