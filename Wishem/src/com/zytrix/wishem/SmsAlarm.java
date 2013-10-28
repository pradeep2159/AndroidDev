package com.zytrix.wishem;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Character.UnicodeBlock;
import java.util.ArrayList;


import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.provider.SyncStateContract.Constants;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

public class SmsAlarm extends BroadcastReceiver{


	private int j=0;
	private Context mContext;
	int uid;
	String iuid;
	NotificationManager nm;
	Sendsms ss= new Sendsms();
	MessageThreadDatabase mModel;
	SmsManager smsManager;
	String SENT = "SMS_SENT";
	String DELIVERED = "SMS_DELIVERED";
	PendingIntent deliveredPI;
	PendingIntent sentPI;
	private int mResult;
	private String mPhoneno;
	
	Notification notif;
	Notification notif2;
	PendingIntent pIntent;
	CharSequence from = "Wishem ";

	private ArrayList<String> Nlist = new ArrayList<String>();
	private String mMessage;

	//Bundle count;

	public void onReceive(Context context, Intent intent) {


		mModel= new MessageThreadDatabase(context);
		iuid=intent.getStringExtra("uid");

		Bundle data = intent.getExtras();


		Nlist = data.getStringArrayList("SourceList");
		mMessage =data.getString("Sourcemsg");
		mPhoneno=data.getString("phno");

		//Toast.makeText(context, "Notification Added", 1000).show();

		nm = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);



		

		//PendingIntent contentIntent = PendingIntent.getActivity(context, 0, new Intent(context, Notify.class), Intent.FLAG_ACTIVITY_NEW_TASK);
		Intent intent1 = new Intent(context, Sendsms.class);

		Intent intent3 = new Intent(context, MessageThreads.class);

		PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent3, PendingIntent.FLAG_CANCEL_CURRENT);
		Notification notif = new Notification(R.drawable.appicon, "Notification...", System.currentTimeMillis());

		notif.flags |= Notification.FLAG_AUTO_CANCEL;
		 
        // Play default notification sound
		notif.defaults |= Notification.DEFAULT_SOUND;


		notif.setLatestEventInfo(context, from, "Message Sent Successfully To."+Nlist, pIntent);
		nm.notify(0, notif); 

				
		           //line. Then it will be cleared.
		


		deliveredPI = PendingIntent.getBroadcast(context, 0, new Intent(DELIVERED), 0);
		sentPI = PendingIntent.getBroadcast(context, 0, new Intent(SENT), 0);

		String rs= getResultData();

		//int rc=Integer.parseInt("rs");
		Log.v("","the Result Code"+22222+"RC str"+rs);





		ScheduledSMS();
		//ss.SentSMS(Nlist,mMessage);

		int rec = Nlist.size();
		for(int i=0;i<rec;i++)
		{
			ContentValues values = new ContentValues();
			values.put("address", Nlist.get(i));
			values.put("body", mMessage); 
			context.getContentResolver().insert(Uri.parse("content://sms/sent"), values); 
			Log.v("","Sent sms created");
		}

		//iuid=""+uid;
		Log.v("","the UID is"+uid+"and IUID is"+iuid);
		mModel.UpdateStatus(iuid,"Message Sent");



	}


	private Intent getIntent() {
		// TODO Auto-generated method stub
		return null;
	}


	private  void ScheduledSMS() {


		
		
		Log.v("","Send SMS Called---------------------");

		int p = Nlist.size();
		Log.v("","Send SMS Called-----Intent List Size"+p+"Message"+mMessage);

		if(p==0)
		{
			Nlist.add(mPhoneno);
			Log.v("","Send SMS Edit Text"+mPhoneno);

		}


		// String sms = m_smsBody;
		int n  =Nlist.size();

		Log.v("","Send SMS with Text no"+n+"num"+mPhoneno);
		
		 
		try {

			for (int i=0;i<n;i++){


				smsManager = SmsManager.getDefault();
				smsManager.sendTextMessage(Nlist.get(i), null, mMessage, sentPI, deliveredPI);

				Log.v("","Send SMS Called" +i+"times"+Nlist.get(i));

			}
			
		} catch (Exception e) {
			/*Toast.makeText(mContext,"SMS faild, please try again later!",Toast.LENGTH_LONG).show();*/
			
			
			
			
			 e.printStackTrace();
			 
			 StringWriter sw = new StringWriter();
			 PrintWriter pw = new PrintWriter(sw);

			 e.printStackTrace(pw);
			/* e.printStackTrace(System.out);*/
			
			Log.v("","SMS Sent failed");
		}




	}






}
