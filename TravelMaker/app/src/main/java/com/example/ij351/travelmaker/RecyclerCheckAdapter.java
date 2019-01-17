package com.example.ij351.travelmaker;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class RecyclerCheckAdapter extends RecyclerView.Adapter<RecyclerCheckAdapter.ViewHolder> {
    private List<String> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private String TAG = "RecyclerCheckAdapter";


    // data is passed into the constructor
    RecyclerCheckAdapter(Context context, List<String> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_checklists, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final String title = mData.get(position);
        holder.myTextView.setText(title);
        //글쓰기 버튼 누를 때 동작
        holder.write_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConstraintLayout keyboard = (ConstraintLayout)holder.itemView.getRootView().findViewById(R.id.layout_keyboard);
                keyboard.setVisibility(View.VISIBLE);

                EditText content = (EditText)holder.itemView.getRootView().findViewById(R.id.edit_keyboard);
                content.requestFocus();
                Button send = (Button)holder.itemView.getRootView().findViewById(R.id.button_key_send);

                final InputMethodManager imm = (InputMethodManager)mInflater.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(content, InputMethodManager.SHOW_FORCED);
                //글쓰기 누를 때
                send.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText content = (EditText)holder.itemView.getRootView().findViewById(R.id.edit_keyboard);
                        if(content.getText().toString().trim().length() == 0) {
                            content.requestFocus();
                            return;
                        }
                        TravelRoom.writeContent(title, content.getText().toString());
                        content.setText("");
                        imm.hideSoftInputFromWindow(holder.itemView.getWindowToken(), 0); //키보드 접기
                    }
                });
            }
        });



        //contents 가져오기
        final RecyclerCheckContentAdapter adapter_content;
        final ArrayList<Content> contents = new ArrayList<>();       //2차원 배열
        // set up the RecyclerView
        holder.recyclerView_content.setLayoutManager(new LinearLayoutManager(mInflater.getContext()));
        adapter_content = new RecyclerCheckContentAdapter(mInflater.getContext(), contents);
        //adapter.setClickListener(this);
        holder.recyclerView_content.setAdapter(adapter_content);
        TravelRoom.db.collection("Travels").document(TravelRoom.roomId)
                .collection("CheckLists").document(title).collection("contents")
                .orderBy("time")
                .addSnapshotListener(new EventListener<QuerySnapshot>(){
                    @Override
                    public void onEvent(@javax.annotation.Nullable QuerySnapshot value, @javax.annotation.Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "Listen failed.", e);
                            return;
                        }

                        contents.clear();
                        for (QueryDocumentSnapshot doc : value) {
                            if (doc.getString("comment") != null || doc.getTimestamp("time") != null || doc.getString("writer") != null) {
                                String comment = doc.getString("comment");
                                Timestamp time = doc.getTimestamp("time");
                                String writer = doc.getString("writer");

                                Content newContent = new Content(doc.getId(), comment, time, writer);
                                contents.add(newContent);
                            }
                        }
                        adapter_content.notifyDataSetChanged();     //Adapter 새로고침
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
        TextView myTextView;
        Button write_btn;
        RecyclerView recyclerView_content;
        ConstraintLayout keyboard;

        ViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.textView_checkList_title);
            recyclerView_content = itemView.findViewById(R.id.recycler_checklist_content);
            write_btn = itemView.findViewById(R.id.button_content_write);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    String getItem(int id) {
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
