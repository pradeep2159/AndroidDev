package com.zytrix.wishem;



import java.util.ArrayList;
import java.util.List;






import android.R.integer;
import android.app.Activity;
import android.app.ListActivity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;


public class SaveContacts extends Activity {



	ContactsDB contacts;
	Context context;
	Button addCont;
	Button smcancel;
	CheckBox checkbox;
	int tag=0;
	int textlength = 0;

	private boolean[] mCheckStates;
	private RelativeLayout mTabsBarR3 = null;
	/*private Tabsbar mTabsbarInstance3 = null;*/
	private EditText inputSearch;

	public ListView list;
	private ArrayList<String> contactno = new ArrayList<String>();

	private ArrayList<String> contactname = new ArrayList<String>();

	private ArrayList<String> contactno_sort = new ArrayList<String>();

	private ArrayList<String> contactname_sort = new ArrayList<String>();

	private static  ArrayList<String> mNumbersList = new ArrayList<String>();
	private static  ArrayList<String> mNameNumbersList = new ArrayList<String>();

	private Myadapter b1;
	private Button contact_btn;
	private Button groups_btn;
	private static final String STAR_STATES = "listviewtipsandtricks:star_states";


	public static  ArrayList<String> getmNumbersList() {
		return mNumbersList;
	}
	public static  ArrayList<String> getmNameNumbersList() {
		return mNameNumbersList;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contacts);


		/*mTabsbarInstance3 = new Tabsbar(SaveContacts.this, "SaveContacts");
		mTabsBarR3.addView(mTabsbarInstance3); 

		mTabsbarInstance3.mGroups.setBackgroundResource(R.drawable.bottombg_hover);		
		mTabsbarInstance3.mGroupsText.setTextColor(Color.parseColor("#000000"));*/

		contact_btn = (Button)findViewById(R.id.button);
		contact_btn.setBackgroundResource(R.drawable.contacts_tab_hover);

		groups_btn = (Button)findViewById(R.id.button2);


		addCont = (Button) findViewById(R.id.addCont);
		smcancel = (Button) findViewById(R.id.cancel);
		inputSearch = (EditText) findViewById(R.id.input_search_query);

		list =(ListView)findViewById(R.id.list);


		int k = mNumbersList.size();

		context = SaveContacts.this;
		try
		{
			/*String[] PROJECTION=new String[] {Contacts._ID,
					Contacts.DISPLAY_NAME,
					Phone.NUMBER
			};*/


			Cursor c =getContentResolver().query(Phone.CONTENT_URI, null, null, null,Phone.DISPLAY_NAME + " ASC");

			if (c.moveToFirst()) {
				String ClsPhonename = null;
				String ClsphoneNo = null;

				do
				{
					ClsPhonename = c.getString(c.getColumnIndex(Phone.DISPLAY_NAME));
					ClsphoneNo = c.getString(c.getColumnIndex(Phone.NUMBER));



					contactno.add(ClsphoneNo);
					contactname.add(ClsPhonename);

					//	Log.v("", "Phonre no:"+Phone.CONTENT_URI+"    "+ClsPhonename);


				}while(c.moveToNext());

			}
		}catch(Exception e)
		{

		}




