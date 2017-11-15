package com.dignsys.dsdsp.dsdsp_9100.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;


import com.dignsys.dsdsp.dsdsp_9100.R;


public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init() {
        View rootView = findViewById(R.id.main_layout);
        ImageFragment imageFragment1 = ImageFragment.newInstance(0, 0);
        //ImageFragment imageFragment2 = ImageFragment.newInstance(960, 0);
        VideoFragment videoFragment = VideoFragment.newInstance(960, 0);

        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction
                .add(rootView.getId(), imageFragment1)
                .add(rootView.getId(), videoFragment)
                .commit();

    }
}
