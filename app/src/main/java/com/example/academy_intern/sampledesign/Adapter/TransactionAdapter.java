package com.example.academy_intern.sampledesign.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.academy_intern.sampledesign.Model.ItemProfile;
import com.example.academy_intern.sampledesign.R;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>
{

    private ArrayList<ItemProfile> transactionsList;
    private String itemName, itemPrice, date, time;

    public TransactionAdapter(ArrayList<ItemProfile> transactionsList)
    {
        this.transactionsList = transactionsList;
    }

    @Override
    public TransactionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.row_transaction, parent, false);
        return new TransactionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TransactionViewHolder holder, int position)
    {
        itemName = "Item: " + transactionsList.get(position).getItemName();
        itemPrice = "Price: " + transactionsList.get(position).getItemPrice() + " Points";
        date = " " + StringUtils.substring(transactionsList.get(position).getDateTime(), 0, 10);
        time = " " + StringUtils.substring(transactionsList.get(position).getDateTime(), 11, 16);
        holder.tvItemName.setText(itemName);
        holder.tvItemPrice.setText(itemPrice);
        holder.tvDate.setText(date);
        holder.tvTime.setText(time);
    }

    @Override
    public int getItemCount() {
        return transactionsList.size();
    }

    class TransactionViewHolder extends RecyclerView.ViewHolder
    {

        TextView tvItemName, tvItemPrice, tvDate, tvTime;

        TransactionViewHolder(View itemView)
        {
            super(itemView);
            tvItemName = itemView.findViewById(R.id.tv_item_name);
            tvItemPrice = itemView.findViewById(R.id.tv_item_price);
            tvDate = itemView.findViewById(R.id.tv_date_of_purchase);
            tvTime = itemView.findViewById(R.id.tv_time_of_purchase);
        }
    }
}