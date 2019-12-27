package com.sharpflux.deliveryboy2;

public class RateShowModal {
    String VehicleType;
    String DayBaseFare;
    String Day0To5Price;
    String Day10To15Price;
    String Day5To10Price;
    String DayAbove15;
    String DayPerMinCharges;


    String Night0To5Price;
    String Night10To15Price;
    String Night5To10Price;
    String NightAbove15;
    String NightBaseFare;
    String NightPerMinCharges;

    public RateShowModal(String vehicleType, String dayBaseFare, String day0To5Price, String day10To15Price, String day5To10Price, String dayAbove15, String dayPerMinCharges, String night0To5Price, String night10To15Price, String night5To10Price, String nightAbove15, String nightBaseFare, String nightPerMinCharges) {
        VehicleType = vehicleType;
        DayBaseFare = dayBaseFare;
        Day0To5Price = day0To5Price;
        Day10To15Price = day10To15Price;
        Day5To10Price = day5To10Price;
        DayAbove15 = dayAbove15;
        DayPerMinCharges = dayPerMinCharges;
        Night0To5Price = night0To5Price;
        Night10To15Price = night10To15Price;
        Night5To10Price = night5To10Price;
        NightAbove15 = nightAbove15;
        NightBaseFare = nightBaseFare;
        NightPerMinCharges = nightPerMinCharges;
    }

    public String getVehicleType() {
        return VehicleType;
    }

    public void setVehicleType(String vehicleType) {
        VehicleType = vehicleType;
    }

    public String getDayBaseFare() {
        return DayBaseFare;
    }

    public void setDayBaseFare(String dayBaseFare) {
        DayBaseFare = dayBaseFare;
    }

    public String getDay0To5Price() {
        return Day0To5Price;
    }

    public void setDay0To5Price(String day0To5Price) {
        Day0To5Price = day0To5Price;
    }

    public String getDay10To15Price() {
        return Day10To15Price;
    }

    public void setDay10To15Price(String day10To15Price) {
        Day10To15Price = day10To15Price;
    }

    public String getDay5To10Price() {
        return Day5To10Price;
    }

    public void setDay5To10Price(String day5To10Price) {
        Day5To10Price = day5To10Price;
    }

    public String getDayAbove15() {
        return DayAbove15;
    }

    public void setDayAbove15(String dayAbove15) {
        DayAbove15 = dayAbove15;
    }

    public String getDayPerMinCharges() {
        return DayPerMinCharges;
    }

    public void setDayPerMinCharges(String dayPerMinCharges) {
        DayPerMinCharges = dayPerMinCharges;
    }

    public String getNight0To5Price() {
        return Night0To5Price;
    }

    public void setNight0To5Price(String night0To5Price) {
        Night0To5Price = night0To5Price;
    }

    public String getNight10To15Price() {
        return Night10To15Price;
    }

    public void setNight10To15Price(String night10To15Price) {
        Night10To15Price = night10To15Price;
    }

    public String getNight5To10Price() {
        return Night5To10Price;
    }

    public void setNight5To10Price(String night5To10Price) {
        Night5To10Price = night5To10Price;
    }

    public String getNightAbove15() {
        return NightAbove15;
    }

    public void setNightAbove15(String nightAbove15) {
        NightAbove15 = nightAbove15;
    }

    public String getNightBaseFare() {
        return NightBaseFare;
    }

    public void setNightBaseFare(String nightBaseFare) {
        NightBaseFare = nightBaseFare;
    }

    public String getNightPerMinCharges() {
        return NightPerMinCharges;
    }

    public void setNightPerMinCharges(String nightPerMinCharges) {
        NightPerMinCharges = nightPerMinCharges;
    }
}
