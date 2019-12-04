package com.sharpflux.deliveryboy2;

public class MyOrderModel {

    String orderId;
    String orderStatus;
    String distace;
    String time;
    String total;
    String pickupAddress;
    String dropAddress;

    public MyOrderModel(String orderId, String orderStatus,
                        String distace, String time, String total, String pickupAddress, String dropAddress) {
        this.orderId = orderId;
        this.orderStatus = orderStatus;
        this.distace = distace;
        this.time = time;
        this.total = total;
        this.pickupAddress = pickupAddress;
        this.dropAddress = dropAddress;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getDistace() {
        return distace;
    }

    public void setDistace(String distace) {
        this.distace = distace;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getPickupAddress() {
        return pickupAddress;
    }

    public void setPickupAddress(String pickupAddress) {
        this.pickupAddress = pickupAddress;
    }

    public String getDropAddress() {
        return dropAddress;
    }

    public void setDropAddress(String dropAddress) {
        this.dropAddress = dropAddress;
    }
}
