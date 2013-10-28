package com.zytrix.wishem;

import java.util.ArrayList;
import java.util.Collection;







import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MessageTemplates extends Activity {

	private RelativeLayout mTabsBarR2 = null;
	/*private Tabsbar mTabsbarInstance2;*/
	private Button mMessageThreads;

	ListView listview;
	Button newmessagetemp;
	private Context context;
	private EditText messageText=null;
	MessageTemplatesDatabase mt;
	String messageTemp;
	Cursor cursor;
	private Button mNew_messageTemp;

	private Button saveButton;
	
	
	 public static boolean mFinish;

	private TextView mTempSize;


	private  ArrayList<String> template = new ArrayList<String>();
	private  ArrayList<String> id = new ArrayList<String>();
	
	protected void onResume()
	{
		super.onResume();
		if(MessageTemplates.mFinish)
		{
			finish();
		}
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MessageTemplates.mFinish=false;
		 
		setContentView(R.layout.messagetemplates);
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		final SharedPreferences.Editor editor = preferences.edit();
		mt = new MessageTemplatesDatabase(this);
		context = MessageTemplates.this;
		MyController.getInstance().setActivity1(this);

		listview = (ListView)findViewById(R.id.listview1);



		TextView tv3 = (TextView)findViewById(R.id.textvv);
		cursor= getSqlData();
		showSqlData(cursor);
		//		mTabsbarInstance2.currAct();

		if (cursor.getCount() == 0) {


			tv3.setText("No Message Templates are added yet");
		}
		else
		{
			tv3.setVisibility(View.INVISIBLE);
		}




		MessageAdapter m1 = new MessageAdapter(this);
		listview.setAdapter(m1);



		mTabsBarR2 = (RelativeLayout)findViewById(R.id.id_bottom_message_rl);
		/*mTabsbarInstance2 = new Tabsbar(MessageTemplates.this, "MessageTemplates");
		mTabsBarR2.addView(mTabsbarInstance2); 

		//mTabsbarInstance2.mMessages_btn.setBackgroundResource(R.drawable.template_hover);
		mTabsbarInstance2.mMessageTemplates.setBackgroundResource(R.drawable.bottombg_hover);		
		mTabsbarInstance2.mMessageText.setTextColor(Color.parseColor("#000000"));*/

		listview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				String s = template.get(position);

				//String product = ((TextView) view).getText().toString();
				// Toast.makeText(getApplicationContext(), s, 3000).show();
				// Launching new Activity on selecting single List Item
				Intent i = new Intent(MessageTemplates.this, Sendsms.class);
				//sending data to new activity
				i.putExtra("messageTemplate", s);

				editor.putString("smsbody",s);
				editor.commit();
				startActivity(i);
				finish();

			}
		});

		listview.setOnItemLongClickListener(new OnItemLongClickListener() {

			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				final String s = id.get(position);
				final String edit = template.get(position);

				final int j= Integer.parseInt(s);

				Vibrator mVibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

				// Vibrate for 50 milliseconds
				mVibrator.vibrate(50);
				//String product = ((TextView) view).getText().toString();
				//	 Toast.makeText(getApplicationContext(), j, 3000).show();
				/*// Launching new Activity on selecting single List Item
				Intent i = new Intent(getApplicationContext(), Sendsms.class);
				//sending data to new activity
				i.putExtra("messageTemplate", s);

				  editor.putString("smsbody",s);
				  editor.commit();
				startActivity(i);*/


				AlertDialog.Builder alertDialog = new AlertDialog.Builder(MessageTemplates.this);

				// Setting Dialog Title
				alertDialog.setTitle("Wishem");

				// Setting Dialog Message
				alertDialog.setMessage("Are you sure you want to edit or delete this message template ?");

				// Setting Icon to Dialog
				alertDialog.setIcon(R.drawable.appicon);

				// Setting Positive "Yes" Button
				alertDialog.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int which) {

						// Write your code here to invoke YES event
						final Dialog dialog2 = new Dialog(context);
						dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);

						dialog2.setContentView(R.layout.dialog_message);
						Button dialogButton = (Button) dialog2.findViewById(R.id.dialog_cancel);
						saveButton = (Button) dialog2.findViewById(R.id.dialog_save);
						messageText = (EditText) dialog2.findViewById(R.id.message); 
						messageText.setText(edit);
						mTempSize=(TextView)dialog2.findViewById(R.id.TempSize);
						

						MessageTextWatch();
						// if button is clicked, close the custom dialog
						dialogButton.setOnClickListener(new OnClickListener() {

							public void onClick(View v) {
								dialog2.dismiss();
							}
						});
						saveButton.setOnClickListener(new OnClickListener() {

							public void onClick(View v) {
								String messageTemp = messageText.getText().toString();
								mt.UpdateRow(j, messageTemp);

								//addSqlData(messageTemp);
								Intent intent = getIntent();
								finish();
								startActivity(intent); 

								dialog2.dismiss();
							}
						});

						dialog2.show();
					}

				});

				// Setting Negative "NO" Button
				alertDialog.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// Write your code here to invoke NO event
						mt.deleterow(j);

						dialog.cancel();
						Intent intent = getIntent();
						finish();
						startActivity(intent); 
					}
				});

				// Showing Alert Message
				alertDialog.show();
				return false;
			}
		});

		newmessagetemp = (Button)findViewById(R.id.new_message);
		mMessageThreads = (Button)findViewById(R.id.message_thread);

		mNew_messageTemp= (Button)findViewById(R.id.new_messageTemp);

		newmessagetemp.setBackgroundResource(R.drawable.template_tab_hover);
		mNew_messageTemp.setOnClickListener(new OnClickListener() {


			public void onClick(View v) {



				final Dialog dialog = new Dialog(context);
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

				dialog.setContentView(R.layout.dialog_message);


				Button dialogButton = (Button) dialog.findViewById(R.id.dialog_cancel);
				saveButton = (Button) dialog.findViewById(R.id.dialog_save);
				saveButton.setEnabled(false);

				messageText = (EditText) dialog.findViewById(R.id.message); 

				mTempSize = (TextView)dialog.findViewById(R.id.TempSize);

				tempSize();
				MessageTextWatch();

				// if button is clicked, close the custom dialog
				dialogButton.setOnClickListener(new OnClickListener() {

					public void onClick(View v) {
						dialog.dismiss();
					}
				});

				saveButton.setOnClickListener(new OnClickListener() {

					public void onClick(View v) {
						String messageTemp = messageText.getText().toString();
						addSqlData(messageTemp);
						Intent intent = getIntent();
						finish();
						startActivity(intent); 

						dialog.dismiss();
					}
				});

				dialog.show();
			}
		});

		mMessageThreads.setOnClickListener(new OnClickListener() {


			public void onClick(View v) {

				Intent messagethreads = new Intent(getApplicationContext(), MessageThreads.class);

				startActivity(messagethreads);
				finish();

			}
		});




	}
	public void onBackPressed() {
		//Toast.makeText(getApplicationContext(), "Back pressed", 3000).show();
		AlertDialog.Builder alertDialog3 = new AlertDialog.Builder(MessageTemplates.this);
		alertDialog3.setMessage("Are you sure you want to exit?");
		alertDialog3.setCancelable(false);
		// Setting Icon to Dialog
		alertDialog3.setIcon(R.drawable.appicon);
		alertDialog3.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// Write your code here to invoke NO event
				//closeApplication();
				MessageTemplates.mFinish=true;
				finish();
			}
		});

		alertDialog3.setNegativeButton("No", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// Write your code here to invoke NO event
				dialog.cancel();
			}
		});

		alertDialog3.show();
	}



	

	public void MessageTextWatch()
	{
		enableSubmitIfReady();

		messageText.addTextChangedListener(new TextWatcher() {
			int mSize=160;
			int nos=1;
			public void afterTextChanged(Editable arg0) {
				enableSubmitIfReady();
			}

			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			public void onTextChanged(CharSequence s, int start, int before, int count) {

				int h = messageText.getText().toString().length();


				int mTextsize=mSize-h;				


				if(mTextsize==0){

					nos=nos+1;
					mSize=mSize+160;

				}

				mTempSize.setText("SMS Text "+mTextsize+"/"+nos);
				//Toast.makeText(getApplicationContext(), "Text changed"+mTextsize+"/"+nos, 1000).show();

			}
		});
	}

	public void enableSubmitIfReady() {

		boolean isReady = messageText.getText().toString().length()>1;


		if (isReady) {
			saveButton.setEnabled(true);
		} else {
			saveButton.setEnabled(false);
		}
	}
	public void tempSize()
	{
		int mSize=160;
		int nos=1;
		int h = messageText.getText().toString().length();
		int mTextsize=mSize-h;	
		mTempSize.setText("SMS Text "+mTextsize+"/"+nos);
		
	}

	private void addSqlData(String message) {
		SQLiteDatabase db = mt.getWritableDatabase();
		ContentValues values = new ContentValues();


		values.put(MessageTemplatesDatabase.MESSAGE, message);

		db.insert(MessageTemplatesDatabase.TABLE, null, values);
		Toast.makeText(getApplicationContext(), "New Message Template Created", 2000).show();
	}
	private Cursor getSqlData() {
		SQLiteDatabase db = mt.getReadableDatabase();
		Cursor cursor = db.query(MessageTemplatesDatabase.TABLE, null, null, null, null, null,
				null);

		// startManagingCursor(cursor);

		return cursor;
	}
	private void showSqlData(Cursor cursor) {

		if (cursor.moveToFirst()) {
			do {

				String ids = cursor.getString(0);
				String message = cursor.getString(1);

				template.add(message);
				id.add(ids);

			} while (cursor.moveToNext());
		}




	}




	class MessageAdapter extends BaseAdapter{

		//	private ArrayList<String> template;

		private  Context mcontext;
		public MessageAdapter(Context context){
			mcontext = context;

		}

		public int getCount() {
			// TODO Auto-generated method stub
			return template.size();
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
			View view = inflater.inflate( R.layout.template, parent, false );

			String   item = template.get(position);
			int len = item.length();
			String it="........";
			if(len > 20){

				item =item.substring(0,20);

				item=item+""+it;
			}
			TextView t = (TextView)view.findViewById(R.id.tvv);

			t.setText(item);



			//   product = ((TextView) view).getText().toString();
			//Toast.makeText(getApplicationContext(), item, 3000).show();
			return view;

		}




	}
}
