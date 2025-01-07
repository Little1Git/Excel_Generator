//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package excel_processor;

import java.util.*;
import java.util.stream.Collectors;

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

    public Record() {
    }

    public Record(String a_id, String checkvalue, String date, String remark, String doexecuteby, String lineindex, String checkexecuteby, String stationindex, String linename, String shift, String stationname, String submitdate, String no, String whattomaintain, String interval, String costtime, String doby, String checkby) {
        this.a_id = a_id;
        this.checkvalue = checkvalue;
        this.date = date;
        this.remark = remark;
        this.doexecuteby = doexecuteby;
        this.lineindex = lineindex;
        this.checkexecuteby = checkexecuteby;
        this.stationindex = stationindex;
        this.linename = linename;
        this.shift = shift;
        this.stationname = stationname;
        this.submitdate = submitdate;
        this.no = no;
        this.whattomaintain = whattomaintain;
        this.interval = interval;
        this.costtime = costtime;
        this.doby = doby;
        this.checkby = checkby;
    }

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
        map_of_stations_confirm.forEach((stationName, stationMap) -> {
            stationMap.forEach((interval, records) -> {
                Map<String, Record> uniqueByCheckexecuteby = new HashMap();
                records.forEach((record) -> {
                    uniqueByCheckexecuteby.put(record.getCheckexecuteby(), record);
                });
                uniqueByCheckexecuteby.forEach((key, value) -> {
                });
                stationMap.put(interval, new ArrayList(uniqueByCheckexecuteby.values()));
            });
        });
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

    public String getA_id() {
        return this.a_id;
    }

    public String getCheckvalue() {
        return this.checkvalue;
    }

    public String getDate() {
        return this.date;
    }

    public String getRemark() {
        return this.remark;
    }

    public String getDoexecuteby() {
        return this.doexecuteby;
    }

    public String getLineindex() {
        return this.lineindex;
    }

    public String getCheckexecuteby() {
        return this.checkexecuteby;
    }

    public String getStationindex() {
        return this.stationindex;
    }

    public String getLinename() {
        return this.linename;
    }

    public String getShift() {
        return this.shift;
    }

    public String getStationname() {
        return this.stationname;
    }

    public String getSubmitdate() {
        return this.submitdate;
    }

    public String getNo() {
        return this.no;
    }

    public String getWhattomaintain() {
        return this.whattomaintain;
    }

    public String getInterval() {
        return this.interval;
    }

    public String getCosttime() {
        return this.costtime;
    }

    public String getDoby() {
        return this.doby;
    }

    public String getCheckby() {
        return this.checkby;
    }

    public void setA_id(String a_id) {
        this.a_id = a_id;
    }

    public void setCheckvalue(String checkvalue) {
        this.checkvalue = checkvalue;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public void setDoexecuteby(String doexecuteby) {
        this.doexecuteby = doexecuteby;
    }

    public void setLineindex(String lineindex) {
        this.lineindex = lineindex;
    }

    public void setCheckexecuteby(String checkexecuteby) {
        this.checkexecuteby = checkexecuteby;
    }

    public void setStationindex(String stationindex) {
        this.stationindex = stationindex;
    }

    public void setLinename(String linename) {
        this.linename = linename;
    }

    public void setShift(String shift) {
        this.shift = shift;
    }

    public void setStationname(String stationname) {
        this.stationname = stationname;
    }

    public void setSubmitdate(String submitdate) {
        this.submitdate = submitdate;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public void setWhattomaintain(String whattomaintain) {
        this.whattomaintain = whattomaintain;
    }

    public void setInterval(String interval) {
        this.interval = interval;
    }

    public void setCosttime(String costtime) {
        this.costtime = costtime;
    }

    public void setDoby(String doby) {
        this.doby = doby;
    }

    public void setCheckby(String checkby) {
        this.checkby = checkby;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof Record)) {
            return false;
        } else {
            Record other = (Record)o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                Object this$a_id = this.getA_id();
                Object other$a_id = other.getA_id();
                if (this$a_id == null) {
                    if (other$a_id != null) {
                        return false;
                    }
                } else if (!this$a_id.equals(other$a_id)) {
                    return false;
                }

                Object this$checkvalue = this.getCheckvalue();
                Object other$checkvalue = other.getCheckvalue();
                if (this$checkvalue == null) {
                    if (other$checkvalue != null) {
                        return false;
                    }
                } else if (!this$checkvalue.equals(other$checkvalue)) {
                    return false;
                }

                Object this$date = this.getDate();
                Object other$date = other.getDate();
                if (this$date == null) {
                    if (other$date != null) {
                        return false;
                    }
                } else if (!this$date.equals(other$date)) {
                    return false;
                }

                label206: {
                    Object this$remark = this.getRemark();
                    Object other$remark = other.getRemark();
                    if (this$remark == null) {
                        if (other$remark == null) {
                            break label206;
                        }
                    } else if (this$remark.equals(other$remark)) {
                        break label206;
                    }

                    return false;
                }

                label199: {
                    Object this$doexecuteby = this.getDoexecuteby();
                    Object other$doexecuteby = other.getDoexecuteby();
                    if (this$doexecuteby == null) {
                        if (other$doexecuteby == null) {
                            break label199;
                        }
                    } else if (this$doexecuteby.equals(other$doexecuteby)) {
                        break label199;
                    }

                    return false;
                }

                Object this$lineindex = this.getLineindex();
                Object other$lineindex = other.getLineindex();
                if (this$lineindex == null) {
                    if (other$lineindex != null) {
                        return false;
                    }
                } else if (!this$lineindex.equals(other$lineindex)) {
                    return false;
                }

                label185: {
                    Object this$checkexecuteby = this.getCheckexecuteby();
                    Object other$checkexecuteby = other.getCheckexecuteby();
                    if (this$checkexecuteby == null) {
                        if (other$checkexecuteby == null) {
                            break label185;
                        }
                    } else if (this$checkexecuteby.equals(other$checkexecuteby)) {
                        break label185;
                    }

                    return false;
                }

                label178: {
                    Object this$stationindex = this.getStationindex();
                    Object other$stationindex = other.getStationindex();
                    if (this$stationindex == null) {
                        if (other$stationindex == null) {
                            break label178;
                        }
                    } else if (this$stationindex.equals(other$stationindex)) {
                        break label178;
                    }

                    return false;
                }

                Object this$linename = this.getLinename();
                Object other$linename = other.getLinename();
                if (this$linename == null) {
                    if (other$linename != null) {
                        return false;
                    }
                } else if (!this$linename.equals(other$linename)) {
                    return false;
                }

                Object this$shift = this.getShift();
                Object other$shift = other.getShift();
                if (this$shift == null) {
                    if (other$shift != null) {
                        return false;
                    }
                } else if (!this$shift.equals(other$shift)) {
                    return false;
                }

                label157: {
                    Object this$stationname = this.getStationname();
                    Object other$stationname = other.getStationname();
                    if (this$stationname == null) {
                        if (other$stationname == null) {
                            break label157;
                        }
                    } else if (this$stationname.equals(other$stationname)) {
                        break label157;
                    }

                    return false;
                }

                label150: {
                    Object this$submitdate = this.getSubmitdate();
                    Object other$submitdate = other.getSubmitdate();
                    if (this$submitdate == null) {
                        if (other$submitdate == null) {
                            break label150;
                        }
                    } else if (this$submitdate.equals(other$submitdate)) {
                        break label150;
                    }

                    return false;
                }

                Object this$no = this.getNo();
                Object other$no = other.getNo();
                if (this$no == null) {
                    if (other$no != null) {
                        return false;
                    }
                } else if (!this$no.equals(other$no)) {
                    return false;
                }

                label136: {
                    Object this$whattomaintain = this.getWhattomaintain();
                    Object other$whattomaintain = other.getWhattomaintain();
                    if (this$whattomaintain == null) {
                        if (other$whattomaintain == null) {
                            break label136;
                        }
                    } else if (this$whattomaintain.equals(other$whattomaintain)) {
                        break label136;
                    }

                    return false;
                }

                Object this$interval = this.getInterval();
                Object other$interval = other.getInterval();
                if (this$interval == null) {
                    if (other$interval != null) {
                        return false;
                    }
                } else if (!this$interval.equals(other$interval)) {
                    return false;
                }

                label122: {
                    Object this$costtime = this.getCosttime();
                    Object other$costtime = other.getCosttime();
                    if (this$costtime == null) {
                        if (other$costtime == null) {
                            break label122;
                        }
                    } else if (this$costtime.equals(other$costtime)) {
                        break label122;
                    }

                    return false;
                }

                Object this$doby = this.getDoby();
                Object other$doby = other.getDoby();
                if (this$doby == null) {
                    if (other$doby != null) {
                        return false;
                    }
                } else if (!this$doby.equals(other$doby)) {
                    return false;
                }

                Object this$checkby = this.getCheckby();
                Object other$checkby = other.getCheckby();
                if (this$checkby == null) {
                    if (other$checkby != null) {
                        return false;
                    }
                } else if (!this$checkby.equals(other$checkby)) {
                    return false;
                }

                return true;
            }
        }
    }

    protected boolean canEqual(Object other) {
        return other instanceof Record;
    }

    public String toString() {
        String var10000 = this.getA_id();
        return "Record(a_id=" + var10000 + ", checkvalue=" + this.getCheckvalue() + ", date=" + this.getDate() + ", remark=" + this.getRemark() + ", doexecuteby=" + this.getDoexecuteby() + ", lineindex=" + this.getLineindex() + ", checkexecuteby=" + this.getCheckexecuteby() + ", stationindex=" + this.getStationindex() + ", linename=" + this.getLinename() + ", shift=" + this.getShift() + ", stationname=" + this.getStationname() + ", submitdate=" + this.getSubmitdate() + ", no=" + this.getNo() + ", whattomaintain=" + this.getWhattomaintain() + ", interval=" + this.getInterval() + ", costtime=" + this.getCosttime() + ", doby=" + this.getDoby() + ", checkby=" + this.getCheckby() + ")";
    }
}
