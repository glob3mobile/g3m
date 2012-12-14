

package org.glob3.mobile.demo;

import java.util.ArrayList;

import org.glob3.mobile.generated.BusyMeshRenderer;
import org.glob3.mobile.generated.CachedDownloader;
import org.glob3.mobile.generated.CameraDoubleDragHandler;
import org.glob3.mobile.generated.CameraDoubleTapHandler;
import org.glob3.mobile.generated.CameraRenderer;
import org.glob3.mobile.generated.CameraRotationHandler;
import org.glob3.mobile.generated.CameraSingleDragHandler;
import org.glob3.mobile.generated.Color;
import org.glob3.mobile.generated.CompositeRenderer;
import org.glob3.mobile.generated.GInitializationTask;
import org.glob3.mobile.generated.ICameraConstrainer;
import org.glob3.mobile.generated.IDownloader;
import org.glob3.mobile.generated.IStorage;
import org.glob3.mobile.generated.IThreadUtils;
import org.glob3.mobile.generated.LayerSet;
import org.glob3.mobile.generated.PeriodicalTask;
import org.glob3.mobile.generated.Planet;
import org.glob3.mobile.generated.Sector;
import org.glob3.mobile.generated.SimpleCameraConstrainer;
import org.glob3.mobile.generated.TileRenderer;
import org.glob3.mobile.generated.TileRendererBuilder;
import org.glob3.mobile.generated.URL;
import org.glob3.mobile.generated.UserData;
import org.glob3.mobile.generated.WMSLayer;
import org.glob3.mobile.generated.WMSServerVersion;
import org.glob3.mobile.specific.Downloader_Android;
import org.glob3.mobile.specific.G3MBaseActivity;
import org.glob3.mobile.specific.G3MWidget_Android;
import org.glob3.mobile.specific.SQLiteStorage_Android;
import org.glob3.mobile.specific.ThreadUtils_Android;

import android.os.Bundle;


public class G3MSimplestGlob3Activity
         extends
            G3MBaseActivity {

   private G3MWidget_Android _widgetAndroid = null;


   @Override
   public void onCreate(final Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);

      // initialize a default widget by using a builder
      //      final G3MBuilder_Android g3mBuilder = new G3MBuilder_Android(this);
      //
      //      _widgetAndroid = g3mBuilder.createWidget();
      //      setContentView(_widgetAndroid);


      // initialize a customized widget without using any builder
      _widgetAndroid = new G3MWidget_Android(this);

      final IStorage storage = new SQLiteStorage_Android("g3m.cache", this);

      final int connectTimeout = 20000;
      final int readTimeout = 30000;
      final boolean saveInBackground = true;
      final IDownloader downloader = new CachedDownloader( //
               new Downloader_Android(8, connectTimeout, readTimeout, this), //
               storage, //
               saveInBackground);

      final IThreadUtils threadUtils = new ThreadUtils_Android(_widgetAndroid);

      final Planet planet = Planet.createEarth();

      final ArrayList<ICameraConstrainer> cameraConstraints = new ArrayList<ICameraConstrainer>();
      cameraConstraints.add(new SimpleCameraConstrainer());

      final CameraRenderer cameraRenderer = new CameraRenderer();
      final boolean useInertia = true;
      cameraRenderer.addHandler(new CameraSingleDragHandler(useInertia));
      final boolean processRotation = true;
      final boolean processZoom = true;
      cameraRenderer.addHandler(new CameraDoubleDragHandler(processRotation, processZoom));
      cameraRenderer.addHandler(new CameraRotationHandler());
      cameraRenderer.addHandler(new CameraDoubleTapHandler());

      final CompositeRenderer mainRenderer = new CompositeRenderer();
      final LayerSet layerSet = new LayerSet();
      final WMSLayer osm = new WMSLayer( //
               "osm_auto:all", //
               new URL("http://129.206.228.72/cached/osm", false), //
               WMSServerVersion.WMS_1_1_0, //
               Sector.fromDegrees(-85.05, -180.0, 85.05, 180.0), //
               "image/jpeg", //
               "EPSG:4326", //
               "", //
               false, //
               null);
      layerSet.addLayer(osm);
      final TileRendererBuilder tlBuilder = new TileRendererBuilder();
      tlBuilder.setLayerSet(layerSet);
      final TileRenderer tileRenderer = tlBuilder.create();
      mainRenderer.addRenderer(tileRenderer);

      final org.glob3.mobile.generated.Renderer busyRenderer = new BusyMeshRenderer();

      final Color backgroundColor = Color.fromRGBA(0, (float) 0.1, (float) 0.2, 1);

      final boolean logFPS = true;

      final boolean logDownloaderStatistics = false;

      final GInitializationTask initializationTask = null;

      final boolean autoDeleteInitializationTask = true;

      final ArrayList<PeriodicalTask> periodicalTasks = new ArrayList<PeriodicalTask>();

      final UserData userData = null;

      _widgetAndroid.initWidget(//
               storage, // 
               downloader, //
               threadUtils, //
               planet, //
               cameraConstraints, //
               cameraRenderer, //
               mainRenderer, //
               busyRenderer, //
               backgroundColor, //
               logFPS, //
               logDownloaderStatistics, //
               initializationTask, //
               autoDeleteInitializationTask, //
               periodicalTasks, //
               userData);

      setContentView(_widgetAndroid);


      //      final G3MBuilder glob3Builder = new G3MBuilder();
      //      _widgetAndroid = glob3Builder.getSimpleBingGlob3(getApplicationContext());
      //      setContentView(_widgetAndroid);
   }


   @Override
   protected G3MWidget_Android getWidgetAndroid() {
      return _widgetAndroid;
   }
}
