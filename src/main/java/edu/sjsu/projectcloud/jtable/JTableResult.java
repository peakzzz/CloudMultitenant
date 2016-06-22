package edu.sjsu.projectcloud.jtable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;


public class JTableResult<T> {
    @JsonProperty("Result")
    private String result = "OK";

    @JsonProperty("Records")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<T> records = null;

    @JsonProperty("Record")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T record = null;

    private boolean forceArray = false;

//    @JsonProperty("TotalRecordCount")
//    public int getTotalRecordCount() {
//        return records.size();
//    }

    public JTableResult(boolean forceArray) {
        this.forceArray = forceArray;
    }

    public JTableResult() {
        this(false);
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public List<T> getRecords() {
        return records;
    }

    public void addRecord(T rec) {
        if (record == null && records == null && forceArray == false) {
            record = rec;
        } else {
            if (records == null) {
                records = new ArrayList<>();
                if (forceArray == false) {
                    records.add(record);
                }
                record = null;
            }
            records.add(rec);
        }
    }

}
