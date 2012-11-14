

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


public class Glob3s {


   //TODO: EXPLAIN WHAT IS THIS!!
   final static ArrayList<ICameraConstrainer> cameraConstraints      = new ArrayList<ICameraConstrainer>();
   final static SimpleCameraConstrainer       scc                    = new SimpleCameraConstrainer();
   //cameraConstraints.add(scc); -> is necessary?
   final static UserData                      userData               = null;
   final static GTask                         initializationTask     = null;
   final static ArrayList<PeriodicalTask>     periodicalTasks        = new ArrayList<PeriodicalTask>();
   final static boolean                       incrementalTileQuality = false;


   //Layers Defined
   final static WMSLayer                      bingLayer              = new WMSLayer(
                                                                              "ve",
                                                                              new URL(
                                                                                       "http://worldwind27.arc.nasa.gov/wms/virtualearth?",
                                                                                       false), WMSServerVersion.WMS_1_1_0,
                                                                              Sector.fullSphere(), "image/png", "EPSG:4326", "",
                                                                              false, null);


   public static G3MWidget_Android simpleBingGlob3(final Context context) {

      final G3MWidget_Android glob3 = new G3MWidget_Android(context);

      //   cameraConstraints.add(scc);

      final LayerSet layerSet = new LayerSet();
      layerSet.addLayer(bingLayer);


      final ArrayList<Renderer> renderers = new ArrayList<Renderer>();

      glob3.initWidget( //
               cameraConstraints, //
               layerSet, //
               renderers, //
               userData, //
               initializationTask, //
               periodicalTasks, //
               incrementalTileQuality);

      return glob3;
   }
}
