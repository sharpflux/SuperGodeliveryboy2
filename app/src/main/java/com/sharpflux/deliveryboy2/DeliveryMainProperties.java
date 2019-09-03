package com.sharpflux.deliveryboy2;

 class DeliveryMainProperties {

        private  int DeliveryId;
     private int CustomerId;
     private int VehicleTypeId;
     private  String PickupDateTime;
     private String PickupFromLocationName;
     private String LandMark;
     private String FromLat;
     private String FromLong;
     private String DropLocationName;

     private String ToLat;
     private String ToLong;
     private String PickupPersonName;
     private String PickupPersonContact;
     private String RecipientPersonName;
     private String RecipientContact;

     private String RecipientAlternameContact;




     public DeliveryMainProperties(
             int DeliveryId,
             int CustomerId,
             int VehicleTypeId,
             String PickupDateTime,
             String PickupFromLocationName,
             String LandMark,
             String FromLat,
             String FromLong,
             String DropLocationName,
             String ToLat,
             String ToLong,
             String PickupPersonName,
             String PickupPersonContact,
             String RecipientPersonName,
             String RecipientContact,
             String RecipientAlternameContact,

             String recipientAlternameContact) {


         this.DeliveryId=DeliveryId;
         this.CustomerId=CustomerId;
         this.VehicleTypeId=VehicleTypeId;
         this.PickupDateTime=PickupDateTime;
         this.PickupFromLocationName=PickupFromLocationName;
         this.LandMark=LandMark;
         this.FromLat=FromLat;
         this.FromLong=FromLong;
         this.DropLocationName=DropLocationName;
         this.ToLat=ToLat;
         this.ToLong=ToLong;
         this.PickupPersonName=PickupPersonName;
         this.PickupPersonContact=PickupPersonContact;
         this.RecipientPersonName=RecipientPersonName;
         this.RecipientContact=RecipientContact;
         this.RecipientAlternameContact=RecipientAlternameContact;
    }
     public int getDeliveryId() {
         return DeliveryId;
     }

     public int getCustomerId() {
         return CustomerId;
     }

     public int getVehicleTypeId() {
         return VehicleTypeId;
     }

     public String getPickupDateTime() {
         return PickupDateTime;
     }

     public String getPickupFromLocationName() {
         return PickupFromLocationName;
     }

     public String getLandMark() {
         return LandMark;
     }

     public String getFromLat() {
         return FromLat;
     }

     public String getFromLong() {
         return FromLong;
     }

     public String getDropLocationName() {
         return DropLocationName;
     }

     public String getToLat() {
         return ToLat;
     }

     public String getToLong() {
         return ToLong;
     }

     public String getPickupPersonName() {
         return PickupPersonName;
     }

     public String getPickupPersonContact() {
         return PickupPersonContact;
     }

     public String getRecipientPersonName() {
         return RecipientPersonName;
     }

     public String getRecipientContact() {
         return RecipientContact;
     }

     public String getRecipientAlternameContact() {
         return RecipientAlternameContact;
     }
}