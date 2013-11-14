

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
import android.widget.RelativeLayout;


public class ThreeDModelActivity
         extends
            Activity {

   private G3MBuilder_Android _builder;
   private G3MWidget_Android  _g3mWidget;
   private RelativeLayout     _placeHolder;


   @Override
   protected void onCreate(final Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_three_dmodel);

      final LayerSet layerSet = SimpleRasterLayerBuilder.createLayerset();
      layerSet.disableAllLayers();
      layerSet.getLayerByTitle("Map Box Aerial").setEnable(true);

      _builder = new G3MBuilder_Android(this);
      _builder.setPlanet(Planet.createSphericalEarth());
      _builder.getPlanetRendererBuilder().setLayerSet(layerSet);
      _builder.setBackgroundColor(Color.fromRGBA255(175, 221, 233, 255));

      final ShapesRenderer planeShapeRenderer = new ShapesRenderer();
      planeShapeRenderer.loadBSONSceneJS(new URL("file:///A320.bson", false), "file:///textures-A320/", false, new Geodetic3D(
               Angle.fromDegreesMinutesSeconds(38, 53, 42.24), Angle.fromDegreesMinutesSeconds(-77, 2, 10.92), 10000),
               AltitudeMode.ABSOLUTE, new ShapeLoadListener() {

                  @Override
                  public void onBeforeAddShape(final SGShape shape) {
                     shape.setScale(200);
                     shape.setPitch(Angle.fromDegrees(90));
                  }


                  @Override
                  public void onAfterAddShape(final SGShape shape) {

                     shape.setAnimatedPosition(
                              TimeInterval.fromSeconds(26),
                              new Geodetic3D(Angle.fromDegreesMinutesSeconds(38, 53, 42.24), Angle.fromDegreesMinutesSeconds(-78,
                                       2, 10.92), 10000), true);

                     final double fromDistance = 75000;
                     final double toDistance = 18750;

                     final Angle fromAzimuth = Angle.fromDegrees(-90);
                     final Angle toAzimuth = Angle.fromDegrees(270);

                     final Angle fromAltitude = Angle.fromDegrees(90);
                     final Angle toAltitude = Angle.fromDegrees(15);

                     shape.orbitCamera(TimeInterval.fromSeconds(20), fromDistance, toDistance, fromAzimuth, toAzimuth,
                              fromAltitude, toAltitude);


                  }


                  @Override
                  public void dispose() {
                     // TODO Auto-generated method stub

                  }
               });

      _builder.addRenderer(planeShapeRenderer);

      _g3mWidget = _builder.createWidget();


      _placeHolder = (RelativeLayout) findViewById(R.id.g3mWidgetHolder);
      _placeHolder.addView(_g3mWidget);
   }


   @Override
   public void onBackPressed() {
      System.exit(0);
   }

}
