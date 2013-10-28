package com.zytrix.wishem;


import java.util.ArrayList;
import java.util.logging.Logger;


import com.zytrix.wishem.SaveContacts.Myadapter;


import android.app.Activity;
import android.content.ClipData.Item;
import android.content.ContentProviderOperation;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds;
import android.provider.ContactsContract.CommonDataKinds.GroupMembership;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.Groups;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;


public class ContactGroups extends Activity{

	private RelativeLayout mTabsBarR9 = null;
	/*private Tabsbar mTabsbarInstance9 = null;*/

	private Button contact_btn1;
	private Button groups_btn1;
	private Cursor mGroupCursor;
	private Context mContext;
	private ListView mGroupList;
	/*private CheckBox mGroupsCheckbox;*/
	private Button mAddcont_group;
	private Button mCancel_group;

	String phNo;
	private ArrayList<String> mGroupName = new ArrayList<String>();
	private ArrayList<String> mGroupIdList = new ArrayList<String>();
	private ArrayList<String> mGroupNumberList = new ArrayList<String>();
	private static ArrayList<String> mGroupContactList = new ArrayList<String>();
	private static ArrayList<String> mGroupContactNameList = new ArrayList<String>();
	Myadapter m1;

	public static  ArrayList<String> getmContactNumberList() {
		return mGroupContactList;
	}
	public static  ArrayList<String> getmContactNameList() {
		return mGroupContactNameList;
	}

	private static final String STAR_STATES = "listviewtipsandtricks:star_states";
	
