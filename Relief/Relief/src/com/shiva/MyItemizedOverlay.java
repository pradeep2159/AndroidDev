package com.shiva;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;
import com.mp.mapoverlays.BalloonItemizedOverlay;

public class MyItemizedOverlay <Item extends OverlayItem> extends BalloonItemizedOverlay<OverlayItem> {

    private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();

    public MyItemizedOverlay(Drawable defaultMarker, MapView mapView) {
        super(boundCenter(defaultMarker), mapView);

    }

    public void addOverlay(OverlayItem overlay) {
        mOverlays.add(overlay);
        populate();
    }

    public void clear() {

        mOverlays.clear();
        populate();
    }

    @Override
    protected OverlayItem createItem(int i) {
        return mOverlays.get(i);
    }

    @Override
    public int size() {
        return mOverlays.size();
    }

//    @Override
//    protected boolean onTap(int index) {
//    	 OverlayItem item = mOverlays.get(index);
//    	 
//    	
//        return true;
//    }

    @Override
    public boolean onTouchEvent(MotionEvent event, MapView mapView){

        return false;
    }

}