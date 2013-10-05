

package org.glob3.mobile.demo;

import org.glob3.mobile.generated.BSONParser;
import org.glob3.mobile.generated.Color;
import org.glob3.mobile.generated.DirectMesh;
import org.glob3.mobile.generated.FloatBufferBuilderFromColor;
import org.glob3.mobile.generated.FloatBufferBuilderFromGeodetic;
import org.glob3.mobile.generated.G3MContext;
import org.glob3.mobile.generated.GInitializationTask;
import org.glob3.mobile.generated.GLPrimitive;
import org.glob3.mobile.generated.Geodetic3D;
import org.glob3.mobile.generated.IBufferDownloadListener;
import org.glob3.mobile.generated.IByteBuffer;
import org.glob3.mobile.generated.IDownloader;
import org.glob3.mobile.generated.ILogger;
import org.glob3.mobile.generated.JSONArray;
import org.glob3.mobile.generated.JSONBaseObject;
import org.glob3.mobile.generated.JSONObject;
import org.glob3.mobile.generated.LayerSet;
import org.glob3.mobile.generated.MeshRenderer;
import org.glob3.mobile.generated.Planet;
import org.glob3.mobile.generated.TimeInterval;
import org.glob3.mobile.generated.URL;
import org.glob3.mobile.specific.G3MBuilder_Android;
import org.glob3.mobile.specific.G3MWidget_Android;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.RelativeLayout;


