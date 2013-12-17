

package com.glob3.mobile.g3mandroidtestingapplication;

import org.glob3.mobile.generated.AltitudeMode;
import org.glob3.mobile.generated.Color;
import org.glob3.mobile.generated.ElevationDataProvider;
import org.glob3.mobile.generated.G3MContext;
import org.glob3.mobile.generated.GEOTileRasterizer;
import org.glob3.mobile.generated.GTask;
import org.glob3.mobile.generated.Geodetic2D;
import org.glob3.mobile.generated.Geodetic3D;
import org.glob3.mobile.generated.LayerSet;
import org.glob3.mobile.generated.MapBoxLayer;
import org.glob3.mobile.generated.MarksRenderer;
import org.glob3.mobile.generated.MeshRenderer;
import org.glob3.mobile.generated.PeriodicalTask;
import org.glob3.mobile.generated.PlanetRenderer;
import org.glob3.mobile.generated.PointShape;
import org.glob3.mobile.generated.LineShape;
import org.glob3.mobile.generated.RasterLineShape;
import org.glob3.mobile.generated.RasterPolygonShape;
import org.glob3.mobile.generated.SGShape;
import org.glob3.mobile.generated.Sector;
import org.glob3.mobile.generated.ShapeLoadListener;
import org.glob3.mobile.generated.ShapesRenderer;
import org.glob3.mobile.generated.SingleBillElevationDataProvider;
import org.glob3.mobile.generated.TimeInterval;
import org.glob3.mobile.generated.URL;
import org.glob3.mobile.specific.G3MBuilder_Android;
import org.glob3.mobile.specific.G3MWidget_Android;
import org.glob3.mobile.generated.Vector3D;
import org.glob3.mobile.generated.BoxShape;
import org.glob3.mobile.generated.Shape;
import org.glob3.mobile.generated.Angle;
import org.glob3.mobile.generated.AltitudeMode;
import org.glob3.mobile.generated.Color;
import org.glob3.mobile.generated.EllipsoidShape;
import org.glob3.mobile.generated.URL;
import org.glob3.mobile.generated.ShapeTouchListener;
import org.glob3.mobile.generated.ILogger;
import org.glob3.mobile.generated.SceneJSShapesParser;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.RelativeLayout;


