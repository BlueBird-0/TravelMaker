package com.example.ij351.travelmaker;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class RecyclerBillAdapter extends RecyclerView.Adapter<RecyclerBillAdapter.ViewHolder> {
    private List<Bill> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;


    // data is passed into the constructor
    RecyclerBillAdapter(Context context, List<Bill> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }
    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_bill, parent, false);

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        StorageReference mountainImagesRef = storageRef.child("images/"+TravelRoom.roomId+mData);

        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        SimpleDateFormat datef= new SimpleDateFormat("YYYY.MM.dd");
        String time_str = datef.format(mData.get(position).time.toDate());
        holder.time.setText(time_str);

        Log.d("test001", "getImage");
        Glide.with(mInflater.getContext().getApplicationContext())
                .load(mData.get(position).uri)
                .addListener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        ProgressBar progressBar = (ProgressBar)holder.itemView.findViewById(R.id.progressBar_main);
                        progressBar.setVisibility(View.INVISIBLE);
                        return false;
                    }
                })
                .apply(RequestOptions.centerCropTransform())
                .into(holder.imageView);


        //롱클릭 이벤트
        holder.mainLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //롱클릭 액티비티 실행
                Intent intent = new Intent(mInflater.getContext(), LongClickActivity.class);
                Bundle extras = new Bundle();
                extras.putInt("state", LongClickActivity.STATE_DEL_BILL);
                extras.putString("documentId", mData.get(position).documentId);
                extras.putString("storageFileName", "bill_"+String.valueOf(mData.get(position).time.getSeconds()));
                intent.putExtras( extras );
                mInflater.getContext().startActivity(intent);
                return false;
            }
        });
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView time;
        ImageView imageView;
        ConstraintLayout mainLayout;

        ViewHolder(View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.textView_bill_time);
            imageView = itemView.findViewById(R.id.textView_bill_image);
            mainLayout = itemView.findViewById(R.id.layout_main);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    Bill getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(RecyclerBillAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
