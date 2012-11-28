

package org.glob3.mobile.demo;

import java.util.ArrayList;

import org.glob3.mobile.generated.GTask;
import org.glob3.mobile.generated.ICameraConstrainer;
import org.glob3.mobile.generated.LayerSet;
import org.glob3.mobile.generated.PeriodicalTask;
import org.glob3.mobile.generated.Renderer;
import org.glob3.mobile.generated.Sector;
import org.glob3.mobile.generated.SimpleCameraConstrainer;
import org.glob3.mobile.generated.URL;
import org.glob3.mobile.generated.UserData;
import org.glob3.mobile.generated.WMSLayer;
import org.glob3.mobile.generated.WMSServerVersion;
import org.glob3.mobile.specific.G3MWidget_Android;

import android.content.Context;


public class G3MBuilder {


   public G3MBuilder() {
      super();
      // TODO Auto-generated constructor stub
   }

   //List of cosntrainers of camera, Object that evit that the camera goes to any place. You can add all the constraints that you need
   ArrayList<ICameraConstrainer> cameraConstraints;
   // Simplest implementation of  <ICameraConstrainer>, for example, evits that the earth goes so little
   SimpleCameraConstrainer       scc;
   //You can storage any object in the glob3, for example a data model or wathever thing
   UserData                      userData;
   //Gtask (like java runnable) is a task that always is done before launch the globe (start downloader, move the camera, etc....)
   GTask                         initializationTask;
   //Task that are executed every periodical time. For example, launch a downloader every period
   ArrayList<PeriodicalTask>     periodicalTasks;
   //false-> Always try to download first the last level , true -> download all the levels secuencially
   boolean                       incrementalTileQuality;


   //Layers Defined
   final WMSLayer                bingLayer = new WMSLayer("ve", new URL("http://worldwind27.arc.nasa.gov/wms/virtualearth?",
                                                    false), WMSServerVersion.WMS_1_1_0, Sector.fullSphere(), "image/png",
                                                    "EPSG:4326", "", false, null);

   final WMSLayer                osmLayer  = new WMSLayer( //
                                                    "osm_auto:all", //
                                                    new URL("http://129.206.228.72/cached/osm", false), //
                                                    WMSServerVersion.WMS_1_1_0, //
                                                    Sector.fromDegrees(-85.05, -180.0, 85.05, 180.0), //
                                                    "image/jpeg", //
                                                    "EPSG:4326", //
                                                    "", //
                                                    false, //
                                                    null);


   void initParams() {

      //List of cosntrainers of camera, Object that evit that the camera goes to any place. You can add all the constraints that you need
      cameraConstraints = new ArrayList<ICameraConstrainer>();
      // Simplest implementation of  <ICameraConstrainer>, for example, evits that the earth goes so little
      scc = new SimpleCameraConstrainer();
      //You can storage any object in the glob3, for example a data model or wathever thing
      userData = null;
      //Gtask (like java runnable) is a task that always is done before launch the globe (start downloader, move the camera, etc....)
      initializationTask = null;
      //Task that are executed every periodical time. For example, launch a downloader every period
      periodicalTasks = new ArrayList<PeriodicalTask>();
      //false-> Always try to download first the last level , true -> download all the levels secuencially
      incrementalTileQuality = false;

   }


   public G3MWidget_Android getSimpleBingGlob3(final Context context) {

      final G3MWidget_Android glob3 = new G3MWidget_Android(context);

      initParams();
      cameraConstraints.add(scc);

      final LayerSet layerSet = new LayerSet();
      layerSet.addLayer(bingLayer);


      final ArrayList<Renderer> renderers = new ArrayList<Renderer>();

      //      glob3.initWidget( //
      //               cameraConstraints, //
      //               layerSet, //
      //               renderers, //
      //               userData, //
      //               initializationTask, //
      //               periodicalTasks, //
      //               incrementalTileQuality);

      return glob3;
   }


   public G3MWidget_Android getSimpleOSMGlob3(final Context context) {

      final G3MWidget_Android glob3 = new G3MWidget_Android(context);

      initParams();
      cameraConstraints.add(scc);

      final LayerSet layerSet = new LayerSet();
      layerSet.addLayer(osmLayer);


      final ArrayList<Renderer> renderers = new ArrayList<Renderer>();

      //      glob3.initWidget( //
      //               cameraConstraints, //
      //               layerSet, //
      //               renderers, //
      //               userData, //
      //               initializationTask, //
      //               periodicalTasks, //
      //               incrementalTileQuality);

      return glob3;
   }


   public G3MWidget_Android getGlob3WithCustomLayers(final Context context) {

      final G3MWidget_Android glob3 = new G3MWidget_Android(context);

      cameraConstraints.add(scc);

      final LayerSet layerSet = new LayerSet();
      layerSet.addLayer(osmLayer);


      final ArrayList<Renderer> renderers = new ArrayList<Renderer>();

      //      glob3.initWidget( //
      //               cameraConstraints, //
      //               layerSet, //
      //               renderers, //
      //               userData, //
      //               initializationTask, //
      //               periodicalTasks, //
      //               incrementalTileQuality);

      return glob3;
   }


}
