package com.codepath.assignment.newsapp.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.codepath.assignment.newsapp.R;
import com.codepath.assignment.newsapp.utils.QueryPreferences;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by saip92 on 9/19/2017.
 */

public class SettingsFragment extends PreferenceFragmentCompat implements
        SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String OPEN_DATE_PICKER_DIALOG = "open_date_picker_dialog";
    private static final int REQUEST_TO_OPEN_DIALOG = 1;
    private Preference beingDatePref;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.pref_visualizer);

        SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();
        /*PreferenceScreen prefScreen = getPreferenceScreen();

        int count = prefScreen.getPreferenceCount();

        for(int i = 0; i < count; i++){
            Preference p = prefScreen.getPreference(i);
            if(!(p instanceof CheckBoxPreference)){
                String value = sharedPreferences.getString(p.getKey(),"");
                setPreferenceSummary(p,value);
            }
        }*/

        setUpSortOrderPreference(sharedPreferences);
        setUpBeginDatePreference(sharedPreferences);


    }

    private void setUpSortOrderPreference(SharedPreferences sharedPreferences){
        ListPreference sortOrderPreference = (ListPreference)
                findPreference(getString(R.string.pref_sort_key));
        sortOrderPreference.setSummary(sharedPreferences
                .getString(getString(R.string.pref_sort_key),
                        getString(R.string.pref_sort_older_value)));
    }

    private void setUpBeginDatePreference(SharedPreferences sharedPreferences){
        beingDatePref = findPreference(getString(R.string.pref_select_begin_date_key));
        String value = sharedPreferences.getString(getString(R.string.pref_select_begin_date_key),"");
        if(value.equals("")){
            updateDate(new Date());
        }else{
            beingDatePref.setSummary(value);
        }
        beingDatePref.setOnPreferenceClickListener(preference -> {
            String key = preference.getKey();
            if(key.equals(getString(R.string.pref_select_begin_date_key))){
                openDatePickerDialog();
            }
            return false;
        });
    }

    private void setPreferenceSummary(Preference preference, String value){
        if(preference instanceof ListPreference){
            ListPreference listPreference = (ListPreference) preference;
            int prefIndex = listPreference.findIndexOfValue(value);
            if(prefIndex >= 0){
                listPreference.setSummary(listPreference.getEntries()[prefIndex]);
            }
        }
    }

    private void openDatePickerDialog(){
        DatePickerDialogFragment dialogFragment = DatePickerDialogFragment.newInstance(new Date());
        dialogFragment.setTargetFragment(this, REQUEST_TO_OPEN_DIALOG);
        dialogFragment.show(getActivity().getSupportFragmentManager(), OPEN_DATE_PICKER_DIALOG);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != Activity.RESULT_OK) return;
        if(requestCode == REQUEST_TO_OPEN_DIALOG){
            Date date = (Date) data.getSerializableExtra(DatePickerDialogFragment.EXTRA_DATE);
            updateDate(date);
        }
    }

    private void updateDate(Date date) {
        DateFormat dateFormat = SimpleDateFormat.getDateInstance(DateFormat.MEDIUM, Locale.US);
        String strDate = dateFormat.format(date);
        beingDatePref.setSummary(strDate);
        QueryPreferences.setBeginDatePref(getActivity(),date);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Preference preference = findPreference(key);
        if(preference != null){
            if((preference instanceof ListPreference)){
                String value = sharedPreferences.getString(preference.getKey(),"");
                setPreferenceSummary(preference,value);
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }
}
