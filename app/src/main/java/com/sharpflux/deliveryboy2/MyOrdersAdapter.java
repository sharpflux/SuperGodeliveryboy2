package com.sharpflux.deliveryboy2;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

class MyOrdersAdapter extends RecyclerView.Adapter<MyOrderHolder>  {

    private Context mContext;
    public String id;
    private static int currentPosition = 0;


    private ArrayList<MyOrderModel> mList;

    public MyOrdersAdapter(Context mContext, ArrayList<MyOrderModel> mList) {
        this.mContext = mContext;
        this.mList = mList;

    }

    @Override
    public MyOrderHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_ordere_view, parent, false);
        return new MyOrderHolder(mView);


    }

    @Override
    public void onBindViewHolder(MyOrderHolder holder, final int position) {



        holder.mOrderId.setText(mList.get(position).getOrderId());
        holder.mOrdereStatus.setText(mList.get(position).getOrderStatus());
        holder.mDistance.setText(mList.get(position).getDistace());
        holder.mDuration.setText(mList.get(position).getTime());
        holder.mTotal.setText(mList.get(position).getTotal()+"₹");
        holder.mPick.setText(mList.get(position).getPickupAddress());
        holder.mDrop.setText(mList.get(position).getDropAddress());
        holder.mComm.setText(mList.get(position).getCompanyCommission()+"₹");
        holder.mBoyEarning.setText(mList.get(position).getDeliveryBoyCommission()+"₹");




    }



    @Override
    public int getItemCount() {
        return  mList.size();
    }


}

class MyOrderHolder extends RecyclerView.ViewHolder {


    TextView mOrderId,mOrdereStatus,mDistance,mDuration,mTotal,mPick,mDrop,mComm,mBoyEarning,mgstAmt;

    String GstAmt;
    List<MyOrderModel> mlist;

    MyOrderHolder(View itemView) {
        super(itemView);


        mOrderId = itemView.findViewById(R.id.tv_OrderId1);
        mOrdereStatus = itemView.findViewById(R.id.tv_Status2);
        mDistance = itemView.findViewById(R.id.tv_Distance3);
        mDuration = itemView.findViewById(R.id.tv_Duration2);
        mTotal = itemView.findViewById(R.id.tv_TotalCharges3);
        mPick = itemView.findViewById(R.id.txt_pickup_location1);
        mDrop= itemView.findViewById(R.id.tv_Drop_location1);
        mComm= itemView.findViewById(R.id.tv_Comm);
        mBoyEarning= itemView.findViewById(R.id.tv_boyEarn);


    }






}