	public  boolean[] mCheckStates;
	
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contacts_groups);

	/*	mTabsBarR9 = (RelativeLayout)findViewById(R.id.id_bottom_groups_rl);*/
		/*mTabsbarInstance9 = new Tabsbar(ContactGroups.this, "ContactGroups");
		mTabsBarR9.addView(mTabsbarInstance9); 
		mTabsbarInstance9.mGroups.setBackgroundResource(R.drawable.bottombg_hover);	 
		mTabsbarInstance9.mGroupsText.setTextColor(Color.parseColor("#000000"));*/

		contact_btn1 = (Button)findViewById(R.id.button_gr);
		groups_btn1 = (Button)findViewById(R.id.button2_gr);
		groups_btn1.setBackgroundResource(R.drawable.groups_tab_hover);
		mGroupList = (ListView)findViewById(R.id.list_groups);

		mContext = ContactGroups.this;
		contact_btn1.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent saveContacts = new Intent(getApplicationContext(), SaveContacts.class);
				startActivity(saveContacts);
				finish();

			}
		});

		groups_btn1.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent ContactGroups = new Intent(getApplicationContext(), ContactGroups.class);
				startActivity(ContactGroups);

			}
		});

		mAddcont_group = (Button)findViewById(R.id.addCont_group);
		mCancel_group = (Button)findViewById(R.id.cancel_group);

		mAddcont_group.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

			//	Toast.makeText(getApplicationContext(), "Checked"+mGroupNumberList, 3000).show();
				int j =mGroupNumberList.size();
				Log.v("TAG", "mGroupNumberList Size"+j);
				for (int i=0;i<j;i++){	
					getGroupContacts(mGroupNumberList.get(i));
				}
				Intent i = new Intent(getApplicationContext(), Sendsms.class);

				//i.putExtra("NumberList", mNumbersList);
				startActivity(i);
				finish();
			}
		});
		mCancel_group.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				
				Intent k = new Intent(getApplicationContext(), Sendsms.class);

				//i.putExtra("NumberList", mNumbersList);
				startActivity(k);
				finish();
			}
		});



		//mGroupCursor = getContentResolver().query(ContactsContract.Groups.CONTENT_URI, null, null, null, null );



		// Now you can get group ID from cursor

		// String groupRowId =  cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.GroupMembership.GROUP_ROW_ID));

		final String[] GROUP_PROJECTION = new String[] { ContactsContract.Groups._ID, ContactsContract.Groups.TITLE  };
		Cursor cursor = ContactGroups.this.managedQuery(ContactsContract.Groups.CONTENT_URI, GROUP_PROJECTION, null, null, ContactsContract.Groups.TITLE + " ASC");


		/*Uri groupURI = ContactsContract.Data.CONTENT_URI;


	    String[] projection = new String[]{
	     ContactsContract.CommonDataKinds.GroupMembership.GROUP_ROW_ID ,
	     ContactsContract.CommonDataKinds.GroupMembership.CONTACT_ID};

	    Cursor c = managedQuery(groupURI,
	    projection,
	    ContactsContract.CommonDataKinds.GroupMembership.GROUP_ROW_ID+"="+groupID,
	    null,null);
		 */

		int i= cursor.getCount();

		Log.v("TAG", "cursorsize"+i);
		if (cursor.moveToFirst()) {
			String mGroup = null;
			String mGroupId = null;



			do
			{
				mGroup = cursor.getString(cursor.getColumnIndex(Groups.TITLE));
				mGroupId = cursor.getString(cursor.getColumnIndex(Groups._ID));

				mGroupName.add(mGroup);
				mGroupIdList.add(mGroupId);



			}while(cursor.moveToNext());


		}


		
		//	ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, mGroupName);
		
		//ListView lv = getListView();
		
		
		if (savedInstanceState != null) {
			mCheckStates = savedInstanceState.getBooleanArray(STAR_STATES);
		}else {
			mCheckStates = new boolean[mGroupName.size()];
        }

		Log.v("TAG", "mGroupName.size()"+mGroupName.size());
		m1= new Myadapter(mGroupName,mGroupIdList);
		mGroupList.setAdapter(m1);
		//	mGroupList.setAdapter(adapter);
		//createGroup();
	}

	private void createGroup(String gName) {
		ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();

		ops.add(ContentProviderOperation
				.newInsert(ContactsContract.Groups.CONTENT_URI)
				.withValue(ContactsContract.Groups.TITLE, gName).build());
		try {

			getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);

			Log.v("", "the new contact group"+ops);

		} catch (Exception e) {
			Log.e("Error", e.toString());

		}

	}

	/*private ArrayList<String> getGroupContacts(String i)
	{
		Uri groupURI = ContactsContract.Data.CONTENT_URI;


		String[] projection = new String[]{
				ContactsContract.CommonDataKinds.GroupMembership.GROUP_ROW_ID ,
				ContactsContract.CommonDataKinds.GroupMembership.CONTACT_ID,
				ContactsContract.CommonDataKinds.GroupMembership.DISPLAY_NAME,
				ContactsContract.CommonDataKinds.Phone.NUMBER,
				
				
		};

		Cursor c = managedQuery(groupURI,
				projection,
				ContactsContract.CommonDataKinds.GroupMembership.GROUP_ROW_ID+"="+i+" AND "+CommonDataKinds.GroupMembership.MIMETYPE+"='"+CommonDataKinds.GroupMembership.CONTENT_ITEM_TYPE+"'",null,null);
	      String cid = c.getString(c.getColumnIndex(GroupMembership.RAW_CONTACT_ID));
	      
	      Cursor phoneCursor = getContentResolver().query(Phone.CONTENT_URI, new String[]{Phone.NUMBER,Phone.DISPLAY_NAME,Phone.TYPE}, Phone.CONTACT_ID+"="+cid,null,null);


		if (phoneCursor.moveToFirst()) {
			long name = 0;
			String mNumber = null;



			do
			{

				mNumber= phoneCursor.getString(phoneCursor.getColumnIndex(Phone.NUMBER));

				
			
				

				
				//mGroupContactList.add(mNumber);
				Log.v("TAG", "mGroupNumberList  contact no."+mNumber );
				



			}while(c.moveToNext());


		}

		return mGroupContactList;



	}*/
	
	
	
	private ArrayList<String> getGroupContacts(String groupId){
		
	 	ArrayList<Item> groupMembers = new ArrayList<Item>();
	 	
	 	String where =  CommonDataKinds.GroupMembership.GROUP_ROW_ID +"="+groupId
	       +" AND "
	       +CommonDataKinds.GroupMembership.MIMETYPE+"='"
	       +CommonDataKinds.GroupMembership.CONTENT_ITEM_TYPE+"'";
	 	String[] projection = new String[]{GroupMembership.RAW_CONTACT_ID,Data.DISPLAY_NAME};
	 	Cursor cursor = getContentResolver().query(Data.CONTENT_URI, projection, where,null,
	 			Data.DISPLAY_NAME+" COLLATE LOCALIZED ASC");
	 	while(cursor.moveToNext()){
	 	
	 		String name = cursor.getString(cursor.getColumnIndex(Data.DISPLAY_NAME));
	 		String id = cursor.getString(cursor.getColumnIndex(GroupMembership.RAW_CONTACT_ID));
	 		Cursor phoneFetchCursor = getContentResolver().query(Phone.CONTENT_URI,
	 				new String[]{Phone.NUMBER,Phone.DISPLAY_NAME,Phone.TYPE},
	 				Phone.CONTACT_ID+"="+id,null,null);
	 		
	 		
	 		while(phoneFetchCursor.moveToNext()){
	 			String phNo = phoneFetchCursor.getString(phoneFetchCursor.getColumnIndex(Phone.NUMBER));
	 			String phDisplayName = phoneFetchCursor.getString(phoneFetchCursor.getColumnIndex(Phone.DISPLAY_NAME));
	 			String phType = phoneFetchCursor.getString(phoneFetchCursor.getColumnIndex(Phone.TYPE));
	 			
	 			
	 			Log.v("TAG", "mGroupNumberList  contact Number."+phNo );
	 			mGroupContactList.add(phNo);
	 			mGroupContactNameList.add(phDisplayName+"<"+phNo+">");
	 		}
	 		phoneFetchCursor.close();
	 		//Log.v("TAG", "mGroupNumberList  contact Number."+phNo );
	 		//Log.v("TAG", "mGroupNumberList  contact Id."+id );
	 		//Log.v("TAG", "mGroupNumberList  contact Name."+name );
	 		
	 	}	
	 	cursor.close();
	 	return mGroupContactList;
	 }

	
	private int getGroupCount(String n){
		Uri groupURI = ContactsContract.Data.CONTENT_URI;


		String[] projection = new String[]{
				ContactsContract.CommonDataKinds.GroupMembership.GROUP_ROW_ID ,
				ContactsContract.CommonDataKinds.GroupMembership.CONTACT_ID,
				ContactsContract.CommonDataKinds.GroupMembership.DISPLAY_NAME,
				
				
		};
		
		Cursor c = managedQuery(groupURI,
				projection,
				ContactsContract.CommonDataKinds.GroupMembership.GROUP_ROW_ID+"="+n,
				null,null);
		c.getCount();
		return c.getCount();
		
	}

	class Myadapter extends BaseAdapter{

	private ArrayList<String> mGroupName = new ArrayList<String>();
		private ArrayList<String> mGroupIdList = new ArrayList<String>();

		ViewHolder holder;
		public Myadapter(ArrayList<String> tGroupName,ArrayList<String> tGroupIdList) {
			this.mGroupName = tGroupName;
			this.mGroupIdList = tGroupIdList;


			// TODO Auto-generated constructor stub
		}

		public int getCount() {
			return mGroupName.size();

		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {

			return position;
		}


		
		public View getView(int position, View convertView, ViewGroup Parent) {
			
			
			
			
			final String item = mGroupName.get(position);
			final String item1 = mGroupIdList.get(position);
			
			int k = getGroupCount(item1);
			String p = "("+k+")";
			if( convertView == null){

				
			holder = new ViewHolder();
				
			LayoutInflater inflater1 = (LayoutInflater)mContext.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
			convertView = inflater1.inflate( R.layout.groupsrow, Parent, false );

	
			convertView.setTag(holder);
			}
			else {
				holder = (ViewHolder)convertView.getTag();
				
			}

			

			holder.tv1 = (TextView)convertView.findViewById( R.id.tva_gr );
			holder.tv2 = (TextView)convertView.findViewById( R.id.tvb_gr );



		holder.mcbox = (CheckBox)convertView.findViewById(R.id.chk_gr);

		
		holder.mcbox.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {

				if (((CheckBox) v).isChecked())
				{
					mGroupNumberList.add(item1);

					
				}
				else
				{
					mGroupNumberList.remove(item1);

					
				}




			}
		});

					//holder.mcbox.setChecked(false);
					holder.mcbox.setOnCheckedChangeListener(null);
					holder.mcbox.setChecked(mCheckStates[position]);
					holder.mcbox.setOnCheckedChangeListener(mStarCheckedChanceChangeListener);

					holder.tv2.setText(p);
					holder.tv1.setText(item);	


					return convertView;
		}




	}
	
	public class ViewHolder {

		CheckBox mcbox;
		TextView tv1,tv2;

	}

	private OnCheckedChangeListener mStarCheckedChanceChangeListener = new OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			final int position = mGroupList.getPositionForView(buttonView);
			if (position != ListView.INVALID_POSITION) {
				mCheckStates[position] = isChecked;
			}
		}



	};

}
