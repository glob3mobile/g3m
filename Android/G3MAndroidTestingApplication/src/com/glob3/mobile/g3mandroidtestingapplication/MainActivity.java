

package com.glob3.mobile.g3mandroidtestingapplication;

import org.glob3.mobile.generated.G3MContext;
import org.glob3.mobile.generated.GTask;
import org.glob3.mobile.generated.Geodetic2D;
import org.glob3.mobile.generated.Sector;
import org.glob3.mobile.generated.TimeInterval;
import org.glob3.mobile.specific.G3MBuilder_Android;
import org.glob3.mobile.specific.G3MWidget_Android;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.RelativeLayout;


public class MainActivity
         extends
            Activity {

   private G3MWidget_Android _g3mWidget;
   private RelativeLayout    _placeHolder;


   @Override
   protected void onCreate(final Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);

      setContentView(R.layout.activity_main);
      final G3MBuilder_Android builder = new G3MBuilder_Android(this);
      builder.getPlanetRendererBuilder().setRenderDebug(true);
      _g3mWidget = builder.createWidget();
      _placeHolder = (RelativeLayout) findViewById(R.id.g3mWidgetHolder);
      _placeHolder.addView(_g3mWidget);
      
      if (true) { //Incomplete world

          int time = 5; //SECS

          class RenderedSectorTask extends GTask{
            G3MWidget_Android _androidWidget;
          
            public  RenderedSectorTask(G3MWidget_Android androidWidget){ _androidWidget = androidWidget;}

            int randomInt(int max) {
            	return (int) (Math.random() * max);
            }


    		@Override
    		public void run(G3MContext context) {

                double minLat = randomInt(180) -90;
                double minLon = randomInt(360) - 180;

                double maxLat = minLat + randomInt(90 - (int)minLat);
                double maxLon = minLon + randomInt(90 - (int)minLat);

                Sector sector = Sector.fromDegrees(minLat, minLon, maxLat, maxLon);
                _androidWidget.getG3MWidget().setShownSector(sector);
    			
    		}
          };
          _g3mWidget.getG3MWidget().addPeriodicalTask(
        		  TimeInterval.fromSeconds(time), 
        		  new RenderedSectorTask(_g3mWidget));
        }

   }


   @Override
   public boolean onCreateOptionsMenu(final Menu menu) {
      // Inflate the menu; this adds items to the action bar if it is present.
      getMenuInflater().inflate(R.menu.main, menu);
      return true;
   }

}
