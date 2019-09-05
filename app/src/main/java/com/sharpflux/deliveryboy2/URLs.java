package com.sharpflux.deliveryboy2;

public class URLs {
    private static final String ROOT_URL = "http://192.168.101.1/Android/Api.php?apicall=";

    public static final String URL_REGISTER = "http://api.supergo.in/api/DeliveryBoy/DriverRegistration";

    public static final String URL_LOGIN= "http://api.supergo.in/api/DeliveryBoyLogin/DeliveryBoyLogin";

    public  static  final String URL_DELIVERIES="http://api.supergo.in/api/GetAllDelivery/DeliveryRequestsall";

    public  static  final String URL_STATUS="http://api.supergo.in/api/DeliveryLog/InsertDelivery";

    public static final String URL_OTP="http://admin.supergo.in/Utilities/OTPGenerate";
    public static final String URL_LOCATIONUPDATE="http://admin.supergo.in/api/Location/LocationUpdate";
    public static final String URL_SENNOTIFICATIONTOCUSTOMER="http://admin.supergo.in/api/notfiy/notfiyToCustomer";
    public static final String URL_GETONGOINGDELIVERY="http://admin.supergo.in/api/GetOngoingDelivery?DeliveryBoyId=";
    public static final String URL_RESETPASS="http://admin.supergo.in/api/User/ChangePassword";

}
