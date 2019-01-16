package com.example.ij351.travelmaker;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TabLayout tabLayout;
    FragmentPagerAdapter fragmentPagerAdapter;
    private int[] tabIcons = {
            R.drawable.ic_pen,
            R.drawable.ic_pen,
            R.drawable.ic_pen
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(0xFFFFFFFF);
        setSupportActionBar(toolbar);


        ViewPager viewPager = (ViewPager)findViewById(R.id.mainViewPager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout)findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        //setupTabIcons();

        //로그인 체크
        if(User.checkLogined() == false)
        {
            //메인 액티비티 만들면서 로그인 액티비티 실행
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }else
        {
            User.loginUser();
        }

        //키보드 이벤트 리스너
        KeyboardVisibilityEvent.setEventListener(
                this, new KeyboardVisibilityEventListener() {

                    @Override
                    public void onVisibilityChanged(boolean isOpen) {
                        Log.d("test001", String.valueOf(isOpen));
                        if(isOpen == false)
                        {
                            ConstraintLayout keyboardLayout = (ConstraintLayout)findViewById(R.id.layout_keyboard);
                            keyboardLayout.setVisibility(View.INVISIBLE);
                        }
                    }
                }
        );


    }

    private void setupViewPager(ViewPager viewPager) {
        fragmentPagerAdapter = new MainPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(fragmentPagerAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.toolbar_option)
        {
            Intent intent = new Intent(this, WriteActivity.class);
            startActivity(intent);
            Toast.makeText(MainActivity.this, "Action clicked", Toast.LENGTH_LONG).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}