		contact_btn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent saveContacts = new Intent(getApplicationContext(), SaveContacts.class);
				startActivity(saveContacts);

			}
		});
		groups_btn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent ContactGroups = new Intent(getApplicationContext(), ContactGroups.class);
				startActivity(ContactGroups);
				finish();

			}
		});

		addCont.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				//				Toast.makeText(getApplicationContext(), "Checked"+mNumbersList, 3000).show();
				Intent i = new Intent(getApplicationContext(), Sendsms.class);
				i.putStringArrayListExtra("NumberList", mNumbersList);
				//i.putExtra("NumberList", mNumbersList);
				startActivity(i);
				finish();
			}
		});
		smcancel.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				mNumbersList.clear();
				Intent i = new Intent(getApplicationContext(), Sendsms.class);
				startActivity(i);
				finish();
			}
		});

		b1 = new Myadapter(contactno,contactname);

		//ListView lv = getListView();
		if (savedInstanceState != null) {
			mCheckStates = savedInstanceState.getBooleanArray(STAR_STATES);
		}else {
			mCheckStates = new boolean[b1.getCount()];
		}




		list.setAdapter(b1);

		inputSearch.addTextChangedListener(new TextWatcher() {


			public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {

				textlength = inputSearch.getText().length();

				contactname_sort.clear();
				contactno_sort.clear();


				for (int i = 1; i < contactname.size(); i++)
				{
					if (textlength <= contactname.get(i).length())
					{
						if (inputSearch.getText().toString().equalsIgnoreCase((String) contactname.get(i).subSequence(0, textlength)))
						{
							contactname_sort.add(contactname.get(i));
							contactno_sort.add(contactno.get(i));
						}
					}
				}
				list.setAdapter(new Myadapter(contactno_sort,contactname_sort));

				//	list.setAdapter(B1);

				//	Toast.makeText(getApplicationContext(), "Text changed"+contactname.size()+contactno.size(), 3000).show();
			}

			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub

			}

			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
			}


		});



		//Toast.makeText(getApplicationContext(), "Text changed"+contactname.size()+contactno.size(), 3000).show();

	}// oncreat ends
	/*protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putBooleanArray(STAR_STATES, mCheckStates);
	}
	 */




	private void addSqlData(String name, String mobile) {
		SQLiteDatabase db = contacts.getWritableDatabase();
		ContentValues values = new ContentValues();

		values.put(ContactsDB.NAME, name);
		values.put(ContactsDB.MOBILE, mobile);

		db.insert(ContactsDB.TABLE, null, values);

		Log.v("","Data inserted");

	}

	private static class ViewHolder {

		public CheckBox cbox;
		public TextView tv,tvb;

	}

	class Myadapter extends BaseAdapter{



		private ArrayList<String> contactno;
		private ArrayList<String> contactname;
		ViewHolder holder;

		public Myadapter(ArrayList<String> contactno, ArrayList<String> contactname) {
			this.contactno = contactno;
			this.contactname = contactname;

			// TODO Auto-generated constructor stub
		}

		public int getCount() {
			return contactno.size();

		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {

			return position;
		}


		public View getView(int position, View convertView, ViewGroup Parent) {




			final String item = contactno.get(position);
			final String item1 = contactname.get(position);



			if(convertView == null){

				holder = new ViewHolder();

				LayoutInflater inflater = (LayoutInflater)
						context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
				convertView = inflater.inflate( R.layout.rowxml, Parent, false );
				convertView.setTag(holder);

			}else {
				holder = (ViewHolder)convertView.getTag();
			}

			holder.tv = (TextView)convertView.findViewById( R.id.tva );
			holder.tvb = (TextView)convertView.findViewById( R.id.tvb );

			holder.tvb.setText( item );
			holder.tv.setText( item1 );

			holder.cbox= (CheckBox)convertView.findViewById(R.id.chk);
			holder.cbox.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					if (((CheckBox) v).isChecked())
					{
						mNumbersList.add(item);

						mNameNumbersList.add(item1+"<"+item+">");



					}
					else{
						mNumbersList.remove(item);
						mNameNumbersList.remove(item1+"<"+item+">");



						//	Toast.makeText(getApplicationContext(), "Unchecked"+mNumbersList, 3000).show();
					}

				}

			});
			holder.cbox.setOnCheckedChangeListener(null);
			holder.cbox.setChecked(mCheckStates[position]);
			holder.cbox.setOnCheckedChangeListener(mStarCheckedChanceChangeListener);

			//return view;


			return convertView;
		}





	}



	private OnCheckedChangeListener mStarCheckedChanceChangeListener = new OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			final int position = list.getPositionForView(buttonView);
			if (position != ListView.INVALID_POSITION) {
				mCheckStates[position] = isChecked;
			}
		}



	};



}







