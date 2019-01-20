package com.example.ij351.travelmaker;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.Constraints;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.IntentCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;

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
    public void onBackPressed() {
    //        super.onBackPressed();
    }

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

        //키보드 이벤트 리스너
        KeyboardVisibilityEvent.setEventListener(
                this, new KeyboardVisibilityEventListener() {

                    @Override
                    public void onVisibilityChanged(boolean isOpen) {
                        ViewPager mainViewPager = (ViewPager)findViewById(R.id.mainViewPager);
                        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) mainViewPager.getLayoutParams();
                        if(isOpen == true) {
                            params.bottomToTop = R.id.layout_keyboard;
                            mainViewPager.setLayoutParams(params);
                        }

                        if(isOpen == false)
                        {
                            ConstraintLayout keyboardLayout = (ConstraintLayout)findViewById(R.id.layout_keyboard);
                            keyboardLayout.setVisibility(View.INVISIBLE);

                            ConstraintLayout keyboardLayout2 = (ConstraintLayout)findViewById(R.id.layout_keyboard2);
                            keyboardLayout2.setVisibility(View.INVISIBLE);


                            params.bottomToTop = ConstraintLayout.LayoutParams.MATCH_PARENT;
                            mainViewPager.setLayoutParams(params);
                        }
                    }
                }
        );
    }

    private void setupViewPager(ViewPager viewPager) {
        fragmentPagerAdapter = new MainPagerAdapter(getSupportFragmentManager());
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }

            @Override
            public void onPageSelected(int i) {
                Log.d("test001", "PageSelected : "+ String.valueOf(i));
                InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });


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
            Intent intent = new Intent(this, OptionActivity.class);
            startActivity(intent);
            //Toast.makeText(MainActivity.this, "Action clicked", Toast.LENGTH_LONG).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}