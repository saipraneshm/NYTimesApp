package com.codepath.assignment.newsapp.activity.abs;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.codepath.assignment.newsapp.R;
import com.codepath.assignment.newsapp.databinding.LayoutFragmentContainerBinding;

/**
 * Created by saip92 on 9/19/2017.
 */

public abstract class SingleFragmentActivity extends AppCompatActivity {


    private LayoutFragmentContainerBinding binding;

    protected abstract Fragment createFragment();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.layout_fragment_container);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if(fragment == null){
            fragment = createFragment();
            fm.beginTransaction().replace(R.id.fragment_container, fragment).commit();
        }


    }
}