public class PointCloudActivity
         extends
            Activity {

   private G3MWidget_Android  _g3mWidget;
   private RelativeLayout     _placeHolder;
   private G3MBuilder_Android _builder;
   final MeshRenderer         _meshRenderer = new MeshRenderer();


   @Override
   protected void onCreate(final Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_point_cloud);

      /*
      40.342244323527
      -5.86641693954211
      40.1540848526184
      -5.51167850675718
      */

      final LayerSet layerSet = SimpleRasterLayerBuilder.createLayerset();
      layerSet.disableAllLayers();
      layerSet.getLayerByTitle("Map Box Aerial").setEnable(true);

      _builder = new G3MBuilder_Android(this);
      //      _builder.setPlanet(Planet.createSphericalEarth());
      _builder.setPlanet(Planet.createEarth());
      _builder.getPlanetRendererBuilder().setLayerSet(layerSet);


      _builder.setInitializationTask(pointCloudInitializationTask());
      _builder.addRenderer(_meshRenderer);

      _g3mWidget = _builder.createWidget();

      _placeHolder = (RelativeLayout) findViewById(R.id.g3mWidgetHolder);
      _placeHolder.addView(_g3mWidget);

   }


   private GInitializationTask pointCloudInitializationTask() {


      final GInitializationTask initTask = new GInitializationTask() {

         @Override
         public void run(final G3MContext context) {

            final IDownloader downloader = context.getDownloader();

            final IBufferDownloadListener listener = new IBufferDownloadListener() {

               private JSONArray _pointsJson;


               @Override
               public void onDownload(final URL url,
                                      final IByteBuffer buffer,
                                      final boolean expired) {

                  ILogger.instance().logInfo("Before parsing");

                  final JSONBaseObject objectBase = BSONParser.parse(buffer);
                  final JSONObject object = objectBase.asObject();
                  _pointsJson = object.getAsArray("points");

                  final FloatBufferBuilderFromGeodetic vertices = FloatBufferBuilderFromGeodetic.builderWithFirstVertexAsCenter(_builder.getPlanet());

                  final FloatBufferBuilderFromColor colors = new FloatBufferBuilderFromColor();


                  final int size = _pointsJson.size();
                  ILogger.instance().logInfo("Painting:" + size + " points");


                  //                  ILogger.instance().logInfo("Painting:" + _pointsJson.size() + " points");

                  double minHeight = Double.MAX_VALUE;
                  double maxHeight = Double.MIN_VALUE;
                  double totalHeight = 0;
                  for (int i = 0; i < size; i = i + 3) {
                     final double height = _pointsJson.get(i + 2).asNumber().value();
                     totalHeight += height;
                     if (height < minHeight) {
                        minHeight = height;
                     }
                     if (height > maxHeight) {
                        maxHeight = height;
                     }
                  }
                  final double averageHeight = totalHeight / (size / 3.0);

                  for (int i = 0; i < size; i = i + 3) {
                     final double latDegrees = _pointsJson.getAsNumber(i + 1, 0);
                     final double lotDegrees = _pointsJson.getAsNumber(i, 0);
                     final double height = _pointsJson.get(i + 2).asNumber().value();

                     final Geodetic3D position = Geodetic3D.fromDegrees(latDegrees, lotDegrees, height);

                     //ILogger.instance().logInfo("Point::" + position.description());

                     vertices.add(position);


                     //        final Color interpolatedColor = fromColor.mixedWith(toColor, normalize((float) height, 744, 775, 1, 0));

                     final Color interpolatedColor = interpolateColor(Color.red(), Color.green(), Color.blue(),
                              normalize((float) height, (float) minHeight, (float) (averageHeight * 2), 1, 0));

                     colors.add(interpolatedColor);


                  }

                  final float lineWidth = 1;
                  final Color flatColor = null;

                  _meshRenderer.addMesh(new DirectMesh(GLPrimitive.points(), true, vertices.getCenter(), vertices.create(),
                           lineWidth, 4F, flatColor, colors.create(), 1, false));

               }


               @Override
               public void onError(final URL url) {
               }


               @Override
               public void onCancel(final URL url) {
               }


               @Override
               public void onCanceledDownload(final URL url,
                                              final IByteBuffer buffer,
                                              final boolean expired) {
               }

            };

            downloader.requestBuffer( //-
                     new URL("file:///18STJ7435_2000_4326.bson", false), //
                     0, //
                     TimeInterval.forever(), //
                     true, //
                     listener, //
                     false);

         }


         @Override
         public boolean isDone(final G3MContext context) {

            return true;
         }
      };


      return initTask;
   }


   private float normalize(final float value,
                           final float max,
                           final float min,
                           final float new_max,
                           final float new_min) {
      return (((value - min) / (max - min)) * (new_max - new_min)) + new_min;
   }


   private static Color interpolateColor(final Color from,
                                         final Color middle,
                                         final Color to,
                                         final float d) {
      if (d <= 0) {
         return from;
      }
      if (d >= 1) {
         return to;
      }
      if (d <= 0.5) {
         return from.mixedWith(middle, d * 2);
      }
      return middle.mixedWith(to, (d - 0.5f) * 2);
   }


   /* 
    private void loadPointsDownloader(final Planet planet1,
                                      final G3MContext context) {
       final IDownloader downloader = context.getDownloader();

       final IBufferDownloadListener listener = new IBufferDownloadListener() {


          @Override
          public void onDownload(final URL url,
                                 final IByteBuffer buffer,
                                 final boolean expired) {


             ILogger.instance().logInfo("Before parsing");

             final JSONBaseObject objectBase = BSONParser.parse(buffer);
             final JSONObject object = objectBase.asObject();
             _pointsJson = object.getAsArray("points");

             final FloatBufferBuilderFromGeodetic vertices = FloatBufferBuilderFromGeodetic.builderWithFirstVertexAsCenter(planet1);

             final FloatBufferBuilderFromColor colors = new FloatBufferBuilderFromColor();


             ILogger.instance().logInfo("Painting:" + _pointsJson.size() + " points");

             for (int i = 0; i < _pointsJson.size(); i = i + 3) {

                final double height = _pointsJson.get(i + 2).asNumber().value();
                vertices.add(new Geodetic3D(Angle.fromDegrees(_pointsJson.getAsNumber(i + 1, 0)),
                         Angle.fromDegrees(_pointsJson.getAsNumber(i, 0)), ((height + DELTA_HEIGHT) * _VerticalExaggeration)));


                //        final Color interpolatedColor = fromColor.mixedWith(toColor, normalize((float) height, 744, 775, 1, 0));

                final Color interpolatedColor = interpolateColor(Color.red(), Color.green(), Color.blue(),
                         normalize((float) height, 744, 775, 1, 0));

                colors.add(interpolatedColor);


             }

             final float lineWidth = 1;
             final Color flatColor = null;

             _meshRenderer.addMesh(new DirectMesh(GLPrimitive.points(), true, vertices.getCenter(), vertices.create(),
                      lineWidth, 4F, flatColor, colors.create(), 1, false));


          }


          @Override
          public void onError(final URL url) {
             // TODO Auto-generated method stub

          }


          @Override
          public void onCancel(final URL url) {
             // TODO Auto-generated method stub

          }


          @Override
          public void onCanceledDownload(final URL url,
                                         final IByteBuffer buffer,
                                         final boolean expired) {
             // TODO Auto-generated method stub

          }

       };


       downloader.requestBuffer( //-
                new URL("file:///338000_8092000_1K_class4326.bson", false), //
                0, //
                TimeInterval.forever(), //
                true, //
                listener, //
                false);
    }
   */

   @Override
   public boolean onCreateOptionsMenu(final Menu menu) {
      // Inflate the menu; this adds items to the action bar if it is present.
      getMenuInflater().inflate(R.menu.point_cloud, menu);
      return true;
   }

}
