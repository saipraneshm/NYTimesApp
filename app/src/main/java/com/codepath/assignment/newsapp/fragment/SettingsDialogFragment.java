package com.codepath.assignment.newsapp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.codepath.assignment.newsapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by saip92 on 9/23/2017.
 */

public class SettingsDialogFragment extends DialogFragment {


    @BindView(R.id.tilBeginDate)
    private TextInputEditText mBeginDate;

    @BindView(R.id.tilSortOrder)
    private TextInputEditText mSortOrder;

    @BindView(R.id.cbArts)
    private AppCompatCheckBox mArts;

    @BindView(R.id.cbFashionAndStyle)
    private AppCompatCheckBox mFashionAndStyle;

    @BindView(R.id.cbSports)
    private AppCompatCheckBox mSports;

    @BindView(R.id.scModalOverlay)
    private SwitchCompat mModalOverlay;


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().setTitle(getString(R.string.menu_settings_title));
        // Show soft keyboard automatically and request focus to field
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.dialog_settings,container, false);
        ButterKnife.bind(this,v);
        return v;
    }

    private void showSortOrderPopUp(){

    }
}
