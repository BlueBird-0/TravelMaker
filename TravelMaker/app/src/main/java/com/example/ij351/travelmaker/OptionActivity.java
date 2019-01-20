package com.example.ij351.travelmaker;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.androidadvance.topsnackbar.TSnackbar;
import com.pixplicity.easyprefs.library.Prefs;

public class OptionActivity extends AppCompatActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);    //액티비티에 사용되는 xml 불러오기

        TextView roomId = (TextView)findViewById(R.id.textView_roomId);
        roomId.setText(TravelRoom.roomId);

        Button roomIdCopy = (Button)findViewById(R.id.button_roomId);
        roomIdCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("초대 코드", TravelRoom.roomId);
                clipboard.setPrimaryClip(clip);
                showSnackbar(findViewById(R.id.layout_option), "Copy Clipboard");
            }
        });

        Button roomExit = (Button)findViewById(R.id.button_roomExit);
        roomExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TravelRoom.roomId = null;
                Prefs.putString(TravelRoom.ROOMID_SP, null);

                Intent intent = new Intent(getApplicationContext(), RoomActivity.class);
                getApplicationContext().startActivity(intent);
            }
        });


        Button logout = (Button)findViewById(R.id.button_logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User.logoutUser();
                Prefs.putString(TravelRoom.ROOMID_SP, null);

                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                getApplicationContext().startActivity(intent);
            }
        });


    }

    public static void showSnackbar(View view, String message)
    {
        //스낵바
        TSnackbar snackbar = TSnackbar
                .make(view, message, TSnackbar.LENGTH_LONG);
        //snackbar.setActionTextColor(Color.WHITE);
        View snackbarView = snackbar.getView();
        //snackbarView.setBackgroundColor(Color.parseColor("#CC00CC"));
        TextView textView = (TextView) snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        snackbar.show();
    }
}
