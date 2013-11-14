

package com.glob3.mobile.g3mandroidtestingapplication;

import org.glob3.mobile.generated.Color;
import org.glob3.mobile.generated.MarksRenderer;
import org.glob3.mobile.generated.MeshRenderer;
import org.glob3.mobile.generated.ShapesRenderer;
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
      //   builder.getPlanetRendererBuilder().setRenderDebug(true);

      final ShapesRenderer shapesRenderer = new ShapesRenderer();
      // builder.addRenderer(shapesRenderer);

      final MarksRenderer marksRenderer = new MarksRenderer(true);
      builder.addRenderer(marksRenderer);


      final MeshRenderer meshRenderer = new MeshRenderer();
      meshRenderer.loadBSONMesh(new URL("file:///1951_r.bson"), Color.white());
      builder.addRenderer(meshRenderer);


      //      final ShapeLoadListener Plistener = new ShapeLoadListener() {
      //         @Override
      //         public void onBeforeAddShape(final SGShape shape) {
      //            //  shape.setScale(2000);
      //            //shape.setRoll(Angle.fromDegrees(-90));
      //         }
      //
      //
      //         @Override
      //         public void onAfterAddShape(final SGShape shape) {
      //
      //
      //            ILogger.instance().logInfo("Downloaded Building");
      //
      //            final double fromDistance = 10000;
      //            final double toDistance = 1000;
      //
      //            final Angle fromAzimuth = Angle.fromDegrees(-90);
      //            final Angle toAzimuth = Angle.fromDegrees(270);
      //
      //            final Angle fromAltitude = Angle.fromDegrees(90);
      //            final Angle toAltitude = Angle.fromDegrees(15);
      //
      //            shape.orbitCamera(TimeInterval.fromSeconds(5), fromDistance, toDistance, fromAzimuth, toAzimuth, fromAltitude,
      //                     toAltitude);
      //
      //
      //         }
      //
      //
      //         @Override
      //         public void dispose() {
      //            // TODO Auto-generated method stub
      //
      //         }
      //      };
      //
      //
      //      shapesRenderer.loadBSONSceneJS(new URL("file:///target.bson"), "", false, new Geodetic3D(Angle.fromDegrees(35.6452500000),
      //               Angle.fromDegrees(-97.214), 30), AltitudeMode.RELATIVE_TO_GROUND, Plistener);
      //
      //
      //      builder.addRenderer(shapesRenderer);


      //      if (false) {
      //         shapesRenderer.loadBSONSceneJS(new URL("file:///A320.bson"), URL.FILE_PROTOCOL + "textures-A320/", false,
      //                  new Geodetic3D(Angle.fromDegreesMinutesSeconds(38, 53, 42.24), Angle.fromDegreesMinutesSeconds(-77, 2, 10.92),
      //                           10000), AltitudeMode.ABSOLUTE, new ShapeLoadListener() {
      //
      //                     @Override
      //                     public void onBeforeAddShape(final SGShape shape) {
      //                        // TODO Auto-generated method stub
      //                        final double scale = 1e5;
      //                        shape.setScale(scale, scale, scale);
      //                        shape.setPitch(Angle.fromDegrees(90));
      //
      //                     }
      //
      //
      //                     @Override
      //                     public void onAfterAddShape(final SGShape shape) {
      //                        // TODO Auto-generated method stub
      //
      //                     }
      //
      //
      //                     @Override
      //                     public void dispose() {
      //                        // TODO Auto-generated method stub
      //
      //                     }
      //                  }, true);
      //      }

      //      if (false) { // Testing lights
      //         shapesRenderer.addShape(new BoxShape(Geodetic3D.fromDegrees(0, 0, 0), AltitudeMode.RELATIVE_TO_GROUND, new Vector3D(
      //                  1000000, 1000000, 1000000), (float) 1.0, Color.red(), Color.black(), true)); // With normals
      //
      //         shapesRenderer.addShape(new BoxShape(Geodetic3D.fromDegrees(0, 180, 0), AltitudeMode.RELATIVE_TO_GROUND, new Vector3D(
      //                  1000000, 1000000, 1000000), (float) 1.0, Color.blue(), Color.black(), true)); // With normals
      //
      //      }
      //      if (false) { // Adding and deleting marks
      //
      //         final int time = 1; // SECS
      //
      //         final GTask markTask = new GTask() {
      //            ArrayList<Mark> _marks = new ArrayList<Mark>();
      //
      //
      //            int randomInt(final int max) {
      //               return (int) (Math.random() * max);
      //            }
      //
      //
      //            @Override
      //            public void run(final G3MContext context) {
      //               final double lat = randomInt(180) - 90;
      //               final double lon = randomInt(360) - 180;
      //
      //               final Mark m1 = new Mark("RANDOM MARK", new URL("http://glob3m.glob3mobile.com/icons/markers/g3m.png", false),
      //                        Geodetic3D.fromDegrees(lat, lon, 0), AltitudeMode.RELATIVE_TO_GROUND, 1e9);
      //               marksRenderer.addMark(m1);
      //
      //               _marks.add(m1);
      //               if (_marks.size() > 5) {
      //
      //                  marksRenderer.removeAllMarks();
      //
      //                  for (int i = 0; i < _marks.size(); i++) {
      //                     _marks.get(i).dispose();
      //                  }
      //
      //
      //                  _marks.clear();
      //
      //               }
      //
      //            }
      //         };
      //
      //         builder.addPeriodicalTask(new PeriodicalTask(TimeInterval.fromSeconds(time), markTask));
      //      }


      //      if (false) {
      //
      //         final GInitializationTask initializationTask = new GInitializationTask() {
      //
      //            @Override
      //            public void run(final G3MContext context) {
      //
      //               final IBufferDownloadListener listener = new IBufferDownloadListener() {
      //
      //                  @Override
      //                  public void onError(final URL url) {
      //                     // TODO Auto-generated method stub
      //
      //                  }
      //
      //
      //                  @Override
      //                  public void onDownload(final URL url,
      //                                         final IByteBuffer buffer,
      //                                         final boolean expired) {
      //                     // TODO Auto-generated method stub
      //
      //                     final Shape shape = SceneJSShapesParser.parseFromBSON(buffer, URL.FILE_PROTOCOL + "2029/", true,
      //                              Geodetic3D.fromDegrees(0, 0, 0), AltitudeMode.ABSOLUTE);
      //
      //                     shapesRenderer.addShape(shape);
      //                  }
      //
      //
      //                  @Override
      //                  public void onCanceledDownload(final URL url,
      //                                                 final IByteBuffer buffer,
      //                                                 final boolean expired) {
      //                     // TODO Auto-generated method stub
      //
      //                  }
      //
      //
      //                  @Override
      //                  public void onCancel(final URL url) {
      //                     // TODO Auto-generated method stub
      //
      //                  }
      //               };
      //
      //               context.getDownloader().requestBuffer(new URL(URL.FILE_PROTOCOL + "2029/2029.bson"), 1000, TimeInterval.forever(),
      //                        true, listener, true);
      //
      //
      //            }
      //
      //
      //            @Override
      //            public boolean isDone(final G3MContext context) {
      //               // TODO Auto-generated method stub
      //               return true;
      //            }
      //
      //         };
      //
      //         builder.setInitializationTask(initializationTask);
      //
      //      }

      if (true) {      
    	  // testing selecting shapes
    	  final double factor = 2e5;
    	  final Vector3D radius1 = new Vector3D(factor, factor, factor);
    	  final Vector3D radius2 = new Vector3D(factor*1.5, factor*1.5, factor*1.5);
    	  final Vector3D radiusBox = new Vector3D(factor, factor*1.5, factor*2);

    	  Shape box1 = new BoxShape(new Geodetic3D(Angle.fromDegrees(0),
    			  Angle.fromDegrees(10),
    			  radiusBox._z/2),
    			  AltitudeMode.ABSOLUTE,
    			  radiusBox,
    			  0,
    			  Color.fromRGBA(0,    1, 0, 1));
    	  shapesRenderer.addShape(box1);

    	  Shape ellipsoid1 = new EllipsoidShape(new Geodetic3D(Angle.fromDegrees(0),
    			  Angle.fromDegrees(0),
    			  radius1._x),
    			  AltitudeMode.ABSOLUTE,
    			  new URL("file:///world.jpg", false),
    			  radius1,
    			  (short)32,
    			  (short)0,
    			  false,
    			  false);
    	  shapesRenderer.addShape(ellipsoid1);

    	  Shape mercator1 = new EllipsoidShape(new Geodetic3D(Angle.fromDegrees(0),
    			  Angle.fromDegrees(5),
    			  radius2._x),
    			  AltitudeMode.ABSOLUTE,
    			  new URL("file:///mercator_debug.png", false),
    			  radius2,
    			  (short)32,
    			  (short)0,
    			  false,
    			  true);
    	  shapesRenderer.addShape(mercator1);
    	  
    	 

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
    	  Geodetic3D position = new Geodetic3D(Angle.fromDegrees(-12.0875), Angle.fromDegrees(15.2036), 2328992);
    	  _g3mWidget.setCameraPosition(position);
    	  _g3mWidget.setCameraHeading(Angle.fromDegrees(-40.72));
    	  _g3mWidget.setCameraPitch(Angle.fromDegrees(35.48));
      }


   }


   @Override
   public boolean onCreateOptionsMenu(final Menu menu) {
      // Inflate the menu; this adds items to the action bar if it is present.
      getMenuInflater().inflate(R.menu.main, menu);
      return true;
   }

}
