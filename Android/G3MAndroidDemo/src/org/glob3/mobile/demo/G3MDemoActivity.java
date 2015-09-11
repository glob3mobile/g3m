

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
      finish();
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


      final Button drawingShapesG3MButton = (Button) findViewById(R.id.symbology);
      drawingShapesG3MButton.setOnClickListener(new OnClickListener() {
         @Override
         public void onClick(final View v) {
            final Intent intent = new Intent(getApplicationContext(), SymbologyActivity.class);
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

      final Button threeDSymbology = (Button) findViewById(R.id.threeDsymbologyButton);
      threeDSymbology.setOnClickListener(new OnClickListener() {
         @Override
         public void onClick(final View v) {
            final Intent intent = new Intent(getApplicationContext(), ShapeSymbolizerActivity.class);
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

      final Button flatWorldButton = (Button) findViewById(R.id.flatWorldButton);
      flatWorldButton.setOnClickListener(new OnClickListener() {
         @Override
         public void onClick(final View v) {
            final Intent intent = new Intent(getApplicationContext(), FlatWorldActivity.class);
            startActivity(intent);
         }
      });

      final Button cameraAnimationButton = (Button) findViewById(R.id.cameraAnimation);
      cameraAnimationButton.setOnClickListener(new OnClickListener() {
         @Override
         public void onClick(final View v) {
            final Intent intent = new Intent(getApplicationContext(), CameraAnimation.class);
            startActivity(intent);
         }
      });

      final Button vectorTilesButton = (Button) findViewById(R.id.vectorialTilesDemoButton);
      vectorTilesButton.setOnClickListener(new OnClickListener() {
         @Override
         public void onClick(final View v) {
            final Intent intent = new Intent(getApplicationContext(), VectorTilesActivity.class);
            startActivity(intent);
         }
      });

      final Button vectorStreainghButton = (Button) findViewById(R.id.vectorialStreamingDemoButton);
      vectorStreainghButton.setOnClickListener(new OnClickListener() {
         @Override
         public void onClick(final View v) {
            final Intent intent = new Intent(getApplicationContext(), VectorStreamingActivity.class);
            startActivity(intent);
         }
      });

      final Button pointCloudStreaming = (Button) findViewById(R.id.pointCloudStreamingButton);
      pointCloudStreaming.setOnClickListener(new OnClickListener() {
         @Override
         public void onClick(final View v) {
            final Intent intent = new Intent(getApplicationContext(), PointCloudStreamimgActivity.class);
            startActivity(intent);
         }
      });


   }
}
