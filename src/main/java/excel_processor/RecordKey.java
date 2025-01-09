package excel_processor;

import java.util.Objects;

public class RecordKey {
    private String checkexecuteby;
    private String date;

    public RecordKey(String checkexecuteby, String date) {
        this.checkexecuteby = checkexecuteby;
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecordKey recordKey = (RecordKey) o;
        return Objects.equals(checkexecuteby, recordKey.checkexecuteby) &&
                Objects.equals(date, recordKey.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(checkexecuteby, date);
    }
}
