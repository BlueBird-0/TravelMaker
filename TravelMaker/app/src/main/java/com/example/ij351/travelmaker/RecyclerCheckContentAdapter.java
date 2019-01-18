package com.example.ij351.travelmaker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import java.util.List;

public class RecyclerCheckContentAdapter extends RecyclerView.Adapter<RecyclerCheckContentAdapter.ViewHolder> {
    private List<Content> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    RecyclerCheckContentAdapter(Context context, List<Content> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_checklist_content, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        String comment_str = mData.get(position).comment;
        holder.comment.setText(comment_str);

        holder.mainLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //롱클릭 액티비티 실행
                Intent intent = new Intent(mInflater.getContext(), LongClickActivity.class);
                Bundle extras = new Bundle();
                extras.putInt("state", LongClickActivity.STATE_DEL_CHECKLIST);
                extras.putString("title", mData.get(position).title);
                extras.putString("documentId", mData.get(position).uid);
                intent.putExtras( extras );
                mInflater.getContext().startActivity(intent);
                return false;
            }
        });
        //String writer_str = mData.get(position).writer;
        holder.writer.setText("");
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView comment;
        TextView writer;
        ConstraintLayout mainLayout;

        ViewHolder(View itemView) {
            super(itemView);
            comment = itemView.findViewById(R.id.content);
            writer = itemView.findViewById(R.id.writer);
            mainLayout = itemView.findViewById(R.id.mainLayout);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    Content getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
