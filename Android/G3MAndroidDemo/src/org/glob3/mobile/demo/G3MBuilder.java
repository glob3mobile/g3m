

package org.glob3.mobile.demo;

import java.util.ArrayList;

import org.glob3.mobile.generated.Angle;
import org.glob3.mobile.generated.BoxShape;
import org.glob3.mobile.generated.CircleShape;
import org.glob3.mobile.generated.Color;
import org.glob3.mobile.generated.G3MContext;
import org.glob3.mobile.generated.GInitializationTask;
import org.glob3.mobile.generated.Geodetic3D;
import org.glob3.mobile.generated.ICameraConstrainer;
import org.glob3.mobile.generated.LayerSet;
import org.glob3.mobile.generated.PeriodicalTask;
import org.glob3.mobile.generated.Renderer;
import org.glob3.mobile.generated.Sector;
import org.glob3.mobile.generated.Shape;
import org.glob3.mobile.generated.ShapesRenderer;
import org.glob3.mobile.generated.SimpleCameraConstrainer;
import org.glob3.mobile.generated.TimeInterval;
import org.glob3.mobile.generated.URL;
import org.glob3.mobile.generated.Vector3D;
import org.glob3.mobile.generated.WMSLayer;
import org.glob3.mobile.generated.WMSServerVersion;
import org.glob3.mobile.generated.WidgetUserData;
import org.glob3.mobile.specific.G3MBuilder_Android;
import org.glob3.mobile.specific.G3MWidget_Android;

import android.content.Context;


/**
 * Does some thing in old style.
 * 
 * @deprecated use {@link G3MBuilder_Android} instead.
 */

@Deprecated
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
   WidgetUserData                userData;
   //Gtask (like java runnable) is a task that always is done before launch the globe (start downloader, move the camera, etc....)
   GInitializationTask           initializationTask;
   //Task that are executed every periodical time. For example, launch a downloader every period
   ArrayList<PeriodicalTask>     periodicalTasks;
   //false-> Always try to download first the last level , true -> download all the levels secuencially
   boolean                       incrementalTileQuality;


   //Layers Defined
   final WMSLayer                bingLayer = new WMSLayer("ve", new URL("http://worldwind27.arc.nasa.gov/wms/virtualearth?",
                                                    false), WMSServerVersion.WMS_1_1_0, Sector.fullSphere(), "image/png",
                                                    "EPSG:4326", "", false, null, TimeInterval.fromDays(30));

   final WMSLayer                osmLayer  = new WMSLayer( //
                                                    "osm_auto:all", //
                                                    new URL("http://129.206.228.72/cached/osm", false), //
                                                    WMSServerVersion.WMS_1_1_0, //
                                                    Sector.fromDegrees(-85.05, -180.0, 85.05, 180.0), //
                                                    "image/jpeg", //
                                                    "EPSG:4326", //
                                                    "", //
                                                    false, //
                                                    null, //
                                                    TimeInterval.fromDays(30));


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

      final boolean useQuadShapes = true;
      if (useQuadShapes) {
         final ShapesRenderer shapesRenderer = new ShapesRenderer();

         final Geodetic3D circlePosition = new Geodetic3D(Angle.fromDegrees(37.78333333), //
                  Angle.fromDegrees(-122.76666666666667), //
                  8000);
         final Color circleColor = Color.newFromRGBA(1, 1, 0, 0.5f);
         final Shape circle = new CircleShape(circlePosition, 50000, circleColor);
         shapesRenderer.addShape(circle);

         final Geodetic3D boxPosition = new Geodetic3D(Angle.fromDegrees(37.78333333), //
                  Angle.fromDegrees(-122.41666666666667), //
                  45000);
         final Vector3D size = new Vector3D(20000, 30000, 50000);
         final Color boxColor = Color.newFromRGBA(0, 1, 0, 0.5f);
         final Color edgeColor = Color.newFromRGBA(0.75f, 0, 0, 0.75f);
         final Shape box = new BoxShape(boxPosition, size, 2, boxColor, edgeColor);
         shapesRenderer.addShape(box);

         renderers.add(shapesRenderer);
      }


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


   public G3MWidget_Android getCustomLayersGlob3(final Context context,
                                                 final LayerSet layerSet) {

      final G3MWidget_Android glob3 = new G3MWidget_Android(context);

      initParams();
      cameraConstraints.add(scc);

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


   public G3MWidget_Android getRenderersGlob3(final Context context,
                                              final ArrayList<Renderer> renderers) {
      final G3MWidget_Android glob3 = new G3MWidget_Android(context);

      initParams();
      cameraConstraints.add(scc);

      final LayerSet layerSet = new LayerSet();
      layerSet.addLayer(bingLayer);

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


   public G3MWidget_Android getRenderersAnimationGlob3(final Context context,
                                                       final ArrayList<Renderer> renderers,
                                                       final ArrayList<PeriodicalTask> periodicalTasks2) {
      final G3MWidget_Android glob3 = new G3MWidget_Android(context);

      initParams();
      cameraConstraints.add(scc);

      final LayerSet layerSet = new LayerSet();
      layerSet.addLayer(bingLayer);

      //      glob3.initWidget( //
      //               cameraConstraints, //
      //               layerSet, //
      //               renderers, //
      //               userData, //
      //               initializationTask, //
      //               periodicalTasks2, //
      //               incrementalTileQuality);

      return glob3;
   }


   public G3MWidget_Android getRenderersAnimationGlob3InitialPosition(final Context context,
                                                                      final ArrayList<Renderer> renderers,
                                                                      final ArrayList<PeriodicalTask> periodicalTasks2,
                                                                      final Geodetic3D position) {
      final G3MWidget_Android glob3 = new G3MWidget_Android(context);

      initParams();
      cameraConstraints.add(scc);


      final GInitializationTask initializationTask1 = new GInitializationTask() {
         @Override
         public void run(final G3MContext context1) {
            glob3.setAnimatedCameraPosition( //
                     position, //
                     TimeInterval.fromSeconds(5));
         }


         @Override
         public boolean isDone(final G3MContext context1) {
            return true;
         }
      };

      final LayerSet layerSet = new LayerSet();
      layerSet.addLayer(bingLayer);

      //      glob3.initWidget( //
      //               cameraConstraints, //
      //               layerSet, //
      //               renderers, //
      //               userData, //
      //               initializationTask1, //
      //               periodicalTasks2, //
      //               incrementalTileQuality);

      return glob3;
   }
}
