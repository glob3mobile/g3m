

package org.glob3.mobile.demo;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


public class G3MDemoActivity
         extends
            Activity {


   @Override
   protected void onDestroy() {
      Log.i(getClass().toString(), "Destroy");
      super.onDestroy();
   }


   @Override
   protected void onPause() {
      Log.i(getClass().toString(), "Pause");
      super.onDestroy();
   }


   @Override
   public void onCreate(final Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_g3_mdemo);

      final Button simpleRasterLayerButton = (Button) findViewById(R.id.simpleRasterLayerButton);


      simpleRasterLayerButton.setOnClickListener(new OnClickListener() {
         @Override
         public void onClick(final View v) {
            final Intent intent = new Intent(getApplicationContext(), SimplestRasterActivity.class);
            startActivity(intent);
         }
      });

      final Button doubleG3MButton = (Button) findViewById(R.id.doubleGlob3G3MButton);


      doubleG3MButton.setOnClickListener(new OnClickListener() {
         @Override
         public void onClick(final View v) {
            final Intent intent = new Intent(getApplicationContext(), G3MDoubleGlob3Activity.class);
            startActivity(intent);
         }
      });


      final Button scenarioButton = (Button) findViewById(R.id.scenarioButton);


      scenarioButton.setOnClickListener(new OnClickListener() {
         @Override
         public void onClick(final View v) {
            final Intent intent = new Intent(getApplicationContext(), ScenarioTerrainActivity.class);
            startActivity(intent);
         }
      });


      final Button drawingShapesG3MButton = (Button) findViewById(R.id.threedshapes);
      drawingShapesG3MButton.setOnClickListener(new OnClickListener() {
         @Override
         public void onClick(final View v) {
            final Intent intent = new Intent(getApplicationContext(), G3MDrawingShapesActivity.class);
            startActivity(intent);
         }
      });


      final Button markG3MButton = (Button) findViewById(R.id.markersGlob3G3MButton);
      markG3MButton.setOnClickListener(new OnClickListener() {
         @Override
         public void onClick(final View v) {
            final Intent intent = new Intent(getApplicationContext(), G3MShowMarkersActivity.class);
            startActivity(intent);
         }
      });

      final Button netCDFButton = (Button) findViewById(R.id.netCDFG3MButton);
      netCDFButton.setOnClickListener(new OnClickListener() {
         @Override
         public void onClick(final View v) {
            final Intent intent = new Intent(getApplicationContext(), G3MNetCDFActivity.class);
            startActivity(intent);
         }
      });

      final Button pointCloudButton = (Button) findViewById(R.id.pointCloudButton);
      pointCloudButton.setOnClickListener(new OnClickListener() {
         @Override
         public void onClick(final View v) {
            final Intent intent = new Intent(getApplicationContext(), PointCloudActivity.class);
            startActivity(intent);
         }
      });


      final Button threeDModelButton = (Button) findViewById(R.id.threeDModelButton);
      threeDModelButton.setOnClickListener(new OnClickListener() {
         @Override
         public void onClick(final View v) {
            final Intent intent = new Intent(getApplicationContext(), ThreeDModelActivity.class);
            startActivity(intent);
         }
      });


   }
}
