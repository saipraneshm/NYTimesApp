package com.codepath.assignment.newsapp.fragment;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;

import com.codepath.assignment.newsapp.R;
import com.codepath.assignment.newsapp.databinding.DialogSettingsBinding;
import com.codepath.assignment.newsapp.utils.QueryPreferences;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


/**
 * Created by saip92 on 9/23/2017.
 */

public class SettingsDialogFragment extends DialogFragment
        implements SharedPreferences.OnSharedPreferenceChangeListener {


    private static final int REQUEST_DATE = 14;
    private static final String DIALOG_DATE = "dialog_date";

    private Date selectedDate;

    private DialogSettingsBinding mBinding;

    private boolean hasPreferencesChanged = false;



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        updateUI();

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.dialog_settings,container,false);
        PreferenceManager.getDefaultSharedPreferences(getActivity())
                .registerOnSharedPreferenceChangeListener(this);
        return mBinding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        PreferenceManager.getDefaultSharedPreferences(getActivity())
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    private void updateUI(){
        selectedDate = parseDate(QueryPreferences.getBeginDatePref(getActivity()));
        mBinding.etSortOrder.setText(QueryPreferences.getSortPref(getActivity()));
        mBinding.etBeginDate.setText(QueryPreferences.getBeginDatePref(getActivity()));
        mBinding.cbArts.setChecked(QueryPreferences.getArtsPref(getActivity()));
        mBinding.cbSports.setChecked(QueryPreferences.getSportsPref(getActivity()));
        mBinding.cbFashionAndStyle.setChecked(QueryPreferences.getFashionPref(getActivity()));
        mBinding.scOverlay.setChecked(QueryPreferences.getSettingsDialoPref(getActivity()));
        mBinding.scBrowser.setChecked(QueryPreferences.getEmbeddedBrowserPref(getActivity()));
        mBinding.etSortOrder.setOnClickListener(view -> showSortOrderPopUp());
        mBinding.etBeginDate.setOnClickListener(view -> showDateDialogPicker());
        mBinding.btnSave.setOnClickListener(view -> {
            updateSharedPreferences();
            Intent intent = new Intent(getString(R.string.broadcast_event_preference_changed));
            intent.putExtra(getString(R.string.has_preference_changed_key),hasPreferencesChanged);
            LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
            dismiss();
        });
        mBinding.btnCancel.setOnClickListener(view -> dismiss());
    }


    private void updateSharedPreferences(){
        QueryPreferences.setSortPref(getActivity(),mBinding.etSortOrder.getText().toString());
        QueryPreferences.setBeginDatePref(getActivity(),selectedDate);
        QueryPreferences.setSettingsDialogPref(getActivity(),mBinding.scOverlay.isChecked());
        QueryPreferences.setArtsPref(getActivity(),mBinding.cbArts.isChecked());
        QueryPreferences.setFashionPref(getActivity(),mBinding.cbFashionAndStyle.isChecked());
        QueryPreferences.setSportsPref(getActivity(),mBinding.cbSports.isChecked());
        QueryPreferences.setEmbeddedBrowserPref(getActivity(),mBinding.scBrowser.isChecked());
    }

    private void showSortOrderPopUp(){
        PopupMenu popup = new PopupMenu(getActivity(),mBinding.etSortOrder);
        popup.setOnMenuItemClickListener(item -> {
            String action = null;
            switch (item.getItemId()){
                case R.id.action_sort_by_oldest:
                    action = getString(R.string.pref_sort_older_value);
                    break;
                case R.id.action_sort_by_newest:
                    action = getString(R.string.pref_sort_newer_value);
                    break;

            }
            if(action != null)
                mBinding.etSortOrder.setText(action);
            return true;
        });
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_sort_order, popup.getMenu());
        popup.show();
    }

    private void showDateDialogPicker(){
        FragmentManager fm = getFragmentManager();
        DatePickerDialogFragment dialogFragment = DatePickerDialogFragment.newInstance(selectedDate);
        dialogFragment.setTargetFragment(this, REQUEST_DATE);
        dialogFragment.show(fm, DIALOG_DATE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != Activity.RESULT_OK) return;
        if(requestCode == REQUEST_DATE){
            Date date = (Date) data.getSerializableExtra(DatePickerDialogFragment.EXTRA_DATE);
            updateDate(date);
        }
    }

    private void updateDate(Date date) {
        DateFormat dateFormat = SimpleDateFormat.getDateInstance(DateFormat.MEDIUM, Locale.US);
        String strDate = dateFormat.format(date);
        selectedDate = date;
        mBinding.etBeginDate.setText(strDate);
        QueryPreferences.setBeginDatePref(getActivity(),date);
    }

    private Date parseDate(String strDate){
        DateFormat fromFormat = SimpleDateFormat.getDateInstance(DateFormat.MEDIUM, Locale.US);
        try{
            return fromFormat.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return new Date();
        }

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        hasPreferencesChanged = true;
    }
}
