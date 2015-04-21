

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
import org.glob3.mobile.generated.Geodetic2D;
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
import org.glob3.mobile.generated.MapBoxLayer;
import org.glob3.mobile.generated.MeshRenderer;
import org.glob3.mobile.generated.Planet;
import org.glob3.mobile.generated.Quality;
import org.glob3.mobile.generated.Sector;
import org.glob3.mobile.generated.SingleBilElevationDataProvider;
import org.glob3.mobile.generated.TimeInterval;
import org.glob3.mobile.generated.URL;
import org.glob3.mobile.generated.Vector2I;
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


         //final JSONParser_Android parser = new JSONParser_Android();
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


            //     ILogger.instance().logInfo(latDegrees + "," + lonDegrees);

            vertices.add(Angle.fromDegrees(latDegrees), Angle.fromDegrees(lonDegrees), height - 1150);

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


   }


   @Override
   protected void onCreate(final Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);

      setContentView(R.layout.activity_main);
      /*MDT
            FILENAME=\\psf\Home\Desktop\SUIZA\mdt_town_300.bil
                     DESCRIPTION=mdt_town_300.bil
                     UPPER LEFT X=7.1475267929
                     UPPER LEFT Y=46.3566926359
                     LOWER RIGHT X=7.1737468920
                     LOWER RIGHT Y=46.3427207284
      */

      //      final Geodetic2D lower = new Geodetic2D( //
      //               Angle.fromDegrees(46.3427207284), //
      //               Angle.fromDegrees(7.1475267929));
      //      final Geodetic2D upper = new Geodetic2D( //
      //               Angle.fromDegrees(46.3566926359), //
      //               Angle.fromDegrees(7.1737468920));
      //
      //      final Sector demSector = new Sector(lower, upper);

      /**
       * FILENAME=\\psf\Home\Downloads\Archive\matterhorn.asc DESCRIPTION=matterhorn.asc UPPER LEFT X=7.6258137669 UPPER LEFT
       * Y=46.0072241194 LOWER RIGHT X=7.7002694041 LOWER RIGHT Y=45.9556662899 NUM COLUMNS=1784 NUM ROWS=1236 MIN
       * ELEVATION=2167.013 meters MAX ELEVATION=4535.022 meters
       */


      final Geodetic2D lower = new Geodetic2D( //
               Angle.fromDegrees(45.9556662899), //
               Angle.fromDegrees(7.6258137669));
      final Geodetic2D upper = new Geodetic2D( //
               Angle.fromDegrees(46.0072241194), //
               Angle.fromDegrees(7.7002694041));

      final Sector demSector = new Sector(lower, upper);


      //      final LayerSet layerSet = SimpleRasterLayerBuilder.createLayerset();
      //      layerSet.disableAllLayers();
      //      layerSet.getLayerByTitle("MapQuest Aerial").setEnable(true);

      final LayerSet layerSet = new LayerSet();

      //      final MapBoxLayer mboxTerrainLayer = new MapBoxLayer("examples.map-qogxobv1", TimeInterval.fromDays(30), true, 2);
      //      layerSet.addLayer(mboxTerrainLayer);
      //      final MapBoxLayer mboxTownLidar = new MapBoxLayer("bobbysud.town-lidar", TimeInterval.fromDays(30), true, 10);
      //      mboxTownLidar.setEnable(true);
      //      layerSet.addLayer(mboxTownLidar);

      final MapBoxLayer mboxMatterhornLidar = new MapBoxLayer("bobbysud.matterhorn-imagery", TimeInterval.fromDays(30), true, 10);
      mboxMatterhornLidar.setEnable(true);
      layerSet.addLayer(mboxMatterhornLidar);


      _builder = new G3MBuilder_Android(this);
      //      _builder.setPlanet(Planet.createSphericalEarth());
      _builder.setPlanet(Planet.createEarth());
      _builder.getPlanetRendererBuilder().setLayerSet(layerSet);


      //  NUM COLUMNS=1872
      //         NUM ROWS=998
      //      final ElevationDataProvider dem = new SingleBillElevationDataProvider(new URL("file:///mdt_town_300_300.bil", false),
      //               demSector, new Vector2I(624, 333), -1200);
      //      _builder.getPlanetRendererBuilder().setElevationDataProvider(dem);


      //      NUM COLUMNS=1784
      //               NUM ROWS=1236
      final ElevationDataProvider dem = new SingleBilElevationDataProvider(new URL("file:///matterhorn_300.bil", false),
               demSector, new Vector2I(1784, 1236), -2160);
      _builder.getPlanetRendererBuilder().setElevationDataProvider(dem);
      _builder.getPlanetRendererBuilder().setQuality(Quality.QUALITY_HIGH);

      //      
      //      final ElevationDataProvider dem = new SingleBillElevationDataProvider(new URL("file:///full-earth-2048x1024.bil", false),
      //               Sector.fullSphere(), new Vector2I(2048, 1024), -1700);
      //      _builder.getPlanetRendererBuilder().setElevationDataProvider(dem);

      _meshRenderer.loadJSONPointCloud(new URL("file:///matterhorn.json"), 2);
      //  _builder.setInitializationTask(pointCloudInitializationTask());
      _builder.setShownSector(demSector);
      // _builder.addRenderer(_meshRenderer);
      //rgb(175,221,233)
      _builder.setBackgroundColor(Color.fromRGBA255(0, 0, 50, 200));

      _g3mWidget = _builder.createWidget();
      _g3mWidget.setAnimatedCameraPosition(new Geodetic3D(demSector.getCenter(), 3000), TimeInterval.fromSeconds(5));

      _placeHolder = (RelativeLayout) findViewById(R.id.g3mWidgetHolder);
      _placeHolder.addView(_g3mWidget);
   }


   @SuppressWarnings("unused")
   private GInitializationTask pointCloudInitializationTask() {


      final GInitializationTask initTask = new GInitializationTask() {
         @Override
         public void run(final G3MContext context) {

            final IDownloader downloader = context.getDownloader();

            final IBufferDownloadListener listener = new PointsCloudDownloader(context.getThreadUtils());

            downloader.requestBuffer( //
                     new URL("file:///points_10.bson", false), //
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
