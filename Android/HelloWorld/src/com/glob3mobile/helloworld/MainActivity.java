

package com.glob3mobile.helloworld;

import org.glob3.mobile.generated.G3MContext;
import org.glob3.mobile.generated.GInitializationTask;
import org.glob3.mobile.generated.IBufferDownloadListener;
import org.glob3.mobile.generated.IByteBuffer;
import org.glob3.mobile.generated.IDownloader;
import org.glob3.mobile.generated.IJSONParser;
import org.glob3.mobile.generated.JSONArray;
import org.glob3.mobile.generated.JSONBaseObject;
import org.glob3.mobile.generated.JSONObject;
import org.glob3.mobile.generated.Mark;

import org.glob3.mobile.generated.AltitudeMode;
import org.glob3.mobile.generated.Angle;
import org.glob3.mobile.generated.Color;
import org.glob3.mobile.generated.EllipsoidShape;
import org.glob3.mobile.generated.Geodetic2D;
import org.glob3.mobile.generated.Geodetic3D;
import org.glob3.mobile.generated.JSONNumber;
import org.glob3.mobile.generated.LayerSet;
import org.glob3.mobile.generated.LevelTileCondition;
import org.glob3.mobile.generated.MarksRenderer;
import org.glob3.mobile.generated.NonOverlapping3DMark;
import org.glob3.mobile.generated.NonOverlapping3DMarksRenderer;
import org.glob3.mobile.generated.Sector;
import org.glob3.mobile.generated.Shape;
import org.glob3.mobile.generated.TimeInterval;
import org.glob3.mobile.generated.URL;
import org.glob3.mobile.generated.Vector3D;
import org.glob3.mobile.generated.WMSLayer;
import org.glob3.mobile.generated.WMSServerVersion;
import org.glob3.mobile.specific.G3MBuilder_Android;
import org.glob3.mobile.specific.G3MWidget_Android;
import org.glob3.mobile.specific.JSONParser_Android;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.LinearLayout;
import android.widget.Toast;


