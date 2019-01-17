package com.example.ij351.travelmaker;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class RecyclerCostAdapter extends RecyclerView.Adapter<RecyclerCostAdapter.ViewHolder> {
    private List<Cost> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    RecyclerCostAdapter(Context context, List<Cost> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }
    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_cost, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Cost cost= mData.get(position);
        SimpleDateFormat datef= new SimpleDateFormat("MM.dd");
        String time_str = datef.format(cost.timestamp.toDate());

        holder.content.setText(cost.content);
        holder.time.setText(time_str);
        holder.cost.setText(String.valueOf(cost.cost)+"￦");
        //글쓰기 버튼 누를 때 동작
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView time;
        TextView content;
        TextView cost;

        ViewHolder(View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.cost_time);
            content = itemView.findViewById(R.id.cost_content);
            cost = itemView.findViewById(R.id.cost_cost);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    Cost getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(RecyclerCostAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
