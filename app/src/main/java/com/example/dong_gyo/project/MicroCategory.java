package com.example.dong_gyo.project;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.ArrayList;

/**
 * Created by Dong_Gyo on 15. 8. 18..
 */
public class MicroCategory extends ActionBarActivity {

    ListView maccat;
    ArrayList <String> arr = new ArrayList<String>();
    ArrayAdapter Mcatlist;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category);

        maccat = (ListView)findViewById(R.id.category);

        arr.add("item1");
        arr.add("item2");
        arr.add("iaem3");
        arr.add("itet4");
        arr.add("iaem5");
        arr.add("iuem6");
        arr.add("itim7");
        arr.add("item8");
        arr.add("item9");

        Mcatlist = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arr);
        Mcatlist.notifyDataSetChanged();
        maccat.setAdapter(Mcatlist);

        maccat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {


            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_actions, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        if (searchView != null) {
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String arg0) {
                    return false;
                }


                @Override
                public boolean onQueryTextChange(String arg0) {
                    if(arg0.length() == 0) {
                        maccat.setAdapter(Mcatlist);
                    } else {
                        ArrayAdapter tmp = new ArrayAdapter(MicroCategory.this, android.R.layout.simple_list_item_1);
                        tmp.clear();
                        for(int i=0; i<arr.size(); i++) {
                            if(arr.get(i).contains(arg0)) {
                                tmp.add(arr.get(i));
                            }
                        }
                        maccat.setAdapter(tmp);
                    }

                    return false;
                }
            });
        }

        return super.onCreateOptionsMenu(menu);
    }
}
