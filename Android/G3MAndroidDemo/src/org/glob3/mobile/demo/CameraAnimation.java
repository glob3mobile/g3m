

package org.glob3.mobile.demo;

import org.glob3.mobile.generated.AltitudeMode;
import org.glob3.mobile.generated.Angle;
import org.glob3.mobile.generated.Color;
import org.glob3.mobile.generated.Geodetic3D;
import org.glob3.mobile.generated.LayerSet;
import org.glob3.mobile.generated.Planet;
import org.glob3.mobile.generated.SGShape;
import org.glob3.mobile.generated.ShapeLoadListener;
import org.glob3.mobile.generated.ShapesRenderer;
import org.glob3.mobile.generated.TimeInterval;
import org.glob3.mobile.generated.URL;
import org.glob3.mobile.specific.G3MBuilder_Android;
import org.glob3.mobile.specific.G3MWidget_Android;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;


public class CameraAnimation
         extends
            Activity {

   private G3MBuilder_Android _builder;
   private G3MWidget_Android  _g3mWidget;
   private RelativeLayout     _placeHolder;
   private SGShape            _sphynxShape;
   private SGShape            _eiffelShape;
   private SGShape            _arcShape;


   @Override
   protected void onCreate(final Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_camera_animation);

      final LayerSet layerSet = SimpleRasterLayerBuilder.createLayerset();
      layerSet.disableAllLayers();
      layerSet.getLayerByTitle("Map Box Aerial").setEnable(true);

      _builder = new G3MBuilder_Android(this);
      _builder.setPlanet(Planet.createSphericalEarth());
      _builder.getPlanetRendererBuilder().setLayerSet(layerSet);
      _builder.setBackgroundColor(Color.fromRGBA255(175, 221, 233, 255));

      final ShapesRenderer sphinxRenderer = new ShapesRenderer();
      final Angle latitude = Angle.fromDegreesMinutesSeconds(29, 58, 30.99);
      final Angle longitude = Angle.fromDegreesMinutesSeconds(31, 8, 15.84);
      final ShapeLoadListener sphynxListener = new ShapeLoadListener() {
         @Override
         public void onBeforeAddShape(final SGShape shape) {
            shape.setPitch(Angle.fromDegrees(90));
         }


         @Override
         public void onAfterAddShape(final SGShape shape) {
            _sphynxShape = shape;
         }


         @Override
         public void dispose() {
         }
      };

      sphinxRenderer.loadBSONSceneJS(new URL("file:///sphinx.bson", false), "file:///images/", false, new Geodetic3D(latitude,
               longitude, 0), AltitudeMode.ABSOLUTE, sphynxListener);


      final ShapesRenderer eifelTowerRenderer = new ShapesRenderer();
      final Angle latitudeP = Angle.fromDegreesMinutesSeconds(48, 51, 29.06);
      final Angle longitudeP = Angle.fromDegreesMinutesSeconds(2, 17, 40.48);


      final ShapeLoadListener eiffelListener = new ShapeLoadListener() {
         @Override
         public void onBeforeAddShape(final SGShape shape) {
            shape.setScale(0.02);
         }


         @Override
         public void onAfterAddShape(final SGShape shape) {
            _eiffelShape = shape;
         }


         @Override
         public void dispose() {
         }
      };


      eifelTowerRenderer.loadBSONSceneJS( //
               new URL("file:///eifeltower.bson", false), //
               "file:///images/eifel/", //
               true, // isTransparent
               new Geodetic3D(latitudeP, longitudeP, 0), //
               AltitudeMode.ABSOLUTE, eiffelListener);


      final ShapesRenderer arcRenderer = new ShapesRenderer();
      final Angle latitudeA = Angle.fromDegreesMinutesSeconds(48, 52, 25.58);
      final Angle longitudeA = Angle.fromDegreesMinutesSeconds(2, 17, 42.12);


      final ShapeLoadListener arcListener = new ShapeLoadListener() {
         @Override
         public void onBeforeAddShape(final SGShape shape) {
            shape.setScale(0.5);
            shape.setPitch(Angle.fromDegrees(90));
         }


         @Override
         public void onAfterAddShape(final SGShape shape) {
            _arcShape = shape;
         }


         @Override
         public void dispose() {
         }
      };


      arcRenderer.loadBSONSceneJS(new URL("file:///arcdeTriomphe.bson", false), "file:///images/arc/", false, new Geodetic3D(
               latitudeA, longitudeA, 0), AltitudeMode.ABSOLUTE, arcListener);


      final Button sphynxButton = (Button) findViewById(R.id.sphinx);

      sphynxButton.setOnClickListener(new OnClickListener() {

         @Override
         public void onClick(final View v) {

            final double fromDistance = 6000;
            final double toDistance = 2000;

            final Angle fromAzimuth = Angle.fromDegrees(-90);
            final Angle toAzimuth = Angle.fromDegrees(45);

            final Angle fromAltitude = Angle.fromDegrees(90);
            final Angle toAltitude = Angle.fromDegrees(30);

            _sphynxShape.orbitCamera(TimeInterval.fromSeconds(20), fromDistance, toDistance, fromAzimuth, toAzimuth,
                     fromAltitude, toAltitude);

         }
      });

      final Button teButton = (Button) findViewById(R.id.eiffel);

      teButton.setOnClickListener(new OnClickListener() {

         @Override
         public void onClick(final View v) {

            final double fromDistance = 10000;
            final double toDistance = 1000;

            final Angle fromAzimuth = Angle.fromDegrees(-90);
            final Angle toAzimuth = Angle.fromDegrees(270);

            final Angle fromAltitude = Angle.fromDegrees(90);
            final Angle toAltitude = Angle.fromDegrees(15);

            _eiffelShape.orbitCamera(TimeInterval.fromSeconds(20), fromDistance, toDistance, fromAzimuth, toAzimuth,
                     fromAltitude, toAltitude);


         }
      });

      final Button atButton = (Button) findViewById(R.id.triomphe);

      atButton.setOnClickListener(new OnClickListener() {

         @Override
         public void onClick(final View v) {

            //  _g3mWidget.setAnimatedCameraPosition(new Geodetic3D(latitudeA, longitudeA, 1000));

            final double fromDistance = 10000;
            final double toDistance = 1000;

            final Angle fromAzimuth = Angle.fromDegrees(-90);
            final Angle toAzimuth = Angle.fromDegrees(270);

            final Angle fromAltitude = Angle.fromDegrees(90);
            final Angle toAltitude = Angle.fromDegrees(15);

            _arcShape.orbitCamera(TimeInterval.fromSeconds(20), fromDistance, toDistance, fromAzimuth, toAzimuth, fromAltitude,
                     toAltitude);

         }
      });


      _builder.addRenderer(sphinxRenderer);
      _builder.addRenderer(eifelTowerRenderer);
      _builder.addRenderer(arcRenderer);


      _g3mWidget = _builder.createWidget();

      _placeHolder = (RelativeLayout) findViewById(R.id.g3mWidgetHolder);
      _placeHolder.addView(_g3mWidget);

   }


   @Override
   public void onBackPressed() {
      System.exit(0);
   }


}
