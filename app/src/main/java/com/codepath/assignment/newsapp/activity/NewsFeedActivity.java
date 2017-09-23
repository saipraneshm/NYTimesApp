package com.codepath.assignment.newsapp.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.codepath.assignment.newsapp.R;
import com.codepath.assignment.newsapp.activity.abs.SingleFragmentActivity;
import com.codepath.assignment.newsapp.fragment.NewsFeedFragment;
import com.codepath.assignment.newsapp.fragment.SettingsDialogFragment;
import com.codepath.assignment.newsapp.fragment.abs.VisibleFragment;

public class NewsFeedActivity extends SingleFragmentActivity {


    private static final String OPEN_SETTINGS_DIALOG = "OpenSettingsDialog";

    @Override
    protected int getLayoutResId() {
        return super.getLayoutResId();
    }

    @Override
    protected Fragment createFragment() {
        return new NewsFeedFragment();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handleIntent(getIntent());

    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if(Intent.ACTION_SEARCH.equals(intent.getAction())){
            String query = intent.getStringExtra(SearchManager.QUERY);
            FragmentManager fm = getSupportFragmentManager();
            Fragment fragment = fm.findFragmentById(R.id.fragment_container);
            if(fragment != null){
                ((VisibleFragment)fragment).handleSearchQuery(query);
            }else{
                fm.beginTransaction()
                        .replace(R.id.fragment_container, NewsFeedFragment.newInstance(query))
                        .commit();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu,menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        //searchManager.setOnDismissListener(() -> getSupportActionBar().show());
        SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default
        searchView.setSubmitButtonEnabled(true);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_search: //onSearchRequested();
                //getSupportActionBar().hide();
                return true;
            case R.id.menu_settings:
                startActivity(SettingsActivity.newIntent(this));
                //initializeDialogFragment();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void initializeDialogFragment(){
        Log.d("NewsFeedActivity","calling intiliazeDialogFragment");
        SettingsDialogFragment dialogFragment = new SettingsDialogFragment();
        dialogFragment.show(getSupportFragmentManager(), OPEN_SETTINGS_DIALOG);
    }


}
