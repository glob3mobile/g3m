

package com.glob3.mobile.g3mandroidtestingapplication;

import org.glob3.mobile.generated.DownloaderImageBuilder;
import org.glob3.mobile.generated.HUDQuadWidget;
import org.glob3.mobile.generated.HUDRelativePosition;
import org.glob3.mobile.generated.HUDRelativeSize;
import org.glob3.mobile.generated.HUDRenderer;
import org.glob3.mobile.generated.LayerSet;
import org.glob3.mobile.generated.MapQuestLayer;
import org.glob3.mobile.generated.TimeInterval;
import org.glob3.mobile.generated.URL;
import org.glob3.mobile.specific.G3MBuilder_Android;
import org.glob3.mobile.specific.G3MWidget_Android;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;


public class MainActivity
         extends
            Activity {

   private G3MWidget_Android _g3mWidget;
   private RelativeLayout    _placeHolder;


   @Override
   protected void onCreate(final Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);

      requestWindowFeature(Window.FEATURE_NO_TITLE);
      getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

      setContentView(R.layout.activity_main);
      final G3MBuilder_Android builder = new G3MBuilder_Android(this);
      // builder.getPlanetRendererBuilder().setRenderDebug(true);


      final LayerSet layerSet = new LayerSet();
      layerSet.addLayer(MapQuestLayer.newOSM(TimeInterval.fromDays(30)));
      builder.getPlanetRendererBuilder().setLayerSet(layerSet);

      final HUDRenderer hudRenderer = new HUDRenderer();
      builder.setHUDRenderer(hudRenderer);
      createHUD(hudRenderer);

      _g3mWidget = builder.createWidget();
      _placeHolder = (RelativeLayout) findViewById(R.id.g3mWidgetHolder);
      _placeHolder.addView(_g3mWidget);
   }


   private void createHUD(final HUDRenderer hudRenderer) {
      final DownloaderImageBuilder imageBuilder = new DownloaderImageBuilder(new URL("file:///altitude_ladder.png"));

      final float vertVis = 0.1f;
      final float aspect = 85f / 5100f;

      final HUDRelativePosition x = new HUDRelativePosition( //
               0.8f, //
               HUDRelativePosition.Anchor.VIEWPORT_WIDTH, //
               HUDRelativePosition.Align.RIGHT);

      final HUDRelativePosition y = new HUDRelativePosition( //
               0.5f, //
               HUDRelativePosition.Anchor.VIEWPORT_HEIGTH, //
               HUDRelativePosition.Align.MIDDLE);

      final HUDRelativeSize width = new HUDRelativeSize( //
               10f * aspect, //
               HUDRelativeSize.Reference.VIEWPORT_MIN_AXIS);

      final HUDRelativeSize height = new HUDRelativeSize( //
               0.8f, //
               HUDRelativeSize.Reference.VIEWPORT_MIN_AXIS);

      final HUDQuadWidget altRuler = new HUDQuadWidget(imageBuilder, x, y, width, height);

      altRuler.setTexCoordsScale(1, vertVis);
      hudRenderer.addWidget(altRuler);
   }


   @Override
   public boolean onCreateOptionsMenu(final Menu menu) {
      // Inflate the menu; this adds items to the action bar if it is present.
      getMenuInflater().inflate(R.menu.main, menu);
      return true;
   }

}
