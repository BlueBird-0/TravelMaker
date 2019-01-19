package com.example.ij351.travelmaker;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class RecyclerPartAdapter extends RecyclerView.Adapter<RecyclerPartAdapter.ViewHolder> {
    private List<User> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    RecyclerPartAdapter(Context context, List<User> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_participants, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        User user = mData.get(position);
        holder.contentComment.setText(user.name);
        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mData.get(position).uid.equals(User.getFirebaseUser().getUid()))
                {
                    Intent intent = new Intent(mInflater.getContext(), ParticipantActivity.class);
                    mInflater.getContext().startActivity(intent);
                }
            }
        });

        if(user.hasPassport == false)
        {
            holder.contentState.setBackgroundResource(R.drawable.ic_passport);
        }else if(user.hasVISA == false)
        {
            holder.contentState.setBackgroundResource(R.drawable.ic_visa);
        }else
        {
            holder.contentState.setBackgroundResource(R.drawable.ic_ok);
        }
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView contentComment;
        Button contentState;
        ConstraintLayout constraintLayout;

        ViewHolder(View itemView) {
            super(itemView);
            contentComment = itemView.findViewById(R.id.textView_contentComment);
            contentState = itemView.findViewById(R.id.button_contentState);
            constraintLayout = itemView.findViewById(R.id.constraint_participants);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    User getItem(int id) {
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
