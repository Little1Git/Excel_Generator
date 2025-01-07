package excel_processor;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OtherInfo {
    public String station_index;
    public String document;
    public String prepare;
    public String review;
    public String approve;
    public String edition;
}
