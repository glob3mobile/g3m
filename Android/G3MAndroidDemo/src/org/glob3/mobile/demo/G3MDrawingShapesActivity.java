

package org.glob3.mobile.demo;

import java.util.ArrayList;
import java.util.Random;

import org.glob3.mobile.generated.AltitudeMode;
import org.glob3.mobile.generated.Angle;
import org.glob3.mobile.generated.BoxShape;
import org.glob3.mobile.generated.CircleShape;
import org.glob3.mobile.generated.Color;
import org.glob3.mobile.generated.ElevationDataProvider;
import org.glob3.mobile.generated.G3MContext;
import org.glob3.mobile.generated.GInitializationTask;
import org.glob3.mobile.generated.GTask;
import org.glob3.mobile.generated.Geodetic3D;
import org.glob3.mobile.generated.Mark;
import org.glob3.mobile.generated.MarksRenderer;
import org.glob3.mobile.generated.PeriodicalTask;
import org.glob3.mobile.generated.Renderer;
import org.glob3.mobile.generated.Sector;
import org.glob3.mobile.generated.ShapesRenderer;
import org.glob3.mobile.generated.SingleBillElevationDataProvider;
import org.glob3.mobile.generated.TimeInterval;
import org.glob3.mobile.generated.URL;
import org.glob3.mobile.generated.Vector2I;
import org.glob3.mobile.generated.Vector3D;
import org.glob3.mobile.specific.G3MBaseActivity;
import org.glob3.mobile.specific.G3MBuilder_Android;
import org.glob3.mobile.specific.G3MWidget_Android;

import android.os.Bundle;
import android.view.Gravity;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;


public class G3MDrawingShapesActivity
         extends
            G3MBaseActivity {

   private G3MWidget_Android _widgetAndroid = null;
   private BoxShape          _boxShape;
   private CircleShape       _circleShape;
   private boolean           _animationFlag = false;


   @Override
   public void onCreate(final Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);

      setContentView(R.layout.bar_glob3_template);

      initializeToolbar();

      final G3MBuilder_Android g3mBuilder = new G3MBuilder_Android(this);
      
      ElevationDataProvider elevationDataProvider = new SingleBillElevationDataProvider(
    		  new URL("file:///full-earth-2048x1024.bil", false),
              Sector.fullSphere(),
              new Vector2I(2048, 1024));
      g3mBuilder.getPlanetRendererBuilder().setElevationDataProvider(elevationDataProvider);
      g3mBuilder.getPlanetRendererBuilder().setVerticalExaggeration(20);
      
      g3mBuilder.setLogFPS(true);
      

      final ArrayList<Renderer> renderers = new ArrayList<Renderer>();
      initializeShapes(renderers);
      
      MarksRenderer marksRenderer = new MarksRenderer(true);
      renderers.add(marksRenderer);
      marksRenderer.addMark(new Mark("Everest", 
    		  Geodetic3D.fromDegrees(27.987778, 86.944444,0), AltitudeMode.RELATIVE_TO_GROUND, 
    		  6e7, 20, Color.red(), Color.black(), null, false, null));
      
      for (final Renderer renderer : renderers) {
         g3mBuilder.addRenderer(renderer);
      }

      final PeriodicalTask task = new PeriodicalTask(TimeInterval.fromSeconds(2), new GTask() {
         @Override
         public void run(final G3MContext context) {
            if (_animationFlag) {

               final int min = -50;
               final int max = 50;

               final Random r = new Random();
               final int i1 = r.nextInt((max - min) + 1) + min;

               _boxShape.setAnimatedScale(1, 1, i1);
               _circleShape.setPosition(new Geodetic3D(G3MGlob3Constants.SAN_FRANCISCO_POSITION.latitude().add(
                        Angle.fromDegrees(i1 / 5)), G3MGlob3Constants.SAN_FRANCISCO_POSITION.longitude().add(
                        Angle.fromDegrees(i1 / 5)), Math.abs(i1) * 5000));
            }
         }
      });
      g3mBuilder.addPeriodicalTask(task);

      final Geodetic3D position = new Geodetic3D(G3MGlob3Constants.SAN_FRANCISCO_POSITION, 5000000);
      final GInitializationTask initializationTask = new GInitializationTask() {
         @Override
         public void run(final G3MContext context) {
            _widgetAndroid.setAnimatedCameraPosition(position, TimeInterval.fromSeconds(5));
         }


         @Override
         public boolean isDone(final G3MContext context) {
            return true;
         }
      };
      g3mBuilder.setInitializationTask(initializationTask);

      _widgetAndroid = g3mBuilder.createWidget();
      final LinearLayout layout = (LinearLayout) findViewById(R.id.glob3);
      layout.addView(_widgetAndroid);


   }


   private void initializeToolbar() {


      final ToggleButton toggleBtn = new ToggleButton(this);
      toggleBtn.setTextOn(" ");
      toggleBtn.setTextOff(" ");
      toggleBtn.setChecked(true);
      toggleBtn.setBackgroundResource(R.drawable.play);
      toggleBtn.setOnCheckedChangeListener(new OnCheckedChangeListener() {

         @Override
         public void onCheckedChanged(final CompoundButton buttonView,
                                      final boolean isChecked) {
            if (!isChecked) {
               toggleBtn.setBackgroundResource(R.drawable.stop);
               _animationFlag = true;
            }
            else {
               toggleBtn.setBackgroundResource(R.drawable.play);
               _animationFlag = false;
            }
         }
      });
      final LinearLayout toolbarPanel = (LinearLayout) findViewById(R.id.toolbar);
      toolbarPanel.addView(toggleBtn);

      final TextView switchInstructionsText = new TextView(getApplicationContext());
      switchInstructionsText.setText(R.string.animation);
      switchInstructionsText.setTextSize(15);
      switchInstructionsText.setGravity(Gravity.CENTER_VERTICAL);
      switchInstructionsText.setTextColor(getResources().getColor(R.color.yellowIberia));
      toolbarPanel.addView(switchInstructionsText);

   }


   private void initializeShapes(final ArrayList<Renderer> renderers) {
      //Creating the shapes

      //BOX SHAPE RENDERER
      final Vector3D boxExtent = new Vector3D(100000, 100000, 100000);
      final float borderWidth = 2f;
      final Color surfaceColor = Color.fromRGBA(0, 0.7f, 0, 0.5f);
      final Color boderColor = Color.fromRGBA(0, 0.5f, 0, 0.75f);
      final Geodetic3D positionLA = new Geodetic3D(G3MGlob3Constants.LOS_ANGELES_POSITION, 10000);
      _boxShape = new BoxShape(positionLA, AltitudeMode.RELATIVE_TO_GROUND, boxExtent, borderWidth, surfaceColor, boderColor);
      //CIRCLE SHAPE

      final Color surfaceColorCircle = Color.fromRGBA(0.75f, 0.75f, 0, 0.75f);
      final Geodetic3D positionSF = new Geodetic3D(G3MGlob3Constants.SAN_FRANCISCO_POSITION, 10000);
      _circleShape = new CircleShape(positionSF, AltitudeMode.RELATIVE_TO_GROUND, 100000, surfaceColorCircle);


      final ShapesRenderer _shapesRenderer = new ShapesRenderer();
      _shapesRenderer.addShape(_boxShape);
      _shapesRenderer.addShape(_circleShape);


      renderers.add(_shapesRenderer);
   }


   @Override
   protected G3MWidget_Android getWidgetAndroid() {
      return _widgetAndroid;
   }
}
