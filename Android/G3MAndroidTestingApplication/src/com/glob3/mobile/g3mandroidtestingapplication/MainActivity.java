

package com.glob3.mobile.g3mandroidtestingapplication;

import java.util.ArrayList;

import org.glob3.mobile.generated.AltitudeMode;
import org.glob3.mobile.generated.Angle;
import org.glob3.mobile.generated.BoxShape;
import org.glob3.mobile.generated.Color;
import org.glob3.mobile.generated.G3MContext;
import org.glob3.mobile.generated.GInitializationTask;
import org.glob3.mobile.generated.GTask;
import org.glob3.mobile.generated.Geodetic3D;
import org.glob3.mobile.generated.IBufferDownloadListener;
import org.glob3.mobile.generated.IByteBuffer;
import org.glob3.mobile.generated.Mark;
import org.glob3.mobile.generated.MarksRenderer;
import org.glob3.mobile.generated.PeriodicalTask;
import org.glob3.mobile.generated.SGShape;
import org.glob3.mobile.generated.SceneJSShapesParser;
import org.glob3.mobile.generated.Shape;
import org.glob3.mobile.generated.ShapeLoadListener;
import org.glob3.mobile.generated.ShapesRenderer;
import org.glob3.mobile.generated.TimeInterval;
import org.glob3.mobile.generated.URL;
import org.glob3.mobile.generated.Vector3D;
import org.glob3.mobile.specific.G3MBuilder_Android;
import org.glob3.mobile.specific.G3MWidget_Android;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.RelativeLayout;


public class MainActivity
         extends
            Activity {

   private G3MWidget_Android _g3mWidget;
   private RelativeLayout    _placeHolder;


   @Override
   protected void onCreate(final Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);

      setContentView(R.layout.activity_main);
      final G3MBuilder_Android builder = new G3MBuilder_Android(this);
      builder.getPlanetRendererBuilder().setRenderDebug(true);

      final ShapesRenderer shapesRenderer = new ShapesRenderer();
      builder.addRenderer(shapesRenderer);

      final MarksRenderer marksRenderer = new MarksRenderer(true);
      builder.addRenderer(marksRenderer);

      if (false) {
         shapesRenderer.loadBSONSceneJS(new URL("file:///A320.bson"), URL.FILE_PROTOCOL + "textures-A320/", false,
                  new Geodetic3D(Angle.fromDegreesMinutesSeconds(38, 53, 42.24), Angle.fromDegreesMinutesSeconds(-77, 2, 10.92),
                           10000), AltitudeMode.ABSOLUTE, new ShapeLoadListener() {

                     @Override
                     public void onBeforeAddShape(final SGShape shape) {
                        // TODO Auto-generated method stub
                        final double scale = 1e5;
                        shape.setScale(scale, scale, scale);
                        shape.setPitch(Angle.fromDegrees(90));

                     }


                     @Override
                     public void onAfterAddShape(final SGShape shape) {
                        // TODO Auto-generated method stub

                     }


                     @Override
                     public void dispose() {
                        // TODO Auto-generated method stub

                     }
                  }, true);
      }

      if (true) { // Testing lights
         shapesRenderer.addShape(new BoxShape(Geodetic3D.fromDegrees(0, 0, 0), AltitudeMode.RELATIVE_TO_GROUND, 
        		 builder.getPlanet(),
        		 new Vector3D(
                  1000000, 1000000, 1000000), (float) 1.0, Color.red(), Color.black(), true)); // With normals

         shapesRenderer.addShape(new BoxShape(Geodetic3D.fromDegrees(0, 180, 0), AltitudeMode.RELATIVE_TO_GROUND, 
        		 builder.getPlanet(),
        		 new Vector3D(
                  1000000, 1000000, 1000000), (float) 1.0, Color.blue(), Color.black(), true)); // With normals

      }

      if (true) { // Adding and deleting marks

         final int time = 1; // SECS

         final GTask markTask = new GTask() {
            ArrayList<Mark> _marks = new ArrayList<Mark>();


            int randomInt(final int max) {
               return (int) (Math.random() * max);
            }


            @Override
            public void run(final G3MContext context) {
               final double lat = randomInt(180) - 90;
               final double lon = randomInt(360) - 180;

               final Mark m1 = new Mark("RANDOM MARK", new URL("http://glob3m.glob3mobile.com/icons/markers/g3m.png", false),
                        Geodetic3D.fromDegrees(lat, lon, 0), AltitudeMode.RELATIVE_TO_GROUND, 1e9);
               marksRenderer.addMark(m1);

               _marks.add(m1);
               if (_marks.size() > 5) {

                  marksRenderer.removeAllMarks();

                  for (int i = 0; i < _marks.size(); i++) {
                     _marks.get(i).dispose();
                  }


                  _marks.clear();

               }

            }
         };

         builder.addPeriodicalTask(new PeriodicalTask(TimeInterval.fromSeconds(time), markTask));
      }


      if (false) {

         final GInitializationTask initializationTask = new GInitializationTask() {

            @Override
            public void run(final G3MContext context) {

               final IBufferDownloadListener listener = new IBufferDownloadListener() {

                  @Override
                  public void onError(final URL url) {
                     // TODO Auto-generated method stub

                  }


                  @Override
                  public void onDownload(final URL url,
                                         final IByteBuffer buffer,
                                         final boolean expired) {
                     // TODO Auto-generated method stub

                     final Shape shape = SceneJSShapesParser.parseFromBSON(buffer, URL.FILE_PROTOCOL + "2029/", true,
                              Geodetic3D.fromDegrees(0, 0, 0), AltitudeMode.ABSOLUTE);

                     shapesRenderer.addShape(shape);
                  }


                  @Override
                  public void onCanceledDownload(final URL url,
                                                 final IByteBuffer buffer,
                                                 final boolean expired) {
                     // TODO Auto-generated method stub

                  }


                  @Override
                  public void onCancel(final URL url) {
                     // TODO Auto-generated method stub

                  }
               };

               context.getDownloader().requestBuffer(new URL(URL.FILE_PROTOCOL + "2029/2029.bson"), 1000, TimeInterval.forever(),
                        true, listener, true);


            }


            @Override
            public boolean isDone(final G3MContext context) {
               // TODO Auto-generated method stub
               return true;
            }

         };

         builder.setInitializationTask(initializationTask);

      }


      _g3mWidget = builder.createWidget();
      _placeHolder = (RelativeLayout) findViewById(R.id.g3mWidgetHolder);
      _placeHolder.addView(_g3mWidget);

   }


   @Override
   public boolean onCreateOptionsMenu(final Menu menu) {
      // Inflate the menu; this adds items to the action bar if it is present.
      getMenuInflater().inflate(R.menu.main, menu);
      return true;
   }

}
