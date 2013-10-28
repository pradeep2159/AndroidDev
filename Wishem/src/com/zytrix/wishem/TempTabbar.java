package com.zytrix.wishem;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
 
public class TempTabbar extends TabActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wishem_tabbar);
 
        TabHost tabHost = getTabHost();
 
        // Tab for Photos
        TabSpec DealsbyCategory = tabHost.newTabSpec("Search by Category");
        // setting Title and Icon for the Tab
        DealsbyCategory.setIndicator(null, getResources().getDrawable(R.drawable.template_tab));
        Intent DealsbycategoryIntent = new Intent(this, MessageTemplates.class);
        DealsbyCategory.setContent(DealsbycategoryIntent);
 
        // Tab for Songs
        TabSpec SearchByLocation = tabHost.newTabSpec("Search By Location");
        SearchByLocation.setIndicator(null, getResources().getDrawable(R.drawable.thread_tab));
        Intent SearchByLocationIntent = new Intent(this, MessageThreads.class);
        SearchByLocation.setContent(SearchByLocationIntent);
 
    
        // Adding all TabSpec to TabHost
        tabHost.addTab(DealsbyCategory); 
        tabHost.addTab(SearchByLocation);
      
    }
}
