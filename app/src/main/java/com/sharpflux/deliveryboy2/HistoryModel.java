package com.sharpflux.deliveryboy2;

public class HistoryModel {

    String totalAmt;
    String fromDate;
    String toDate;

    public HistoryModel(String totalAmt, String fromDate, String toDate) {
        this.totalAmt = totalAmt;
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    public String getTotalAmt() {
        return totalAmt;
    }

    public void setTotalAmt(String totalAmt) {
        this.totalAmt = totalAmt;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }
}
