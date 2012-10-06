package com.yalong;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class Main extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		Thread timer = new Thread(){
			@Override
			public void run(){
				try{
					sleep(1000);
				} catch (InterruptedException e){
					e.printStackTrace();
				} finally{
					Intent openAndroidTestActivity = new Intent("com.yalong.LOGIN");
					startActivity(openAndroidTestActivity);
				}
			}
		};
		timer.start();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		finish();
	}

}
