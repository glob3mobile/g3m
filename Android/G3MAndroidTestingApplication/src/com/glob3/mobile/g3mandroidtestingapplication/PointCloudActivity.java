

package com.glob3.mobile.g3mandroidtestingapplication;

import org.glob3.mobile.generated.Angle;
import org.glob3.mobile.generated.BSONParser;
import org.glob3.mobile.generated.Color;
import org.glob3.mobile.generated.DirectMesh;
import org.glob3.mobile.generated.ElevationDataProvider;
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
import org.glob3.mobile.generated.ILogger;
import org.glob3.mobile.generated.IThreadUtils;
import org.glob3.mobile.generated.JSONArray;
import org.glob3.mobile.generated.JSONBaseObject;
import org.glob3.mobile.generated.JSONObject;
import org.glob3.mobile.generated.LayerSet;
import org.glob3.mobile.generated.MeshRenderer;
import org.glob3.mobile.generated.Planet;
import org.glob3.mobile.generated.Sector;
import org.glob3.mobile.generated.SingleBillElevationDataProvider;
import org.glob3.mobile.generated.TimeInterval;
import org.glob3.mobile.generated.URL;
import org.glob3.mobile.generated.Vector2I;
import org.glob3.mobile.specific.G3MBuilder_Android;
import org.glob3.mobile.specific.G3MWidget_Android;
import org.glob3.mobile.specific.JSONParser_Android;

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


         final JSONParser_Android parser = new JSONParser_Android();
         final JSONBaseObject objectBase = BSONParser.parse(_buffer);
         final JSONObject object = objectBase.asObject();
         final JSONArray pointsJson = object.getAsArray("points");

         final FloatBufferBuilderFromGeodetic vertices = FloatBufferBuilderFromGeodetic.builderWithFirstVertexAsCenter(_builder.getPlanet());

         final FloatBufferBuilderFromColor colors = new FloatBufferBuilderFromColor();


         final int size = pointsJson.size();
         ILogger.instance().logInfo("Painting:" + size + " points");


         for (int i = 0; i < size; i = i + 6) {
            final double latDegrees = pointsJson.getAsNumber(i + 1, 0);
            final double lonDegrees = pointsJson.getAsNumber(i, 0);
            final double height = pointsJson.get(i + 2).asNumber().value();
            final int red = (int) pointsJson.get(i + 3).asNumber().value();
            final int green = (int) pointsJson.get(i + 4).asNumber().value();
            final int blue = (int) pointsJson.get(i + 5).asNumber().value();

            vertices.add(Angle.fromDegrees(latDegrees), Angle.fromDegrees(lonDegrees), height - 150);

            //            final Color interpolatedColor = interpolateColor(fromColor, middleColor, toColor,
            //                     normalize((float) height, (float) minHeight, (float) (averageHeight * 1.5), 1, 0));
            colors.add(Color.fromRGBA255(red, green, blue, 1));
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

      _g3mWidget.setAnimatedCameraPosition(Geodetic3D.fromDegrees(46.3d, 7.16d, 15000), TimeInterval.fromSeconds(5));
   }


   @Override
   protected void onCreate(final Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);

      setContentView(R.layout.activity_main);

      final LayerSet layerSet = SimpleRasterLayerBuilder.createLayerset();
      layerSet.disableAllLayers();
      layerSet.getLayerByTitle("MapQuest Aerial").setEnable(true);

      _builder = new G3MBuilder_Android(this);
      //      _builder.setPlanet(Planet.createSphericalEarth());
      _builder.setPlanet(Planet.createEarth());
      _builder.getPlanetRendererBuilder().setLayerSet(layerSet);

      final ElevationDataProvider dem = new SingleBillElevationDataProvider(new URL("file:///full-earth-2048x1024.bil", false),
               Sector.fullSphere(), new Vector2I(2048, 1024), 0);
      _builder.getPlanetRendererBuilder().setElevationDataProvider(dem);
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
                     new URL("file:///points.bson", false), //
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


   @Override
   public void onBackPressed() {
      System.exit(0);
   }

}
