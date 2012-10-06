package com.yalong.serviceprovider;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;

import com.yalong.R;

public class RequestTabWidget extends TabActivity {
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.serviceproviderrequests);

	    Resources res = getResources(); // Resource object to get Drawables
	    TabHost tabHost = getTabHost();  // The activity TabHost
	    TabHost.TabSpec spec;  // Resusable TabSpec for each tab
	    Intent intent;  // Reusable Intent for each tab

	    // Create an Intent to launch an Activity for the tab (to be reused)
	    intent = new Intent().setClass(this, MatchingRequestsActivity.class);

	    // Initialize a TabSpec for each tab and add it to the TabHost
	    spec = tabHost.newTabSpec("matching").setIndicator("Demandes",
	                      res.getDrawable(android.R.drawable.ic_menu_set_as))
	                  .setContent(intent);
	    tabHost.addTab(spec);

	    // Do the same for the other tabs
	    intent = new Intent().setClass(this, WaitingRequestsActivity.class);
	    spec = tabHost.newTabSpec("waiting").setIndicator("En attente",
	                      res.getDrawable(android.R.drawable.ic_menu_recent_history))
	                  .setContent(intent);
	    tabHost.addTab(spec);

	    intent = new Intent().setClass(this, AcceptedRequestsActivity.class);
	    spec = tabHost.newTabSpec("accepted").setIndicator("En cours",
	                      res.getDrawable(R.drawable.ic_menu_play_clip))
	                  .setContent(intent);
	    tabHost.addTab(spec);

	    intent = new Intent().setClass(this, FinishedRequestsActivity.class);
	    spec = tabHost.newTabSpec("finished").setIndicator("Terminées",
	                      res.getDrawable(R.drawable.ic_menu_mark))
	                  .setContent(intent);
	    tabHost.addTab(spec);

	    tabHost.setCurrentTab(1);
	}
}
