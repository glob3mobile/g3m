

package com.glob3.mobile.g3mandroidtestingapplication;

import org.glob3.mobile.generated.Angle;
import org.glob3.mobile.generated.ElevationDataProvider;
import org.glob3.mobile.generated.Geodetic2D;
import org.glob3.mobile.generated.Sector;
import org.glob3.mobile.generated.SingleBillElevationDataProvider;
import org.glob3.mobile.generated.URL;
import org.glob3.mobile.generated.Vector2I;
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
      
      if (true){ //testing small shown sector

          final Geodetic2D lower = new Geodetic2D( //
                   Angle.fromDegrees(40.1665739916489), //
                   Angle.fromDegrees(-5.85449532145337));
          final Geodetic2D upper = new Geodetic2D( //
                   Angle.fromDegrees(40.3320215899527), //
                   Angle.fromDegrees(-1.5116079822178570));

          final Sector demSector = new Sector(lower, upper);

          //The sector is shrinked to adjust the projection of
          builder.setShownSector(demSector.shrinkedByPercent(0.1f));
      }
      
      
      _g3mWidget = builder.createWidget();
      _placeHolder = (RelativeLayout) findViewById(R.id.g3mWidgetHolder);
      _placeHolder.addView(_g3mWidget);
   }


   @Override
   public boolean onCreateOptionsMenu(final Menu menu) {
      // Inflate the menu; this adds items to the action bar if it is present.
      getMenuInflater().inflate(R.menu.main, menu);
      return true;
   }

}
