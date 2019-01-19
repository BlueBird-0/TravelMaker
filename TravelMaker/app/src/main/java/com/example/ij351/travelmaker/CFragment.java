package com.example.ij351.travelmaker;

import android.Manifest;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gun0912.tedbottompicker.TedBottomPicker;

public class CFragment extends Fragment {
    RecyclerBillAdapter adapter;
    RecyclerCostAdapter cost_adapter;
    private String TAG = "CFragment";

    public static CFragment newInstance(int page, String title) {

        CFragment cFragment = new CFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        cFragment.setArguments(args);

        return cFragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_c, container, false);

        //영수증들 가져오기
        //리스트 출력
        final ArrayList<Bill> bills = new ArrayList<>();
        RecyclerView recyclerView = view.findViewById(R.id.recycler_bills);
        final ProgressBar progressBar = (ProgressBar)view.findViewById(R.id.progressBar_main);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
      adapter = new RecyclerBillAdapter(getContext(), bills);
        //adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        //TravelRoom.db.collection("Travels").document(TravelRoom.roomId)
        TravelRoom.db.collection("Travels").document(TravelRoom.roomId)
                .collection("Bills")
                .orderBy("time")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@javax.annotation.Nullable QuerySnapshot value, @javax.annotation.Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "Listen failed1.", e);
                            return;
                        }

                        bills.clear();
                        for (QueryDocumentSnapshot doc : value) {
                            Bill newBill = new Bill(doc.getId(), doc.getString("uri"), doc.getTimestamp("time"));
                            bills.add(newBill);
                        }
                        adapter.notifyDataSetChanged();     //Adapter 새로고침
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                });
        //영수증 사진 업로드
        Button picture = (Button)view.findViewById(R.id.button_bill_write);
        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PermissionListener permissionlistener = new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        TedBottomPicker tedBottomPicker = new TedBottomPicker.Builder(getContext())
                                .setOnImageSelectedListener(new TedBottomPicker.OnImageSelectedListener() {
                                    @Override
                                    public void onImageSelected(Uri uri) {
                                        uploadImage(uri);
                                    }
                                })
                                .create();
                        tedBottomPicker.show(getFragmentManager());
                    }
                    @Override
                    public void onPermissionDenied(List<String> deniedPermissions) {
                        Toast.makeText(getContext(), "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
                    }
                };
                TedPermission.with(getContext())
                        .setPermissionListener(permissionlistener)
                        .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                        .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .check();
            }
        });




        //키보드 관련
        Button button_cost_write = (Button)view.findViewById(R.id.button_cost_write);
        button_cost_write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConstraintLayout keyboard2 = (ConstraintLayout)view.getRootView().findViewById(R.id.layout_keyboard2);
                keyboard2.setVisibility(View.VISIBLE);

                EditText content = (EditText)view.getRootView().findViewById(R.id.edit_keyboard2);
                content.requestFocus();
                Button send = (Button)view.getRootView().findViewById(R.id.button_key2_send);

                final InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(content, InputMethodManager.SHOW_FORCED);
                //글쓰기 누를 때,
                send.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText content = (EditText)view.getRootView().findViewById(R.id.edit_keyboard2);
                        EditText cost = (EditText)view.getRootView().findViewById(R.id.edit_keyboard3);
                        if(cost.getText().toString().trim().length() == 0) {
                            cost.requestFocus();
                            return;
                        }
                        if(content.getText().toString().trim().length() == 0) {
                            content.requestFocus();
                            return;
                        }
                        TravelRoom.writeCost(content.getText().toString(), Double.valueOf(cost.getText().toString()));
                        content.setText("");
                        cost.setText("");
                        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0); //키보드 접기
                    }
                });
            }
        });



        //사용내역 가져오기
        //리스트 출력
        final ArrayList<Cost> costs = new ArrayList<>();
        RecyclerView recyclerView2 = view.findViewById(R.id.recycler_costs);
        final ProgressBar progressBar2 = (ProgressBar)view.findViewById(R.id.progressBar_cost);
        recyclerView2.setLayoutManager(new LinearLayoutManager(getContext()));
        cost_adapter = new RecyclerCostAdapter(getContext(), costs);
        //adapter.setClickListener(this);
        recyclerView2.setAdapter(cost_adapter);

        TravelRoom.db.collection("Travels").document(TravelRoom.roomId).collection("Costs")
                .orderBy("time")    //내림차순검색
                .addSnapshotListener(new EventListener<QuerySnapshot>(){
                    @Override
                    public void onEvent(@javax.annotation.Nullable QuerySnapshot value, @javax.annotation.Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "Listen failed2.", e);
                            return;
                        }

                        costs.clear();
                        for (QueryDocumentSnapshot doc : value) {
                            Log.w(TAG, "Listen event2.", e);
                            if (doc.getString("content") != null || doc.getString("time") != null || doc.getString("cost") != null) {
                                String content = doc.getString("content");
                                Timestamp time = doc.getTimestamp("time");
                                Double cost = doc.getDouble("cost");

                                Cost newCost = new Cost(doc.getId(), cost, time, content);
                                costs.add(newCost);
                            }
                        }
                        //금액 계산
                        TextView total_text = (TextView)view.findViewById(R.id.textView_total);
                        double total = 0;
                        for(Cost cost :costs)
                        {
                            total += cost.cost;
                        }
                        total_text.setText("Total : "+String.valueOf(total)+"￦");
                        //개인당 금액
                        TextView oneMen_text = (TextView)view.findViewById(R.id.textView_oneMen);
                        oneMen_text.setText("One-Men : "+String.valueOf(total / TravelRoom.numPeopleInRoom) +"￦");


                        cost_adapter.notifyDataSetChanged();     //Adapter 새로고침
                        progressBar2.setVisibility(View.INVISIBLE);
                        Log.d(TAG, "Current cites in CA: " + costs);
                    }
                });
        return view;
    }

    public void uploadImage(Uri file)
    {
        final Timestamp uploadTime = Timestamp.now();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        final StorageReference mountainImagesRef = storageRef.child("images/"+TravelRoom.roomId+"/bill_"+uploadTime.getSeconds());
        UploadTask uploadTask = mountainImagesRef.putFile(file);

        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>(){
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    Log.d(TAG, "사진 업로드 실패");
                    throw task.getException();
                }
                // Continue with the task to get the download URL
                return mountainImagesRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    Log.d(TAG, "사진 업로드 성공");
                    Log.d(TAG, downloadUri.toString());

                    Bill newBill = new Bill("", downloadUri.toString(), uploadTime);
                    TravelRoom.db.collection("Travels").document(TravelRoom.roomId).collection("Bills")
                            .add(newBill.getHashMap());
                } else {
                    // Handle failures
                    // ...
                }
            }
        });
    }
}