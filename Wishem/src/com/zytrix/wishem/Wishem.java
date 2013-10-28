package com.zytrix.wishem;





import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

public class Wishem extends Activity {
	
	private RelativeLayout mTabsBarRl = null;
	/*private Tabsbar mTabsbarInstance = null;*/
	
	final Context context=this;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishem);
        
        mTabsBarRl = (RelativeLayout)findViewById(R.id.id_bottom_rl);
		/*mTabsbarInstance = new Tabsbar(Wishem.this, "Wishem");
		mTabsBarRl.addView(mTabsbarInstance); */
		
           
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_wishem, menu);
        return true;
    }
}
