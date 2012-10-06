package com.yalong;

import com.yalong.serviceprovider.ServiceProviderHome;

import android.app.Activity;
import android.content.Intent;
import android.view.MenuInflater;
import android.view.MenuItem;

/**
 * Manage the menu button of the device
 *
 */
public abstract class ActivityWithMenu extends Activity{
	
	public boolean onCreateOptionsMenu(android.view.Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater blowUp = getMenuInflater();
		blowUp.inflate(R.menu.menu_yalong, menu);		
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
		
		case R.id.menuUserMode:
			Intent i1 = new Intent(this, Dashboard.class);
			startActivity(i1);
			break;

		case R.id.menuServiceProviderMode:
			Intent i2 = new Intent(this, ServiceProviderHome.class);
			startActivity(i2);
			break;	
			
		}
		return false;
	}	
}
