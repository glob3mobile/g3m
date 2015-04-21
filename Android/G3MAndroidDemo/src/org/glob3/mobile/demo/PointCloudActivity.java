

package org.glob3.mobile.demo;

import org.glob3.mobile.generated.Angle;
import org.glob3.mobile.generated.BSONParser;
import org.glob3.mobile.generated.Color;
import org.glob3.mobile.generated.DirectMesh;
import org.glob3.mobile.generated.FloatBufferBuilderFromColor;
import org.glob3.mobile.generated.FloatBufferBuilderFromGeodetic;
import org.glob3.mobile.generated.G3MContext;
import org.glob3.mobile.generated.GAsyncTask;
import org.glob3.mobile.generated.GInitializationTask;
import org.glob3.mobile.generated.GLPrimitive;
import org.glob3.mobile.generated.Geodetic3D;
import org.glob3.mobile.generated.IBufferDownloadListener;
import org.glob3.mobile.generated.IByteBuffer;
import org.glob3.mobile.generated.IDownloader;
import org.glob3.mobile.generated.IThreadUtils;
import org.glob3.mobile.generated.JSONArray;
import org.glob3.mobile.generated.JSONBaseObject;
import org.glob3.mobile.generated.JSONObject;
import org.glob3.mobile.generated.LayerSet;
import org.glob3.mobile.generated.MapBoxLayer;
import org.glob3.mobile.generated.MeshRenderer;
import org.glob3.mobile.generated.Planet;
import org.glob3.mobile.generated.TimeInterval;
import org.glob3.mobile.generated.URL;
import org.glob3.mobile.specific.G3MBuilder_Android;
import org.glob3.mobile.specific.G3MWidget_Android;

import android.app.Activity;
import android.os.Bundle;
import android.widget.RelativeLayout;


public class PointCloudActivity
         extends
            Activity {

   private final class PointsCloudParser
            extends
               GAsyncTask {

      private final IByteBuffer _buffer;
      private DirectMesh        _mesh;


      private PointsCloudParser(final IByteBuffer buffer) {
         _buffer = buffer;
      }


      @Override
      public void runInBackground(final G3MContext context) {
         final JSONBaseObject objectBase = BSONParser.parse(_buffer);
         final JSONObject object = objectBase.asObject();
         final JSONArray pointsJson = object.getAsArray("points");

         final FloatBufferBuilderFromGeodetic vertices = FloatBufferBuilderFromGeodetic.builderWithFirstVertexAsCenter(_builder.getPlanet());

         final FloatBufferBuilderFromColor colors = new FloatBufferBuilderFromColor();


         final int size = pointsJson.size();
         //         ILogger.instance().logInfo("Painting:" + size + " points");

         double minHeight = Double.MAX_VALUE;
         double maxHeight = Double.MIN_VALUE;
         double totalHeight = 0;
         for (int i = 0; i < size; i = i + 3) {
            final double height = pointsJson.get(i + 2).asNumber().value();
            totalHeight += height;
            if (height < minHeight) {
               minHeight = height;
            }
            if (height > maxHeight) {
               maxHeight = height;
            }
         }
         final double averageHeight = totalHeight / (size / 3.0);

         final Color fromColor = Color.red();
         final Color middleColor = Color.green();
         final Color toColor = Color.blue();

         for (int i = 0; i < size; i = i + 3) {
            final double latDegrees = pointsJson.getAsNumber(i + 1, 0);
            final double lonDegrees = pointsJson.getAsNumber(i, 0);
            final double height = pointsJson.get(i + 2).asNumber().value();

            vertices.add(Angle.fromDegrees(latDegrees), Angle.fromDegrees(lonDegrees), height - 150);

            final Color interpolatedColor = interpolateColor(fromColor, middleColor, toColor,
                     normalize((float) height, (float) minHeight, (float) (averageHeight * 1.5), 1, 0));
            colors.add(interpolatedColor);
         }

         final float lineWidth = 1;
         final float pointSize = 4F;
         final Color flatColor = null;
         _mesh = new DirectMesh( //
                  GLPrimitive.points(), //
                  true, //
                  vertices.getCenter(), //
                  vertices.create(), //
                  lineWidth, //
                  pointSize, //
                  flatColor, //
                  colors.create(), //
                  1, //
                  false);
      }


      @Override
      public void onPostExecute(final G3MContext context) {
         if (_mesh != null) {
            setPointsCloudMesh(_mesh);
            _mesh = null;
         }
      }
   }

   private final class PointsCloudDownloader
            extends
               IBufferDownloadListener {

      private final IThreadUtils _threadUtils;


      public PointsCloudDownloader(final IThreadUtils threadUtils) {
         _threadUtils = threadUtils;
      }


      @Override
      public void onDownload(final URL url,
                             final IByteBuffer buffer,
                             final boolean expired) {
         _threadUtils.invokeAsyncTask(new PointsCloudParser(buffer), true);
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
   }


   private G3MWidget_Android  _g3mWidget;
   private RelativeLayout     _placeHolder;
   private G3MBuilder_Android _builder;
   private final MeshRenderer _meshRenderer        = new MeshRenderer();
   private boolean            _isPointsCloudLoader = false;


   private void setPointsCloudMesh(final DirectMesh mesh) {
      _meshRenderer.addMesh(mesh);
      _isPointsCloudLoader = true;

      _g3mWidget.setAnimatedCameraPosition(Geodetic3D.fromDegrees(39.12787093899339d, -77.59965772558118d, 5000),
               TimeInterval.fromSeconds(5));
   }


   @Override
   protected void onCreate(final Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);

      setContentView(R.layout.activity_point_cloud);

      //      final LayerSet layerSet = SimpleRasterLayerBuilder.createLayerset();
      //      layerSet.disableAllLayers();
      //      layerSet.getLayerByTitle("Map Box Aerial").setEnable(true);

      final LayerSet layerSet = new LayerSet();
      layerSet.addLayer(new MapBoxLayer("examples.map-m0t0lrpu", TimeInterval.fromDays(30), true, 2));


      _builder = new G3MBuilder_Android(this);
      //      _builder.setPlanet(Planet.createSphericalEarth());
      _builder.setPlanet(Planet.createEarth());
      _builder.getPlanetRendererBuilder().setLayerSet(layerSet);

      _builder.setInitializationTask(pointCloudInitializationTask());
      _builder.addRenderer(_meshRenderer);
      //rgb(175,221,233)
      _builder.setBackgroundColor(Color.fromRGBA255(175, 221, 233, 255));

      _g3mWidget = _builder.createWidget();


      _placeHolder = (RelativeLayout) findViewById(R.id.g3mWidgetHolder);
      _placeHolder.addView(_g3mWidget);
   }


   private GInitializationTask pointCloudInitializationTask() {


      final GInitializationTask initTask = new GInitializationTask() {
         @Override
         public void run(final G3MContext context) {

            final IDownloader downloader = context.getDownloader();

            final IBufferDownloadListener listener = new PointsCloudDownloader(context.getThreadUtils());

            downloader.requestBuffer( //
                     new URL("file:///18STJ7435_2000_4326.bson", false), //
                     0, //
                     TimeInterval.forever(), //
                     true, //
                     listener, //
                     false);
         }


         @Override
         public boolean isDone(final G3MContext context) {
            return _isPointsCloudLoader;
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


   @Override
   public void onBackPressed() {
      System.exit(0);
   }

}
