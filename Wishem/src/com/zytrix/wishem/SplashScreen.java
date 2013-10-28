package com.zytrix.wishem;




import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

public class SplashScreen extends Activity {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MessageTemplates.mFinish=false;
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.splash_screen);
		

		Thread splashThread = new Thread() {
			@Override
			public void run() {
				try {
					int waited = 0;
					while (waited < 3000) {
						sleep(100);
						waited += 100;
					}
				} catch (InterruptedException e) {
					// do nothing
				} finally {
					finish();
					Intent i1 = new Intent();
					i1.setClassName("com.zytrix.wishem",
							"com.zytrix.wishem.MessageTemplates");
					startActivity(i1);
				}
			}
		};
		splashThread.start();


	}
}