public class MainActivity
         extends
            Activity {

   private G3MWidget_Android _g3mWidget;
   private RelativeLayout    _placeHolder;
   private ILogger			 _logger;


   @Override
   protected void onCreate(final Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);

      setContentView(R.layout.activity_main);
      final G3MBuilder_Android builder = new G3MBuilder_Android(this);
      // builder.getPlanetRendererBuilder().setRenderDebug(true);
      
      // GeoTileRasterizer is needed to draw RasterShapes
      GEOTileRasterizer geoTileRasterizer = new GEOTileRasterizer();
      builder.getPlanetRendererBuilder().addTileRasterizer(geoTileRasterizer);


      final ShapesRenderer shapesRenderer = new ShapesRenderer(geoTileRasterizer);
      builder.addRenderer(shapesRenderer);

      final MarksRenderer marksRenderer = new MarksRenderer(true);
      builder.addRenderer(marksRenderer);

      /*final MeshRenderer meshRenderer = new MeshRenderer();
      meshRenderer.loadBSONMesh(new URL("file:///1951_r.bson"), Color.white());
      builder.addRenderer(meshRenderer);*/
      
      final LayerSet layerSet = new LayerSet();
      layerSet.addLayer(new MapBoxLayer("examples.map-9ijuk24y", TimeInterval.fromDays(30)));
      builder.getPlanetRendererBuilder().setLayerSet(layerSet);

      // final ShapeLoadListener Plistener = new ShapeLoadListener() {
      // @Override
      // public void onBeforeAddShape(final SGShape shape) {
      // // shape.setScale(2000);
      // //shape.setRoll(Angle.fromDegrees(-90));
      // }
      //
      //
      // @Override
      // public void onAfterAddShape(final SGShape shape) {
      //
      //
      // ILogger.instance().logInfo("Downloaded Building");
      //
      // final double fromDistance = 10000;
      // final double toDistance = 1000;
      //
      // final Angle fromAzimuth = Angle.fromDegrees(-90);
      // final Angle toAzimuth = Angle.fromDegrees(270);
      //
      // final Angle fromAltitude = Angle.fromDegrees(90);
      // final Angle toAltitude = Angle.fromDegrees(15);
      //
      // shape.orbitCamera(TimeInterval.fromSeconds(5), fromDistance,
      // toDistance, fromAzimuth, toAzimuth, fromAltitude,
      // toAltitude);
      //
      //
      // }
      //
      //
      // @Override
      // public void dispose() {
      // // TODO Auto-generated method stub
      //
      // }
      // };
      //
      //
      // shapesRenderer.loadBSONSceneJS(new URL("file:///target.bson"), "",
      // false, new Geodetic3D(Angle.fromDegrees(35.6452500000),
      // Angle.fromDegrees(-97.214), 30), AltitudeMode.RELATIVE_TO_GROUND,
      // Plistener);
      //
      //
      // builder.addRenderer(shapesRenderer);

      // if (false) {
      // shapesRenderer.loadBSONSceneJS(new URL("file:///A320.bson"),
      // URL.FILE_PROTOCOL + "textures-A320/", false,
      // new Geodetic3D(Angle.fromDegreesMinutesSeconds(38, 53, 42.24),
      // Angle.fromDegreesMinutesSeconds(-77, 2, 10.92),
      // 10000), AltitudeMode.ABSOLUTE, new ShapeLoadListener() {
      //
      // @Override
      // public void onBeforeAddShape(final SGShape shape) {
      // // TODO Auto-generated method stub
      // final double scale = 1e5;
      // shape.setScale(scale, scale, scale);
      // shape.setPitch(Angle.fromDegrees(90));
      //
      // }
      //
      //
      // @Override
      // public void onAfterAddShape(final SGShape shape) {
      // // TODO Auto-generated method stub
      //
      // }
      //
      //
      // @Override
      // public void dispose() {
      // // TODO Auto-generated method stub
      //
      // }
      // }, true);
      // }

      // if (false) { // Testing lights
      // shapesRenderer.addShape(new BoxShape(Geodetic3D.fromDegrees(0, 0, 0),
      // AltitudeMode.RELATIVE_TO_GROUND, new Vector3D(
      // 1000000, 1000000, 1000000), (float) 1.0, Color.red(), Color.black(),
      // true)); // With normals
      //
      // shapesRenderer.addShape(new BoxShape(Geodetic3D.fromDegrees(0, 180,
      // 0), AltitudeMode.RELATIVE_TO_GROUND, new Vector3D(
      // 1000000, 1000000, 1000000), (float) 1.0, Color.blue(), Color.black(),
      // true)); // With normals
      //
      // }

      // if (false) { // Adding and deleting marks
      //
      // final int time = 1; // SECS
      //
      // final GTask markTask = new GTask() {
      // ArrayList<Mark> _marks = new ArrayList<Mark>();
      //
      //
      // int randomInt(final int max) {
      // return (int) (Math.random() * max);
      // }
      //
      //
      // @Override
      // public void run(final G3MContext context) {
      // final double lat = randomInt(180) - 90;
      // final double lon = randomInt(360) - 180;
      //
      // final Mark m1 = new Mark("RANDOM MARK", new
      // URL("http://glob3m.glob3mobile.com/icons/markers/g3m.png", false),
      // Geodetic3D.fromDegrees(lat, lon, 0), AltitudeMode.RELATIVE_TO_GROUND,
      // 1e9);
      // marksRenderer.addMark(m1);
      //
      // _marks.add(m1);
      // if (_marks.size() > 5) {
      //
      // marksRenderer.removeAllMarks();
      //
      // for (int i = 0; i < _marks.size(); i++) {
      // _marks.get(i).dispose();
      // }
      //
      //
      // _marks.clear();
      //
      // }
      //
      // }
      // };
      //
      // builder.addPeriodicalTask(new
      // PeriodicalTask(TimeInterval.fromSeconds(time), markTask));
      // }

      // if (false) {
      //
      // final GInitializationTask initializationTask = new
      // GInitializationTask() {
      //
      // @Override
      // public void run(final G3MContext context) {
      //
      // final IBufferDownloadListener listener = new
      // IBufferDownloadListener() {
      //
      // @Override
      // public void onError(final URL url) {
      // // TODO Auto-generated method stub
      //
      // }
      //
      //
      // @Override
      // public void onDownload(final URL url,
      // final IByteBuffer buffer,
      // final boolean expired) {
      // // TODO Auto-generated method stub
      //
      // final Shape shape = SceneJSShapesParser.parseFromBSON(buffer,
      // URL.FILE_PROTOCOL + "2029/", true,
      // Geodetic3D.fromDegrees(0, 0, 0), AltitudeMode.ABSOLUTE);
      //
      // shapesRenderer.addShape(shape);
      // }
      //
      //
      // @Override
      // public void onCanceledDownload(final URL url,
      // final IByteBuffer buffer,
      // final boolean expired) {
      // // TODO Auto-generated method stub
      //
      // }
      //
      //
      // @Override
      // public void onCancel(final URL url) {
      // // TODO Auto-generated method stub
      //
      // }
      // };
      //
      // context.getDownloader().requestBuffer(new URL(URL.FILE_PROTOCOL +
      // "2029/2029.bson"), 1000, TimeInterval.forever(),
      // true, listener, true);
      //
      //
      // }
      //
      //
      // @Override
      // public boolean isDone(final G3MContext context) {
      // // TODO Auto-generated method stub
      // return true;
      // }
      //
      // };
      //
      // builder.setInitializationTask(initializationTask);
      //
      // }

      if (false) {

         final int time = 10; // SECS

         final GTask elevationTask = new GTask() {

            ElevationDataProvider _elevationDataProvider1 = new SingleBillElevationDataProvider(new URL(
                                                                   "file:///full-earth-2048x1024.bil", false),
                                                                   Sector.fullSphere(), new Vector2I(2048, 1024));


            @Override
            public void run(final G3MContext context) {
               final PlanetRenderer pr = _g3mWidget.getG3MWidget().getPlanetRenderer();

               final Random r = new Random();

               final int i = r.nextInt() % 4;
               switch (i) {
                  case 0:
                     pr.setElevationDataProvider(_elevationDataProvider1, false);
                     break;
                  case 1:

                     final ElevationDataProvider _elevationDataProvider2 = new SingleBillElevationDataProvider(new URL(
                              "file:///caceres-2008x2032.bil", false), Sector.fromDegrees(39.4642996294239623,
                              -6.3829977122432933, 39.4829891936013553, -6.3645288909498845), new Vector2I(2008, 2032), 0);


                     pr.setElevationDataProvider(_elevationDataProvider2, true);
                     break;
                  case 2:
                     pr.setVerticalExaggeration(r.nextInt() % 5);
                     break;
                  case 3:
                     pr.setElevationDataProvider(null, false);
                     break;

                  default:
                     break;
               }

               final ElevationDataProvider edp = pr.getElevationDataProvider();
               if (edp != null) {
                  edp.setEnabled((r.nextInt() % 2) == 0);
               }
            }
         };

         builder.addPeriodicalTask(new PeriodicalTask(TimeInterval.fromSeconds(time), elevationTask));
      }

      if (true) {      
    	  // testing selecting shapes
    	  final double factor = 3000;
    	  final Vector3D radius1 = new Vector3D(factor, factor, factor);
    	  final Vector3D radius2 = new Vector3D(factor*1.5, factor*1.5, factor*1.5);
    	  final Vector3D radiusBox = new Vector3D(factor, factor*1.5, factor*2);

    	  Shape box1 = new BoxShape(new Geodetic3D(Angle.fromDegrees(39.70),
    			  Angle.fromDegrees(2.80),
    			  radiusBox._z/2),
    			  AltitudeMode.ABSOLUTE,
    			  radiusBox,
    			  0,
    			  Color.fromRGBA(0,    1, 0, 1));
    	  shapesRenderer.addShape(box1);

    	  Shape ellipsoid1 = new EllipsoidShape(new Geodetic3D(Angle.fromDegrees(39.80),
    			  Angle.fromDegrees(2.90),
    			  radius1._z),
    			  AltitudeMode.ABSOLUTE,
    			  new URL("file:///world.jpg", false),
    			  radius1,
    			  (short)32,
    			  (short)0,
    			  false,
    			  false);
    	  shapesRenderer.addShape(ellipsoid1);

    	  Shape mercator1 = new EllipsoidShape(new Geodetic3D(Angle.fromDegrees(39.60),
    			  Angle.fromDegrees(3),
    			  radius2._x),
    			  AltitudeMode.ABSOLUTE,
    			  new URL("file:///mercator_debug.png", false),
    			  radius2,
    			  (short)32,
    			  (short)0,
    			  false,
    			  true);
    	  shapesRenderer.addShape(mercator1);
    	  
    	  // DRAWING POINTS
    	  {
    		  Shape point = new PointShape(new Geodetic3D(Angle.fromDegrees(39.70),
    				  Angle.fromDegrees(3.30),
    				  radiusBox._z),
    				  AltitudeMode.ABSOLUTE,
    				  8,
    				  Color.fromRGBA(0, 0, 1, 1));
    		  shapesRenderer.addShape(point);
    	  }
    	  {
    		  Shape point = new PointShape(new Geodetic3D(Angle.fromDegrees(39.55),
    				  Angle.fromDegrees(3.40),
    				  radiusBox._z),
    				  AltitudeMode.ABSOLUTE,
    				  6,
    				  Color.fromRGBA(0, 0, 1, 1));
    		  shapesRenderer.addShape(point);
    	  }
    	  {
    		  Shape point = new PointShape(new Geodetic3D(Angle.fromDegrees(39.70),
    				  Angle.fromDegrees(3.50),
    				  radiusBox._z),
    				  AltitudeMode.ABSOLUTE,
    				  4,
    				  Color.fromRGBA(0, 0, 1, 1));
    		  shapesRenderer.addShape(point);
    	  }

    	  // DRAWING LINES
    	  {
    		  Shape line = new LineShape(new Geodetic3D(Angle.fromDegrees(39.69),
    				  Angle.fromDegrees(3.31),
    				  radiusBox._z),
    				  new Geodetic3D(Angle.fromDegrees(39.56),
    						  Angle.fromDegrees(3.39),
    						  radiusBox._z),
    						  AltitudeMode.ABSOLUTE,
    						  5,
    						  Color.fromRGBA(1, 0.5f, 0, 1));
    		  shapesRenderer.addShape(line);
    	  }
    	  {
    		  Shape line = new LineShape(new Geodetic3D(Angle.fromDegrees(39.56),
    				  Angle.fromDegrees(3.41),
    				  radiusBox._z),
    				  new Geodetic3D(Angle.fromDegrees(39.69),
    						  Angle.fromDegrees(3.49),
    						  radiusBox._z),   		                                
    						  AltitudeMode.ABSOLUTE,
    						  5,
    						  Color.fromRGBA(1, 0.5f, 0, 1));
    		  shapesRenderer.addShape(line);
    	  }
    	  {
    		  Shape line = new LineShape(new Geodetic3D(Angle.fromDegrees(39.70),
    				  Angle.fromDegrees(3.31),
    				  radiusBox._z),
    				  new Geodetic3D(Angle.fromDegrees(39.70),
    						  Angle.fromDegrees(3.49),
    						  radiusBox._z),
    						  AltitudeMode.ABSOLUTE,
    						  5,
    						  Color.fromRGBA(1, 0.5f, 0, 1));
    		  shapesRenderer.addShape(line);
    	  }

    	  // DRAWING RASTER LINES
    	  {
    		  Shape rasterLine = new RasterLineShape(new Geodetic2D(Angle.fromDegrees(39.40),
    				  Angle.fromDegrees(2.70)),
    				  new Geodetic2D(Angle.fromDegrees(39.40),
    						  Angle.fromDegrees(3.00)),
    						  2,
    						  Color.fromRGBA(0, 0, 1, 1));
    		  shapesRenderer.addShape(rasterLine);
    	  }

    	  // DRAWING RASTER POLYGON
    	  {
    		  java.util.ArrayList<Geodetic2D> vertices = new java.util.ArrayList<Geodetic2D>();
    		  vertices.add(new Geodetic2D(Angle.fromDegrees(39.50), Angle.fromDegrees(3.10)));
    		  vertices.add(new Geodetic2D(Angle.fromDegrees(39.38), Angle.fromDegrees(3.20)));
    		  vertices.add(new Geodetic2D(Angle.fromDegrees(39.40), Angle.fromDegrees(3.28)));
    		  vertices.add(new Geodetic2D(Angle.fromDegrees(39.60), Angle.fromDegrees(3.25)));

    		  Shape pol1 = new RasterPolygonShape(vertices,
    				  2,
    				  Color.green(),
    				  Color.fromRGBA(1, 1, 1, 0.6f));
    		  shapesRenderer.addShape(pol1);
    	  }

    	  // DRAWING JSON
         /* shapesRenderer.loadJSONSceneJS(new URL("file:///seymour-plane.json", false), 
        		  "file:////", 
        		  false,
    			  new Geodetic3D(Angle.fromDegrees(39.70),
    					  Angle.fromDegrees(2.60),
    					  7*factor),
    					  AltitudeMode.ABSOLUTE, new ShapeLoadListener() {

                     @Override
                     public void onBeforeAddShape(final SGShape shape) {
                    	 double scale = factor/5;
                   	  shape.setScale(scale, scale, scale);
                   	  shape.setPitch(Angle.fromDegrees(120));
                   	  shape.setHeading(Angle.fromDegrees(-110));
                     }


                     @Override
                     public void onAfterAddShape(final SGShape shape) {
                     }


                     @Override
                     public void dispose() {
                        // TODO Auto-generated method stub

                     }
                  });*/
/*
    	  Shape plane = SceneJSShapesParser.parseFromJSON("file:///seymour-plane.json", 
    			  "file:////", 
    			  false,
    			  new Geodetic3D(Angle.fromDegrees(39.70),
    					  Angle.fromDegrees(2.60),
    					  7*factor),
    					  AltitudeMode.ABSOLUTE);
    	  double scale = factor/5;
    	  plane.setScale(scale, scale, scale);
    	  plane.setPitch(Angle.fromDegrees(120));
    	  plane.setHeading(Angle.fromDegrees(-110));
    	  shapesRenderer.addShape(plane);*/



    	  // adding touch listener
    	  ShapeTouchListener myShapeTouchListener = new ShapeTouchListener() {
    		  Shape _selectedShape = null;
    		  
    		  public boolean touchedShape(Shape shape) {
    			      			  
    			  if (_selectedShape == null) {
    				  shape.select();
    				  _selectedShape = shape;
    			  } else {
    				  if (_selectedShape==shape) {
    					  shape.unselect();
    					  _selectedShape = null;
    				  } else {
    					  _selectedShape.unselect();
    					  _selectedShape = shape;
    					  shape.select();
    				  }
    			  }
    			  return true;
    		  }
    	  };
      
    	  shapesRenderer.setShapeTouchListener(myShapeTouchListener, true);
      }

      _g3mWidget = builder.createWidget();
      _logger = _g3mWidget.getG3MContext().getLogger();
      _logger.logInfo("Testing shape selection");
      _placeHolder = (RelativeLayout) findViewById(R.id.g3mWidgetHolder);
      _placeHolder.addView(_g3mWidget);
      
      if (true) {
    	  Geodetic3D position = new Geodetic3D(Angle.fromDegrees(39.08), Angle.fromDegrees(2.90), 113000);
    	  _g3mWidget.setCameraPosition(position);
    	  _g3mWidget.setCameraHeading(Angle.fromDegrees(5.0));
    	  _g3mWidget.setCameraPitch(Angle.fromDegrees(24.0));
      }


   }


   @Override
   public boolean onCreateOptionsMenu(final Menu menu) {
      // Inflate the menu; this adds items to the action bar if it is present.
      getMenuInflater().inflate(R.menu.main, menu);
      return true;
   }

}
