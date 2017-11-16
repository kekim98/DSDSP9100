package com.dignsys.dsdsp.dsdsp_9100.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;


import com.dignsys.dsdsp.dsdsp_9100.R;
import com.dignsys.dsdsp.dsdsp_9100.db.entity.ConfigEntity;
import com.dignsys.dsdsp.dsdsp_9100.viewmodel.ConfigViewModel;

import java.util.List;


public class MainActivity extends AppCompatActivity {


    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ConfigViewModel viewModel =
                ViewModelProviders.of(this).get(ConfigViewModel.class);

        subscribeUi(viewModel);

        init();
    }

    private void subscribeUi(ConfigViewModel viewModel) {
        // Update the list when the data changes
        viewModel.getConfig().observe(this, new Observer<ConfigEntity>() {
            @Override
            public void onChanged(@Nullable ConfigEntity myConfig) {
                if (myConfig != null) {
                    Log.d(TAG, "onChanged: ConfigEntity loaded!!!!");
                  //  mBinding.setIsLoading(false);
                } else {
                    Log.d(TAG, "onChanged: ConfigEntity NOT loaded!!!!");
                  //  mBinding.setIsLoading(true);
                }
                // espresso does not know how to wait for data binding's loop so we execute changes
                // sync.
               // mBinding.executePendingBindings();
            }
        });
    }

    private void init() {
        View rootView = findViewById(R.id.main_layout);
        //ImageFragment imageFragment1 = ImageFragment.newInstance(0, 0);
        //ImageFragment imageFragment2 = ImageFragment.newInstance(960, 0);
        //VideoFragment videoFragment = VideoFragment.newInstance(960, 0);
        ClockFragment clockFragment = ClockFragment.newInstance(0, 0);

        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction
              //  .add(rootView.getId(), imageFragment1)
                .add(rootView.getId(), clockFragment)
                .commit();

    }
}
