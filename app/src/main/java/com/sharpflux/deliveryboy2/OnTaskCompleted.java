package com.sharpflux.deliveryboy2;

public interface OnTaskCompleted {
    void onTaskCompleted(String...values);
    void onTaskCompletedHolder(String values, DeliveryMainAdapter.ProductViewHolder...holder);
}
