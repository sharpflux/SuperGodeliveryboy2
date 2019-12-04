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

public class HistoryFragmentAdapter extends RecyclerView.Adapter<HistoryFragmenHolder>  {

    private Context mContext;
    public String id;
    private static int currentPosition = 0;


    private ArrayList<HistoryModel> mList;

    public HistoryFragmentAdapter(Context mContext, ArrayList<HistoryModel> mList) {
        this.mContext = mContext;
        this.mList = mList;

    }

    @Override
    public HistoryFragmenHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_view, parent, false);
            return new HistoryFragmenHolder(mView);


    }

    @Override
    public void onBindViewHolder(HistoryFragmenHolder holder, final int position) {

                     holder.mTotal.setText(mList.get(position).getTotalAmt());
                     holder.mfromDate.setText(mList.get(position).getFromDate());
                     holder.mtoDate.setText(mList.get(position).getToDate());



    }



    @Override
    public int getItemCount() {
        return  mList.size();
    }


}

class HistoryFragmenHolder extends RecyclerView.ViewHolder {


    TextView mTotal,mfromDate,mtoDate;
    List<HistoryModel> mlist;

    HistoryFragmenHolder(View itemView) {
        super(itemView);


        mTotal = itemView.findViewById(R.id.txt_total);
        mfromDate = itemView.findViewById(R.id.txt_fromDate);
        mtoDate = itemView.findViewById(R.id.txt_toDate);


    }






}
