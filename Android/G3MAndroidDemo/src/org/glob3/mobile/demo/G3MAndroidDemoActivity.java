package org.glob3.mobile.demo;

import org.glob3.mobile.specific.G3MWidget_Android;

import android.app.Activity;
import android.os.Bundle;

public class G3MAndroidDemoActivity extends Activity {
	
	G3MWidget_Android _widget = null;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        if (_widget == null){ //Just the first time
        	_widget = new G3MWidget_Android(this);
        	setContentView(_widget);
        }
    }
}