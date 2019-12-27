package com.sharpflux.deliveryboy2;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class RateShowAdapter extends RecyclerView.Adapter<RateShowAdapterHolder>  {

    private Context mContext;
    public String id;
    private static int currentPosition = 0;


    private ArrayList<RateShowModalKeyValue> mList;

    public RateShowAdapter(Context mContext, ArrayList<RateShowModalKeyValue> mList) {
        this.mContext = mContext;
        this.mList = mList;

    }

    @Override
    public RateShowAdapterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.rate_view, parent, false);
            return new RateShowAdapterHolder(mView);


    }

    @Override
    public void onBindViewHolder(@NonNull RateShowAdapterHolder holder, int position) {
        //holder.mTotal.setText(mList.get(position).getTotalAmt()+"₹");
       // holder.mfromDate.setText(mList.get(position).getFromDate());
     //   holder.mtoDate.setText(mList.get(position).getToDate());

        holder.txtHeading.setText(mList.get(position).getKey());
        holder.txtValue.setText(mList.get(position).getValue());

    }





    @Override
    public int getItemCount() {
        return  mList.size();
    }


}

class RateShowAdapterHolder extends RecyclerView.ViewHolder {


    TextView txtHeading,txtValue,mtoDate;
    List<HistoryModel> mlist;

    RateShowAdapterHolder(View itemView) {
        super(itemView);
        txtHeading = itemView.findViewById(R.id.txtHeading);
        txtValue = itemView.findViewById(R.id.txtValue);



    }






}
