package com.shiva;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;


public class ReliefActivity extends MapActivity {
    /** Called when the activity is first created. */
    String response=null;
    double latitude;
    double longitude;
    private MyItemizedOverlay itemizedOverlay;

    MapView mapView;
	MapController mc;
	GeoPoint gp, gp1;
	List<GeoPoint> list = new ArrayList<GeoPoint>();
	public static ArrayList<Relief> mapArrayList = new ArrayList<Relief>();
    
	

	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mapView = (MapView) findViewById(R.id.map_view);
		mapView.setBuiltInZoomControls(true);
		mc = mapView.getController();
        new ReliefAsytask().execute();
        //Toast.makeText(getApplicationContext(), ""+latitude+""+longitude, 3000).show();
    }
    
    
    
    
    
    private class ReliefAsytask extends AsyncTask<Void, Void, Void> {


		
		
		protected void onPreExecute() {
			mapView.getOverlays().clear();
			
			list.clear();

			mapView.invalidate();
			mapArrayList.clear();
		}
		
		protected Void doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			
			ConnectionManager cm=new ConnectionManager();
			response=cm.makeGetRequestgetJSONResponse("http://api.sqoot.com/v2/deals?api_key=904t9j");
			
			
			
			return null;
		}
    
    
		protected void onPostExecute(Void result) {
			if (response != null) {
			try {

				JSONObject jsonObject = new JSONObject(response);

				JSONArray  jsonarray= jsonObject.getJSONArray("deals");

				
					for (int i = 0; i < jsonarray.length(); i++) {

						JSONObject jsonObject2 = jsonObject.getJSONArray("deals").getJSONObject(i);
						JSONObject jsonObject3 = jsonObject2.getJSONObject("deal");
						JSONObject jsonObject4 = jsonObject3.getJSONObject("merchant");
						Relief rf=new Relief();
						 rf.lat=jsonObject4.getString("latitude");
						 rf.lon=jsonObject4.getString("longitude");
						 mapArrayList.add(rf);
						
						 mapPoints();
				}
					
			}catch (Exception e) {
				// TODO: handle exception
			}
			}
			
		}
    
    }


    public void mapPoints() {

		for (int i = 0; i < mapArrayList.size(); i++) {

			latitude = Double.parseDouble(mapArrayList.get(i).lat);
			Log.d("TAG", "lattitude=" + latitude);

			longitude = Double.parseDouble(mapArrayList.get(i).lon);
			Log.d("TAG", "longitude=" + longitude);

			gp = new GeoPoint((int) (latitude * 1E6), (int) (longitude * 1E6));

			list.add(gp);

		}

		if (list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {

				Drawable drawable = this.getResources().getDrawable(
						R.drawable.mappin_green);

				gp1 = list.get(i);

				Log.v("TAG", "Geopoint=" + gp1);

				mc.setCenter(gp1);
				mc.setZoom(8);

				OverlayItem overlayItem = new OverlayItem(gp1,
						"", "");

				itemizedOverlay = new MyItemizedOverlay(drawable, mapView);
				itemizedOverlay.addOverlay(overlayItem);

				mapView.getOverlays().add(itemizedOverlay);
				mapView.invalidate();

			}

		} else {

			mapView.getOverlays().clear();
			// `mapView.invalidate();
		}

	}


	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
    
    
    
}