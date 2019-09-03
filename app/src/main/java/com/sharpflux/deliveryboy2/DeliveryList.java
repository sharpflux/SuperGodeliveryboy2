package com.sharpflux.deliveryboy2;

public class DeliveryList {


    private  int DeliveryId;

    public DeliveryList(String mobile) {


        this.mobile = mobile;
    }

    public int getDeliveryId() {
        return DeliveryId;
    }

    public int getCustomerId() {
        return CustomerId;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public String getPickupAddress() {
        return pickupAddress;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public String getFromLat() {
        return fromLat;
    }

    public String getFromLang() {
        return fromLang;
    }



    public String getProduct() {
        return product;
    }

    public String getPickupDate() {
        return pickupDate;
    }

    public String getPickuptime() {
        return pickuptime;
    }

    public String getCpName() {
        return cpName;
    }

    public String getMobile() {
        return mobile;
    }

    public String getAlternatemobile() {
        return alternatemobile;
    }

    public String getPaymenttype() {
        return paymenttype;
    }

    public String getToLat() {
        return ToLat;
    }

    public String getToLong() {
        return ToLong;
    }

    public String getDistance() {
        return Distance;
    }

    public String getTDuration() {
        return Duration;
    }

    public  String getTotalCharges(){
        return  TotalCharges;
    }

    public  String getCustomerFullName(){
        return  CustomerFullName;
    }

    private int     CustomerId;
    private String  vehicleType;
    private  String pickupAddress;
    private String  deliveryAddress;
    private String  fromLat;
    private String  fromLang;
    private String  product;
    private String  pickupDate;
    private String  pickuptime;
    private String  cpName;
    private String  mobile;
    private String  alternatemobile;
    private String  paymenttype;
    private String  ToLat;
    private String  ToLong;
    private String  Distance;
    private String  Duration;
    private String TotalCharges;
    private  String CustomerFullName;


    public DeliveryList(int deliveryId, int customerId, String vehicleType, String pickupAddress, String deliveryAddress, String fromLat, String fromLang,

                        String product, String pickupDate, String pickuptime, String cpName, String mobile, String alternatemobile, String paymenttype
    ,String ToLat,String ToLong,String Distance,String Duration,String TotalCharges, String CustomerFullName


    ) {
        DeliveryId = deliveryId;
        CustomerId = customerId;
        this.vehicleType = vehicleType;
        this.pickupAddress = pickupAddress;
        this.deliveryAddress = deliveryAddress;
        this.fromLat = fromLat;
        this.fromLang = fromLang;
        this.product = product;
        this.pickupDate = pickupDate;
        this.pickuptime = pickuptime;
        this.cpName = cpName;
        this.mobile = mobile;
        this.alternatemobile = alternatemobile;
        this.paymenttype = paymenttype;
        this.ToLat = ToLat;
        this.ToLong = ToLong;
        this.Distance = Distance;
        this.Duration = Duration;
        this.TotalCharges=TotalCharges;
        this.CustomerFullName=CustomerFullName;

    }









}
