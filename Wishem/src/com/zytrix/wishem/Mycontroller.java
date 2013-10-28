package com.zytrix.wishem;

import android.app.Activity;

class MyController {
    private static MyController instance;
    private Activity Sendsm;


    private MyController() {}
    public static synchronized MyController getInstance() {
        if(instance == null) {
            instance = new MyController();
            
        }

        return instance;
    }

    public void setActivity1(Activity activityObject) { Sendsm = activityObject; }
   

    public void closeAllActivities() {
        if(Sendsm != null) {
        	Sendsm.finish();
        }
       
     
    }
}