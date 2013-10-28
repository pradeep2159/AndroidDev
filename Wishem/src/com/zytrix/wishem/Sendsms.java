package com.zytrix.wishem;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;





import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView.FindListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class Sendsms extends Activity {


	private Button buttonSend;
	private Button addContacts;
	private EditText textPhoneNo;
	private EditText textSMS;
	private String m_smsBody = null; 
	public  String phoneNo="";
	public String sms;
	public static Sendsms sm;

	MessageThreadDatabase mtg;

	private Bundle b1;
	//private String mNumbers = null;
	public ArrayList<String> phoneNumbers = new ArrayList<String>();
	public ArrayList<String> NameNumbers = new ArrayList<String>();

	public  ArrayList<String> mGroupcontacts = new ArrayList<String>();
	public  ArrayList<String> mGroupcontactNames = new ArrayList<String>();
	public int j= 0;
	private int mYear;
	private int mMonth;
	private int mDay;
	private int mHour;
	private int mMinute;
	private String listString="";
	long millis;
	private long recurrentTime;
	private Date setDate;
	private Spinner mSpinner;

	private Button mSmsbtntime;
	private Button mSmsbtndate;
	private TextView mLbldatetime;
	private TextView mSmsSize;
	private Context mContext;
	private int  notif=0;

	private int mPos;
	private long mId;
	public int UID;



	public int getUID() {
		return UID;
	}

	public void setUID(int uID) {
		UID = uID;
	}

	private ArrayList<String> spinnerList = new ArrayList<String>();

	AlarmManager am;

	final Calendar myCalendar = Calendar.getInstance();
	DateFormat fmtDateAndTime1 = DateFormat.getDateTimeInstance();
	SimpleDateFormat fmtDateAndTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	SimpleDateFormat Dateformat = new SimpleDateFormat("dd-MM-yyyy");
	SimpleDateFormat Timeformat = new SimpleDateFormat("HH:mm:ss");

	protected void onResume()
	{
		super.onResume();
		if(MessageTemplates.mFinish)
		{
			finish();
		}
	}
	public void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.sendsms);
		MessageTemplates.mFinish=false;
		super.onCreate(savedInstanceState);
		buttonSend = (Button) findViewById(R.id.buttonSend);
		addContacts = (Button) findViewById(R.id.addHere);
		textPhoneNo = (EditText)findViewById(R.id.editTextPhoneNo);


		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		final SharedPreferences.Editor epn = preferences.edit();

		mSmsSize= (TextView)findViewById(R.id.sms_size);

		mSmsSize.setVisibility(View.VISIBLE);

		textSMS = (EditText)findViewById(R.id.editTextSMS);

		mtg = new MessageThreadDatabase(this);

		textWatch();
		// Spinner Start
		mSpinner = (Spinner) findViewById(R.id.spinner1);


		spinnerList.add("Once");
		spinnerList.add("Every 5 Minutes");
		spinnerList.add("Every 15 Minutes");
		spinnerList.add("Every half an Hour");
		spinnerList.add("Every one hour");
		spinnerList.add("Every 2 hours");
		spinnerList.add("Every 4 hours");
		spinnerList.add("Every 8 hours");
		spinnerList.add("Every weekday(Mon-Fri)");
		spinnerList.add("Every weekend(Sat and Sun)");
		spinnerList.add("Every Day");
		spinnerList.add("Every Week");
		spinnerList.add("Every Month");
		spinnerList.add("Every Year");

		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, spinnerList);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSpinner.setAdapter(dataAdapter);

		// spinner on item click
		mSpinner.setOnItemSelectedListener((OnItemSelectedListener) mContext);


		// Date picker and time picker start

		mSmsbtndate = (Button) findViewById(R.id.sms_btnDate);
		mSmsbtntime = (Button) findViewById(R.id.sms_btnTime);





		final DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				myCalendar.set(Calendar.YEAR, year);
				myCalendar.set(Calendar.MONTH, monthOfYear);
				myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);


				updateMillies();

			}
		};

		final TimePickerDialog.OnTimeSetListener t = new TimePickerDialog.OnTimeSetListener() {
			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
				myCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
				myCalendar.set(Calendar.MINUTE, minute);


				updateMillies();

			}
		};





		mSmsbtndate.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				new DatePickerDialog(Sendsms.this, d, myCalendar
						.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
						myCalendar.get(Calendar.DAY_OF_MONTH)).show();
			}
		});
		mSmsbtntime.setOnClickListener(new View.OnClickListener() {
			public  void onClick(View v) {

				new TimePickerDialog(Sendsms.this, t, myCalendar
						.get(Calendar.HOUR_OF_DAY), myCalendar
						.get(Calendar.MINUTE), true).show();
			}
		});

		updateMillies();

		//millis=myCalendar.getTimeInMillis();



		Log.v("TAG", "mills"+millis);

		//date and time picker ends and get milli secs



		Intent in = getIntent();
		m_smsBody = in.getStringExtra("messageTemplate");
		//		textSMS.setText(m_smsBody);


		SharedPreferences preferences1 = PreferenceManager.getDefaultSharedPreferences(this);
		String smsText = preferences1.getString("smsbody","");


		textSMS.setText(smsText);

		//		Intent in1 =getIntent();
		//		mNumbers = in1.getStringExtra("NumberList");		

		

		phoneNumbers=SaveContacts.getmNumbersList();
		mGroupcontacts=ContactGroups.getmContactNumberList();
		mGroupcontactNames=ContactGroups.getmContactNameList();
		phoneNumbers.addAll(mGroupcontacts);


		NameNumbers=SaveContacts.getmNameNumbersList();
		NameNumbers.addAll(mGroupcontactNames);




		for (String s : NameNumbers)
		{
			listString += s + ",";
		}

		textPhoneNo.setText(listString);



		//phoneNumbers.add("5556");
		//phoneNumbers.add("5554");

		//	String s = phoneNumbers.get(1);

		// Intent intent = new Intent(Intent.ACTION_VIEW);
		//intent.putExtra("sms_body",m_smsBody );
		//intent.setType("vnd.android-dir/mms-sms");
		//startActivity(intent);








		addContacts.setOnClickListener(new OnClickListener() {


			public void onClick(View v) {


				Intent i = new Intent(getApplicationContext(), SaveContacts.class);
				b1=new Bundle();
				b1.putStringArrayList("pno", phoneNumbers);
				b1.putStringArrayList("pnm,", NameNumbers);
				i.putExtras(b1);
				startActivity(i);

				

			}


		});

		buttonSend.setEnabled(false);
		buttonSend.setTextColor(Color.parseColor("#000000"));
		textWatch();

		buttonSend.setOnClickListener(new OnClickListener() {


			public void onClick(View v) {


				long cTime = System.currentTimeMillis()-60000;


				if(millis < cTime){

					AlertDialog.Builder alertDialog4 = new AlertDialog.Builder(Sendsms.this);
					alertDialog4.setMessage("Either Date or Time is incorrect, Please choose Proper Date or Time");
					alertDialog4.setCancelable(false);
					// Setting Icon to Dialog
					alertDialog4.setIcon(R.drawable.appicon);
					alertDialog4.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							// Write your code here to invoke NO event
							dialog.cancel();
						}
					});


					alertDialog4.show();
				}
				else{

					Log.v("TAG", "mills"+millis);
					/*Toast.makeText(getApplicationContext(), "SMS Time"+millis,Toast.LENGTH_LONG).show();*/

					phoneNo = textPhoneNo.getText().toString();


					sms = textSMS.getText().toString();
					UID = (int) (System.currentTimeMillis() & 0xfffffff);



					String Text = mSpinner.getSelectedItem().toString();
					String Status= "Message yet to Send";
					mPos = mSpinner.getSelectedItemPosition();
					switch(mPos) {
					case 0:
						setOneTimeSMS(millis,UID);
						break;
					case 1:
						recurrentTime = 5*60*1000;//every 5 minutes
						setRepeatingSMS(millis,recurrentTime,UID);
						break;
					case 2:
						recurrentTime = 15*60*1000;//every 15 minutes
						setRepeatingSMS(millis,recurrentTime,UID);
						break;
					case 3:
						recurrentTime = 30*60*1000;//every 30 minutes
						setRepeatingSMS(millis,recurrentTime,UID);
						break;
					case 4:
						recurrentTime = 60*60*1000;//every one hour
						setRepeatingSMS(millis,recurrentTime,UID);
						break;
					case 5:
						recurrentTime = 120*60*1000;//every two hours
						setRepeatingSMS(millis,recurrentTime,UID);
						break;
					case 6:
						recurrentTime = 240*60*1000;//every four hours
						setRepeatingSMS(millis,recurrentTime,UID);
						break;
					case 7:
						recurrentTime = 480*60*1000;//every eight hours
						setRepeatingSMS(millis,recurrentTime,UID);
						break;
					case 8:
						recurrentTime = 3*480*60*1000;//every day 
						setRepeatingSMS(millis,recurrentTime,UID);
						break;
					case 9:
						recurrentTime = 7*3*480*60*1000;//every day 
						setRepeatingSMS(millis,recurrentTime,UID);
						break;
					case 10:
						recurrentTime = 30*480*60*1000;//every day 
						setRepeatingSMS(millis,recurrentTime,UID);
						break;
					case 11:
						recurrentTime = 365*480*60*1000;//every day 
						setRepeatingSMS(millis,recurrentTime,UID);
						break;



						/* default:
			        		  //setOneTimeSMS(millis);
						 */		        		  

					}


					addSqlThreads(phoneNo,UID,Text,Status,sms);

					Intent messagethreads = new Intent(getApplicationContext(), MessageThreads.class);

					finish();
					startActivity(messagethreads);



				}



			}




		});
	}

	private void textWatch() {
		enableSubmitIfReady();

		// TODO Auto-generated method stub
		textPhoneNo.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable arg0) {
				enableSubmitIfReady();
			}

			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			public void onTextChanged(CharSequence s, int start, int before, int count) {
				int h = textSMS.getText().toString().length();
				//Toast.makeText(getApplicationContext(), "Text changed"+h, 3000).show();



			}
		});
		textSMS.addTextChangedListener(new TextWatcher() {
			int mSize=160;
			int nos=1;
			public void afterTextChanged(Editable arg0) {
				enableSubmitIfReady();
			}

			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			public void onTextChanged(CharSequence s, int start, int before, int count) {
				int h = textSMS.getText().toString().length();
				//	Toast.makeText(getApplicationContext(), "Text changed"+h, 3000).show();

				int mTextsize=mSize-h;				


				if(mTextsize==0){

					nos=nos+1;
					mSize=mSize+160;

				}

				mSmsSize.setText("Text Count "+mTextsize+"/"+nos);

			}
		});

	}


	public void enableSubmitIfReady() {

		boolean isReady = textPhoneNo.getText().toString().length()>2;
		boolean smst = textSMS.getText().toString().length()>1;

		boolean str = phoneNumbers.size()>2;

		if (isReady && smst) {
			buttonSend.setEnabled(true);
			buttonSend.setTextColor(Color.parseColor("#FFFFFF"));
		} else if(str) {
			buttonSend.setEnabled(true);
			buttonSend.setTextColor(Color.parseColor("#FFFFFF"));

		}else {
			buttonSend.setEnabled(false);
			buttonSend.setTextColor(Color.parseColor("#000000"));
		}
	}


	private void addSqlThreads(String number,int uid,String text,String status, String message) {
		SQLiteDatabase db2 = mtg.getWritableDatabase();
		ContentValues values = new ContentValues();

		values.put(MessageThreadDatabase.UID, uid);
		values.put(MessageThreadDatabase.NUMBER, number);		
		values.put(MessageThreadDatabase.FREQ, text);
		values.put(MessageThreadDatabase.STATUS, status);
		values.put(MessageThreadDatabase.MESSAGE, message);

		db2.insert(MessageThreadDatabase.TABLE, null, values);

	}


	/*private void ScheduledSMS() {
		Log.v("TAG", "Scheduled sms called............................");

		if(phoneNumbers!=null)
		{
			phoneNumbers.add(phoneNo);
		}

		// String sms = m_smsBody;
		j = phoneNumbers.size();
		try {
			for (int i=0;i<j;i++){			  

				SmsManager smsManager = SmsManager.getDefault();
				smsManager.sendTextMessage(phoneNumbers.get(i), null, sms, null, null);
				Toast.makeText(getApplicationContext(), "SMS Sent!"+i,Toast.LENGTH_LONG).show();
			}
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(),"SMS faild, please try again later!",Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}
		phoneNumbers.clear();

	}

	 */


	private void setOneTimeSMS(long datetime,int iUniqueId) {



		am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);


		//Toast.makeText(getApplicationContext(), "Alaram Added"+datetime, 3000).show();
		Intent intent = new Intent(this, SmsAlarm.class);
		String iuid;
		iuid=""+iUniqueId;
		intent.putExtra("uid", iuid);

		Bundle mBundle = new Bundle();

		mBundle.putStringArrayList("SourceList", phoneNumbers);
		mBundle.putString("Sourcemsg", sms);
		mBundle.putString("phno", phoneNo);
		intent.putExtras(mBundle);


		//PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
		PendingIntent pi = PendingIntent.getBroadcast(this, iUniqueId, intent, 0);
		am.set(AlarmManager.RTC_WAKEUP, datetime, pi);

		/*PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, new Intent(SENT), 0);
        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0, new Intent(DELIVERED), 0);
		 */



	}
	public void setRepeatingSMS(long datetime,long repeatTime,int rUniqueId) {

		Bundle mBundle = new Bundle();


		am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(this, SmsAlarm.class);

		mBundle.putStringArrayList("SourceList", phoneNumbers);
		mBundle.putString("Sourcemsg", sms);
		mBundle.putString("phno", phoneNo);
		intent.putExtras(mBundle);

		PendingIntent pendingIntent = PendingIntent.getBroadcast(this, rUniqueId, intent, 0);
		am.setRepeating(AlarmManager.RTC_WAKEUP, datetime,repeatTime, pendingIntent);


	}

	/*public void SentSMS(){
		int rec = phoneNumbers.size();
		for(int i=0;i<rec;i++)
		{
			ContentValues values = new ContentValues();
			values.put("address", phoneNumbers.get(i));
			values.put("body", sms); 
			getApplicationContext().getContentResolver().insert(Uri.parse("content://sms/sent"), values); 

		}
	}*/
	private void updateMillies(){

		//mLbldatetime.setText("Date: "+Dateformat.format(myCalendar.getTime())+" Time: "+Timeformat.format(myCalendar.getTime()));
		setDate = myCalendar.getTime();
		myCalendar.setTime(setDate);
		millis=myCalendar.getTimeInMillis();

		mSmsbtndate.setText("Date: "+Dateformat.format(myCalendar.getTime()));
		mSmsbtntime.setText("Time: "+Timeformat.format(myCalendar.getTime()));

	}

	public void onBackPressed() {

		Intent t1 = new Intent(getApplicationContext(), MessageTemplates.class);

		startActivity(t1);
		finish();
	}


	/*protected void onResume() {
	 ScheduledSMS();
	// TODO Auto-generated method stub
	super.onResume();
}
	 */

	@Override
	public void onDestroy()
	{
		super.onDestroy();
		phoneNumbers.clear();
		NameNumbers.clear();
		mGroupcontacts.clear();
		mGroupcontactNames.clear();
		
		/* Toast.makeText(getApplicationContext(),"16. onDestroy()", Toast.LENGTH_SHORT).show();*/
	}
	
	
	
	/*public void onBackPressed() {
		phoneNumbers.clear();
		NameNumbers.clear();
		mGroupcontacts.clear();
		finish();
	}*/
	/*public void SentSMS(ArrayList<String> nlist, String mMessage) {
		int rec = nlist.size();
		for(int i=0;i<rec;i++)
		{
			ContentValues values = new ContentValues();
			values.put("address", nlist.get(i));
			values.put("body", mMessage); 
			this.getContentResolver().insert(Uri.parse("content://sms/sent"), values); 

		}

	}*/







}
