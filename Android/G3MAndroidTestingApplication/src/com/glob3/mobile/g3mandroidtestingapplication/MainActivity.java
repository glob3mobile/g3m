

package com.glob3.mobile.g3mandroidtestingapplication;

import org.glob3.mobile.generated.DownloaderImageBuilder;
import org.glob3.mobile.generated.Geodetic3D;
import org.glob3.mobile.generated.NonOverlappingMark;
import org.glob3.mobile.generated.NonOverlappingMarksRenderer;
import org.glob3.mobile.generated.URL;
import org.glob3.mobile.specific.G3MBuilder_Android;
import org.glob3.mobile.specific.G3MWidget_Android;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;


public class MainActivity
         extends
            Activity {

   private G3MWidget_Android _g3mWidget;


   @Override
   protected void onCreate(final Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);

      requestWindowFeature(Window.FEATURE_NO_TITLE);
      getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

      setContentView(R.layout.activity_main);

      _g3mWidget = createWidget();

      final RelativeLayout placeHolder = (RelativeLayout) findViewById(R.id.g3mWidgetHolder);

      placeHolder.addView(_g3mWidget);

      // Canarias, here we go!
      _g3mWidget.setAnimatedCameraPosition(Geodetic3D.fromDegrees(28.034468668529083146, -15.904092315837871752, 1634079));
   }


   private static NonOverlappingMark createMark(final Geodetic3D position) {
      final URL markBitmapURL = new URL("file:///g3m-marker.png");
      final URL anchorBitmapURL = new URL("file:///anchorWidget.png");

      return new NonOverlappingMark( //
               new DownloaderImageBuilder(markBitmapURL), //
               new DownloaderImageBuilder(anchorBitmapURL), //
               position);


      //      final MarkWidgetTouchListener touchListener = null;
      //      final float springLengthInPixels = 100.0f;
      //      final float springK = 70.0f;
      //      final float minSpringLength = 0.0f;
      //      final float maxSpringLength = 0.0f;
      //      final float electricCharge = 3000.0f;
      //      final float anchorElectricCharge = 2000.0f;
      //      //final float minWidgetSpeedInPixelsPerSecond = 5.0f;
      //      final float minWidgetSpeedInPixelsPerSecond = 50.0f;
      //      final float maxWidgetSpeedInPixelsPerSecond = 1000.0f;
      //      final float resistanceFactor = 0.95f;
      //
      //
      //      return new NonOverlappingMark( //
      //               new DownloaderImageBuilder(markBitmapURL), //
      //               new DownloaderImageBuilder(anchorBitmapURL), //
      //               position, //
      //               touchListener, //
      //               springLengthInPixels, //
      //               springK, //
      //               minSpringLength, //
      //               maxSpringLength, //
      //               electricCharge, //
      //               anchorElectricCharge, //
      //               minWidgetSpeedInPixelsPerSecond, //
      //               maxWidgetSpeedInPixelsPerSecond, //
      //               resistanceFactor //
      //      );

   }


   private G3MWidget_Android createWidget() {
      final G3MBuilder_Android builder = new G3MBuilder_Android(this);


      final NonOverlappingMarksRenderer renderer = new NonOverlappingMarksRenderer(30);
      builder.addRenderer(renderer);

      renderer.addMark(createMark(Geodetic3D.fromDegrees(28.131817, -15.440219, 0)));
      renderer.addMark(createMark(Geodetic3D.fromDegrees(28.947345, -13.523105, 0)));
      renderer.addMark(createMark(Geodetic3D.fromDegrees(28.473802, -13.859360, 0)));
      renderer.addMark(createMark(Geodetic3D.fromDegrees(28.467706, -16.251426, 0)));
      renderer.addMark(createMark(Geodetic3D.fromDegrees(28.701819, -17.762003, 0)));
      renderer.addMark(createMark(Geodetic3D.fromDegrees(28.086595, -17.105796, 0)));
      renderer.addMark(createMark(Geodetic3D.fromDegrees(27.810709, -17.917639, 0)));


      return builder.createWidget();
   }


}
