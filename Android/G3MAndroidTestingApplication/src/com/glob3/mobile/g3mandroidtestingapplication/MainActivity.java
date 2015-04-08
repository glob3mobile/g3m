

package com.glob3.mobile.g3mandroidtestingapplication;

import org.glob3.mobile.generated.AltitudeMode;
import org.glob3.mobile.generated.Color;
import org.glob3.mobile.generated.ColumnLayoutImageBuilder;
import org.glob3.mobile.generated.DownloaderImageBuilder;
import org.glob3.mobile.generated.G3MContext;
import org.glob3.mobile.generated.GFont;
import org.glob3.mobile.generated.GInitializationTask;
import org.glob3.mobile.generated.Geodetic3D;
import org.glob3.mobile.generated.ICanvas;
import org.glob3.mobile.generated.IImage;
import org.glob3.mobile.generated.IImageDownloadListener;
import org.glob3.mobile.generated.IImageListener;
import org.glob3.mobile.generated.LabelImageBuilder;
import org.glob3.mobile.generated.LayerSet;
import org.glob3.mobile.generated.NonOverlappingMark;
import org.glob3.mobile.generated.NonOverlappingMarkTouchListener;
import org.glob3.mobile.generated.NonOverlappingMarksRenderer;
import org.glob3.mobile.generated.OSMLayer;
import org.glob3.mobile.generated.QuadShape;
import org.glob3.mobile.generated.ShapesRenderer;
import org.glob3.mobile.generated.TimeInterval;
import org.glob3.mobile.generated.URL;
import org.glob3.mobile.generated.Vector2F;
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

      _g3mWidget.setAnimatedCameraPosition(Geodetic3D.fromDegrees(28.034468668529083146, -15.904092315837871752, 1634079));

      //      // Buenos Aires, there we go!
      //      _g3mWidget.setAnimatedCameraPosition(Geodetic3D.fromDegrees(-34.615047738942699596, -58.4447233540403559, 35000));
   }


   private static NonOverlappingMark createMark(final Geodetic3D position) {
      final URL markBitmapURL = new URL("file:///g3m-marker.png");
      final URL anchorBitmapURL = new URL("file:///anchorWidget.png");

      return new NonOverlappingMark( //
               new DownloaderImageBuilder(markBitmapURL), //
               new DownloaderImageBuilder(anchorBitmapURL), //
               position);
   }


   private static NonOverlappingMark createMark(final String label,
                                                final Geodetic3D position) {
      final URL markBitmapURL = new URL("file:///g3m-marker.png");
      final URL anchorBitmapURL = new URL("file:///anchorWidget.png");

      final ColumnLayoutImageBuilder imageBuilderWidget = new ColumnLayoutImageBuilder( //
               new DownloaderImageBuilder(markBitmapURL), //
               new LabelImageBuilder(label, GFont.monospaced()) //
      );

      return new NonOverlappingMark( //
               imageBuilderWidget, //
               new DownloaderImageBuilder(anchorBitmapURL), //
               position);
   }


   private G3MWidget_Android createWidget() {
      final G3MBuilder_Android builder = new G3MBuilder_Android(this);

      final LayerSet layerSet = new LayerSet();
      layerSet.addLayer(new OSMLayer(TimeInterval.fromDays(30)));
      builder.getPlanetRendererBuilder().setLayerSet(layerSet);


      final NonOverlappingMarksRenderer renderer = new NonOverlappingMarksRenderer(30);
      builder.addRenderer(renderer);

      renderer.setTouchListener(new NonOverlappingMarkTouchListener() {
         @Override
         public boolean touchedMark(final NonOverlappingMark mark,
                                    final Vector2F touchedPixel) {
            System.out.println("Touched on pixel=" + touchedPixel + ", mark=" + mark);
            return true;
         }
      });

      renderer.addMark(createMark("Label #1", Geodetic3D.fromDegrees(28.131817, -15.440219, 0)));
      renderer.addMark(createMark(Geodetic3D.fromDegrees(28.947345, -13.523105, 0)));
      renderer.addMark(createMark(Geodetic3D.fromDegrees(28.473802, -13.859360, 0)));
      renderer.addMark(createMark(Geodetic3D.fromDegrees(28.467706, -16.251426, 0)));
      renderer.addMark(createMark(Geodetic3D.fromDegrees(28.701819, -17.762003, 0)));
      renderer.addMark(createMark(Geodetic3D.fromDegrees(28.086595, -17.105796, 0)));
      renderer.addMark(createMark(Geodetic3D.fromDegrees(27.810709, -17.917639, 0)));


      final boolean testCanvas = false;
      if (testCanvas) {
         final ShapesRenderer shapesRenderer = new ShapesRenderer();
         builder.addRenderer(shapesRenderer);


         builder.setInitializationTask(new GInitializationTask() {
            @Override
            public void run(final G3MContext context) {


               final IImageDownloadListener listener = new IImageDownloadListener() {
                  @Override
                  public void onError(final URL url) {
                  }


                  @Override
                  public void onDownload(final URL url,
                                         final IImage image,
                                         final boolean expired) {

                     final ICanvas canvas = context.getFactory().createCanvas();
                     final int width = 1024;
                     final int height = 1024;
                     canvas.initialize(width, height);

                     canvas.setFillColor(Color.fromRGBA(1f, 1f, 0f, 0.5f));
                     canvas.fillRectangle(0, 0, width, height);
                     canvas.setLineWidth(4);
                     canvas.setLineColor(Color.black());
                     canvas.strokeRectangle(0, 0, width, height);

                     final int steps = 8;
                     final float leftStep = (float) width / steps;
                     final float topStep = (float) height / steps;

                     canvas.setLineWidth(1);
                     canvas.setFillColor(Color.fromRGBA(0f, 0f, 0f, 0.75f));
                     for (int i = 1; i < steps; i++) {
                        canvas.fillRectangle(0, topStep * i, width, 1);
                        canvas.fillRectangle(leftStep * i, 0, 1, height);
                     }

                     canvas.setFont(GFont.monospaced());
                     canvas.setFillColor(Color.black());
                     //                  canvas.fillText("0,0", 0, 0);
                     //                  canvas.fillText("w,h", width, height);
                     for (int i = 0; i < steps; i++) {
                        canvas.fillText("Hellow World", leftStep * i, topStep * i);
                     }

                     //                  canvas.drawImage(image, width / 4, height / 4); // ok

                     canvas.drawImage(image, width / 8, height / 8); // ok
                     canvas.drawImage(image, (width / 8) * 3, height / 8, 0.5f); // ok

                     canvas.drawImage(image, width / 8, (height / 8) * 3, image.getWidth() * 2, image.getHeight() * 2); // ok
                     canvas.drawImage(image, (width / 8) * 3, (height / 8) * 3, image.getWidth() * 2, image.getHeight() * 2, 0.5f); //ok

                     // ok
                     canvas.drawImage(image, //
                              0, 0, image.getWidth(), image.getHeight(), //
                              (width / 8) * 5, (height / 8) * 5, image.getWidth() * 2, image.getHeight() * 2);
                     // ok
                     canvas.drawImage(image, //
                              0, 0, image.getWidth(), image.getHeight(), //
                              (width / 8) * 7, (height / 8) * 7, image.getWidth() * 2, image.getHeight() * 2, //
                              0.5f);


                     canvas.createImage(new IImageListener() {
                        @Override
                        public void imageCreated(final IImage canvasImage) {
                           final QuadShape quad = new QuadShape( //
                                    Geodetic3D.fromDegrees(-34.615047738942699596, -58.4447233540403559, 1000), //
                                    AltitudeMode.ABSOLUTE, //
                                    canvasImage, //
                                    canvasImage.getWidth() * 15.0f, //
                                    canvasImage.getHeight() * 15.0f, //
                                    true);

                           shapesRenderer.addShape(quad);
                        }
                     }, true);

                     canvas.dispose();

                     image.dispose();
                  }


                  @Override
                  public void onCanceledDownload(final URL url,
                                                 final IImage image,
                                                 final boolean expired) {
                  }


                  @Override
                  public void onCancel(final URL url) {
                  }
               };


               context.getDownloader().requestImage( //
                        new URL("file:///g3m-marker.png"), //
                        1, // priority, //
                        TimeInterval.zero(), //
                        false, //
                        listener, //
                        true);
            }


            @Override
            public boolean isDone(final G3MContext context) {
               return true;
            }
         });
      }


      return builder.createWidget();
   }
}
