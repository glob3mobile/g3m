

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


   private G3MWidget_Android createWidget() {
      final G3MBuilder_Android builder = new G3MBuilder_Android(this);


      final NonOverlappingMarksRenderer renderer = new NonOverlappingMarksRenderer(30);
      builder.addRenderer(renderer);

      final URL markBitmapURL = new URL("file:///g3m-marker.png");
      final URL anchorBitmapURL = new URL("file:///anchorWidget.png");

      final NonOverlappingMark mark1 = new NonOverlappingMark( //
               new DownloaderImageBuilder(markBitmapURL), //
               new DownloaderImageBuilder(anchorBitmapURL), //
               Geodetic3D.fromDegrees(28.131817, -15.440219, 0));
      renderer.addMark(mark1);

      final NonOverlappingMark mark2 = new NonOverlappingMark( //
               new DownloaderImageBuilder(markBitmapURL), //
               new DownloaderImageBuilder(anchorBitmapURL), //
               Geodetic3D.fromDegrees(28.947345, -13.523105, 0));
      renderer.addMark(mark2);

      final NonOverlappingMark mark3 = new NonOverlappingMark( //
               new DownloaderImageBuilder(markBitmapURL), //
               new DownloaderImageBuilder(anchorBitmapURL), //
               Geodetic3D.fromDegrees(28.473802, -13.859360, 0));
      renderer.addMark(mark3);

      final NonOverlappingMark mark4 = new NonOverlappingMark( //
               new DownloaderImageBuilder(markBitmapURL), //
               new DownloaderImageBuilder(anchorBitmapURL), //
               Geodetic3D.fromDegrees(28.467706, -16.251426, 0));
      renderer.addMark(mark4);

      final NonOverlappingMark mark5 = new NonOverlappingMark( //
               new DownloaderImageBuilder(markBitmapURL), //
               new DownloaderImageBuilder(anchorBitmapURL), //
               Geodetic3D.fromDegrees(28.701819, -17.762003, 0));
      renderer.addMark(mark5);

      final NonOverlappingMark mark6 = new NonOverlappingMark( //
               new DownloaderImageBuilder(markBitmapURL), //
               new DownloaderImageBuilder(anchorBitmapURL), //
               Geodetic3D.fromDegrees(28.086595, -17.105796, 0));
      renderer.addMark(mark6);

      final NonOverlappingMark mark7 = new NonOverlappingMark( //
               new DownloaderImageBuilder(markBitmapURL), //
               new DownloaderImageBuilder(anchorBitmapURL), //
               Geodetic3D.fromDegrees(27.810709, -17.917639, 0));
      renderer.addMark(mark7);


      return builder.createWidget();
   }


}