public class MainActivity
         extends
            Activity {
	NonOverlapping3DMarksRenderer forceGraphRenderer;

   @Override
   protected void onCreate(final Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);

      final G3MBuilder_Android builder = new G3MBuilder_Android(this);

      final Geodetic2D lower = new Geodetic2D( //
               Angle.fromDegrees(43.532822), //
               Angle.fromDegrees(1.350360));
      final Geodetic2D upper = new Geodetic2D( //
               Angle.fromDegrees(43.668522), //
               Angle.fromDegrees(1.515350));

      final Sector demSector = new Sector(lower, upper);

      final LayerSet layerSet = new LayerSet();
      final WMSLayer franceRaster4000K = new WMSLayer("Raster4000k", new URL("http://www.geosignal.org/cgi-bin/wmsmap?", false),
               WMSServerVersion.WMS_1_1_0, demSector, "image/jpeg", "EPSG:4326", "", false, new LevelTileCondition(0, 18),
               TimeInterval.fromDays(30), true);

      layerSet.addLayer(franceRaster4000K);
      //     builder.getPlanetRendererBuilder().setLayerSet(layerSet);


      //      final MapBoxLayer mboxTerrainLayer = new MapBoxLayer("examples.map-qogxobv1", TimeInterval.fromDays(30), true, 11);
      //      layerSet.addLayer(mboxTerrainLayer);

      Shape sphere = new EllipsoidShape(new Geodetic3D(Angle.fromDegrees(0), Angle.fromDegrees(0), 5), AltitudeMode.ABSOLUTE, new Vector3D(100000, 100000, 100000), (short) 16, 0, false, false, Color.fromRGBA((float).5,(float) 1, (float).5, (float).9));

      Shape anchor_sphere = new EllipsoidShape(new Geodetic3D(Angle.fromDegrees(0), Angle.fromDegrees(0), 5), AltitudeMode.ABSOLUTE, new Vector3D(100000, 100000, 100000), (short) 16, 0, false, false, Color.fromRGBA((float).5,(float) 1, (float).5, (float).9));

      forceGraphRenderer = new NonOverlapping3DMarksRenderer(10);
      /*NonOverlapping3DMark anchor = new NonOverlapping3DMark(anchor_sphere, sphere, Geodetic3D.fromDegrees(0, 0, 5e5));
      NonOverlapping3DMark node = new NonOverlapping3DMark(sphere, anchor_sphere, Geodetic3D.fromDegrees(4, 0, 5e5));
      NonOverlapping3DMark node2 = new NonOverlapping3DMark(sphere, anchor_sphere, Geodetic3D.fromDegrees(0, 4, 5));
      NonOverlapping3DMark node3 = new NonOverlapping3DMark(sphere, anchor_sphere, Geodetic3D.fromDegrees(30, 30, 3));
      NonOverlapping3DMark node4 = new NonOverlapping3DMark(sphere, anchor_sphere, Geodetic3D.fromDegrees(30, 30, 3));
      node.addAnchor(anchor);
      node2.addAnchor(anchor);
      node3.addNeighbor(node2);
      node4.addNeighbor(node);

      forceGraphRenderer.addMark(node);
      forceGraphRenderer.addMark(anchor);
      forceGraphRenderer.addMark(node2);
      forceGraphRenderer.addMark(node3);
      forceGraphRenderer.addMark(node4);*/
     
      builder.setInitializationTask(getWikiGraphTask());
      builder.addRenderer(forceGraphRenderer);
      final G3MWidget_Android g3mWidget = builder.createWidget();
      

      //  g3mWidget.setCameraPosition(new Geodetic3D(demSector.getCenter(), 10000));

      final LinearLayout layout = (LinearLayout) findViewById(R.id.glob3);
      layout.addView(g3mWidget);

   }
   
   @Override
   public boolean onCreateOptionsMenu(final Menu menu) {
      // Inflate the menu; this adds items to the action bar if it is present.
      getMenuInflater().inflate(R.menu.main, menu);
      return true;
   }
   

   private org.glob3.mobile.generated.GInitializationTask getWikiGraphTask() {
      final GInitializationTask initializationTask = new GInitializationTask() {
    	 boolean _WikiGraphIsDone = false;

         @Override
         public void run(final G3MContext context) {

            final IDownloader downloader = context.getDownloader();

            final IBufferDownloadListener listener = new IBufferDownloadListener() {
            	

               @Override
               public void onDownload(final URL url,
                                      final IByteBuffer buffer,
                                      final boolean expired) {

                  final String response = buffer.getAsString();
                  final IJSONParser parser = new JSONParser_Android();
                  final JSONBaseObject jsonObject = parser.parse(response);
                  final JSONObject object = jsonObject.asObject();
                  final JSONArray list = object.getAsArray("geonames");
                  
                  //for now, hard code anchor for new york, eventually query various places and query those places for neighbors
                  Geodetic3D nypos = new Geodetic3D(Angle.fromDegrees(40.7127), Angle.fromDegrees(-74.0059), 500); 
                  
                  NonOverlapping3DMark anchor = new NonOverlapping3DMark(null, null, nypos);
                  NonOverlapping3DMark nynode = new NonOverlapping3DMark(null, null, nypos);
                  nynode.addAnchor(anchor);
                  forceGraphRenderer.addMark(anchor);
                  forceGraphRenderer.addMark(nynode);
                  for (int i = 0; i < list.size(); i++) {

                     final JSONObject entry = list.getAsObject(i);
                	  
                	  JSONNumber lat = entry.getAsNumber("lat");
                	  JSONNumber lon = entry.getAsNumber("lng");

                   
                     final Geodetic3D position = new Geodetic3D(Angle.fromDegrees(lat.value()),
                              Angle.fromDegrees(lon.value()), 10);
                 
                     //create and add mark here
                     NonOverlapping3DMark m = new NonOverlapping3DMark(null, null, position);
                     m.addNeighbor(nynode);
                     forceGraphRenderer.addMark(m);
                     
                     
     

                  }


                 _WikiGraphIsDone = true;
               }


               @Override
               public void onError(final URL url) {
                  Toast.makeText(getApplicationContext(), "Error retrieving  weather data", Toast.LENGTH_SHORT).show();

               }


               @Override
               public void onCancel(final URL url) {
                  //DO Nothing
               }


               @Override
               public void onCanceledDownload(final URL url,
                                              final IByteBuffer data,
                                              final boolean expired) {
                  //Do Nothing
               }
            };

            downloader.requestBuffer( //
                     new URL("http://api.geonames.org/wikipediaSearchJSON?q=newyork&maxRows=10&username=stefaniea", false), //
                     0, //
                     TimeInterval.fromHours(1.0), //
                     false, //
                     listener, //
                     false);

         }


         @Override
         public boolean isDone(final G3MContext context) {
            return _WikiGraphIsDone;
         }
      };
      return initializationTask;
   }


}


