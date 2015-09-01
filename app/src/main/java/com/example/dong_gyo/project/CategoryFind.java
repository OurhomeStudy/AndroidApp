package com.example.dong_gyo.project;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.ArrayAdapter;
import android.widget.SearchView;

import java.util.ArrayList;

/**
 * Created by Dong_Gyo on 15. 8. 3..
 */
public class CategoryFind extends ActionBarActivity {

    ArrayList arr = new ArrayList();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_find);

        //ListView catlist = (ListView)findViewById(R.id.category);

        arr.add("item1");
        arr.add("item2");
        arr.add("item3");
        arr.add("item4");
        arr.add("item5");
        arr.add("item6");
        arr.add("item7");
        arr.add("item8");
        arr.add("item9");


        ArrayAdapter catadapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arr);

        //catlist.setAdapter(catadapter);


    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_actions, menu);

        SearchManager searchManager = (SearchManager)getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView)menu.findItem(R.id.action_search).getActionView();
        if(searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        }

        return super.onCreateOptionsMenu(menu);
    }
}
