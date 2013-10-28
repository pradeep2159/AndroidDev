package com.zytrix.wishem;

import java.util.ArrayList;


import com.zytrix.wishem.MessageTemplates.MessageAdapter;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MessageThreads extends Activity{

	private RelativeLayout mTabsBarR8 = null;
	/*private Tabsbar mTabsbarInstance8; */
	MessageThreadDatabase mMessagetempGroups;
	private ArrayList<String> mUniqueId = new ArrayList<String>();
	private ArrayList<String> mNumber = new ArrayList<String>();
	private ArrayList<String> mFrequency = new ArrayList<String>();
	private ArrayList<String> mStatus = new ArrayList<String>();
	private ArrayList<String> mMessage = new ArrayList<String>();
	private Cursor mCursor;
	private ListView mThreadList;
	private Context mContext;
	private Button mNew_message;
	private Button mMessage_thread;
	MessageTemplatesDatabase mtd;
	private TextView mDefaultText;
	private Button deleteButton;
	private Button cancelButton;
	private TextView mNum, mStat, mMess, mFreq;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.messagethreads);
		mMessagetempGroups = new MessageThreadDatabase(this);
		mContext = MessageThreads.this;

		
		
	/*	mTabsBarR8 = (RelativeLayout)findViewById(R.id.id_bottom_message_rl);*/
		mDefaultText = (TextView)findViewById(R.id.textvv);
		
		/*mTabsbarInstance8 = new Tabsbar(MessageThreads.this, "MessageThreads");
		mTabsBarR8.addView(mTabsbarInstance8); 

		//mTabsbarInstance2.mMessages_btn.setBackgroundResource(R.drawable.template_hover);
		mTabsbarInstance8.mMessageTemplates.setBackgroundResource(R.drawable.bottombg_hover);		
		mTabsbarInstance8.mMessageText.setTextColor(Color.parseColor("#000000"));*/

		mThreadList = (ListView)findViewById(R.id.listview_thread);


		mCursor = getSqlData();
		showSqlData(mCursor);
		if (mCursor.getCount() == 0) {


			mDefaultText.setText("No Message Threads are added yet");
		}
		else
		{
			mDefaultText.setVisibility(View.INVISIBLE);
		}


		mThreadList.setAdapter(new ThreadAdapter(this));
		mNew_message = (Button)findViewById(R.id.new_message);
		mMessage_thread = (Button)findViewById(R.id.message_thread);
		mMessage_thread.setBackgroundResource(R.drawable.thread_tab_hover);
		
		mNew_message.setOnClickListener(new OnClickListener() {


			public void onClick(View v) {
				
				Intent messagetemplates = new Intent(getApplicationContext(), MessageTemplates.class);
				
				startActivity(messagetemplates);
				finish();
			}
		});
		
		mThreadList.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				String s = mUniqueId.get(position);
				final int j= Integer.parseInt(s);
					
				//String product = ((TextView) view).getText().toString();
				
				/*// Launching new Activity on selecting single List Item
				Intent i = new Intent(getApplicationContext(), Sendsms.class);
				//sending data to new activity
				i.putExtra("messageTemplate", s);
				
				  editor.putString("smsbody",s);
				  editor.commit();
				startActivity(i);*/
				 
			
				 
				 AlertDialog.Builder alertDialog = new AlertDialog.Builder(MessageThreads.this);
				 
				 	final Dialog dialog = new Dialog(mContext);
					dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

					dialog.setContentView(R.layout.dialog_thread);
					 cancelButton = (Button) dialog.findViewById(R.id.dialog_cancel);
					 deleteButton = (Button) dialog.findViewById(R.id.dialog_save);
					 mNum = (TextView) dialog.findViewById(R.id.tv1);
					 mStat = (TextView) dialog.findViewById(R.id.tv2);
					 mMess = (TextView) dialog.findViewById(R.id.tv3);
					 mFreq = (TextView) dialog.findViewById(R.id.tv4);
					 
					 mMess.setText(mMessage.get(position));
					 mStat.setText(mStatus.get(position));
					 mNum.setText(mNumber.get(position));
					 mFreq.setText(mFrequency.get(position));
					 
			        // Setting Dialog Title
					
			 
			        // Setting Dialog Message
			    //    alertDialog.setMessage("Are you sure you want to delete this message Thread ?");
			 
			        // Setting Icon to Dialog
			      //  dialog.setIcon(R.drawable.appicon);
			 
					 cancelButton.setOnClickListener(new OnClickListener() {

						public void onClick(View v) {
							dialog.dismiss();
						}
					});
			        // Setting Positive "Yes" Button
					
					 deleteButton.setOnClickListener(new OnClickListener() {

							public void onClick(View v) {
								CancelAlarm(j);
							}
						});
			 
			        // Setting Negative "NO" Button
			       
			 
			        // Showing Alert Message
			      //  alertDialog.show();
			        dialog.show();


			}
		});

		
		
	}
	private Cursor getSqlData() {
		SQLiteDatabase db3 = mMessagetempGroups.getReadableDatabase();
		Cursor cursor = db3.query(MessageThreadDatabase.TABLE, null, null, null, null, null, null);

		// startManagingCursor(cursor);

		return cursor;
	}
	private void showSqlData(Cursor cursor) {

		if (cursor.moveToFirst()) {
			do {

				String uid = cursor.getString(1);
				String number = cursor.getString(2);
				String freq = cursor.getString(3);
				String stat = cursor.getString(4);
				String message = cursor.getString(5);

			
				mUniqueId.add(uid);
				mNumber.add(number);
				mFrequency.add(freq);
				mStatus.add(stat);
				mMessage.add(message);

				

			} while (cursor.moveToNext());
		}

		/*mUniqueId.add("456123");*/

	}

	private void CancelAlarm(int uniqueId){

		Intent intent = new Intent(this, SmsAlarm.class);
		PendingIntent pi = PendingIntent.getBroadcast(this, uniqueId, intent, 0);
		AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);

		am.cancel(pi);
		Toast.makeText(getApplicationContext(), "Thread deleted successfully.", 3000).show();
		
		mMessagetempGroups.deleterow(uniqueId);
		//String iuid=""+uniqueId;
		//mMessagetempGroups.UpdateStatus(iuid, "Close");
		Intent intent1 = getIntent();
		
		startActivity(intent1); 
		finish();
	}
	
	public void onBackPressed() {
		
		Intent t1 = new Intent(getApplicationContext(), MessageTemplates.class);

		startActivity(t1);
		finish();
	}
	 
	    // Deleting single contact
	   
	 

	private void showDialog(Activity activity, String title, CharSequence message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		if (title != null)
			builder.setTitle(title);
		builder.setMessage(message);
		builder.setPositiveButton("OK", null);
		builder.setNegativeButton("Cancel", null);
		builder.show();
	}
	

	class ThreadAdapter extends BaseAdapter{

		//	private ArrayList<String> template;

		private  Context mcontext;
		public ThreadAdapter(Context context){
			mcontext = context;

		}

		public int getCount() {
			// TODO Auto-generated method stub
			return mUniqueId.size();
		}

		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		public View getView(int position, View convertview, ViewGroup parent) {

			LayoutInflater inflater = (LayoutInflater)mcontext.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
			View view = inflater.inflate( R.layout.threadrow, parent, false );

			String   item1 = mUniqueId.get(position);
			String   item2 = mNumber.get(position);
			String   item3 = mFrequency.get(position);
			String   item4 = mStatus.get(position);

			TextView t1 = (TextView)view.findViewById(R.id.uid);
			TextView t2 = (TextView)view.findViewById(R.id.number);
			TextView t3 = (TextView)view.findViewById(R.id.freq);

			int len = item2.length();
			String it="........";
			if(len > 20){
				
				item2 =item2.substring(0,20);
				
				item2=item2+""+it;
			}
			t1.setText(item2);
			t2.setText(item3);
			t3.setText(item4);



			//   product = ((TextView) view).getText().toString();
			//Toast.makeText(getApplicationContext(), item, 3000).show();
			return view;

		}


	}
}
