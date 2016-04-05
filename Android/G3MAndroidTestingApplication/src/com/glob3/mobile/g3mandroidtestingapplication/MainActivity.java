

package com.glob3.mobile.g3mandroidtestingapplication;

import org.glob3.mobile.generated.AltitudeMode;
import org.glob3.mobile.generated.Angle;
import org.glob3.mobile.generated.BingMapType;
import org.glob3.mobile.generated.BingMapsLayer;
import org.glob3.mobile.generated.Color;
import org.glob3.mobile.generated.ColumnLayoutImageBuilder;
import org.glob3.mobile.generated.DirectMesh;
import org.glob3.mobile.generated.DownloaderImageBuilder;
import org.glob3.mobile.generated.FloatBufferBuilderFromGeodetic;
import org.glob3.mobile.generated.G3MContext;
import org.glob3.mobile.generated.GFont;
import org.glob3.mobile.generated.GInitializationTask;
import org.glob3.mobile.generated.GLPrimitive;
import org.glob3.mobile.generated.Geodetic2D;
import org.glob3.mobile.generated.Geodetic3D;
import org.glob3.mobile.generated.ICanvas;
import org.glob3.mobile.generated.IImage;
import org.glob3.mobile.generated.IImageDownloadListener;
import org.glob3.mobile.generated.IImageListener;
import org.glob3.mobile.generated.LabelImageBuilder;
import org.glob3.mobile.generated.LayerSet;
import org.glob3.mobile.generated.MeshRenderer;
import org.glob3.mobile.generated.NonOverlappingMark;
import org.glob3.mobile.generated.NonOverlappingMarkTouchListener;
import org.glob3.mobile.generated.NonOverlappingMarksRenderer;
import org.glob3.mobile.generated.OSMLayer;
import org.glob3.mobile.generated.Planet;
import org.glob3.mobile.generated.PyramidElevationDataProvider;
import org.glob3.mobile.generated.QuadShape;
import org.glob3.mobile.generated.Sector;
import org.glob3.mobile.generated.ShapesRenderer;
import org.glob3.mobile.generated.SingleBilElevationDataProvider;
import org.glob3.mobile.generated.SphericalPlanet;
import org.glob3.mobile.generated.TimeInterval;
import org.glob3.mobile.generated.URL;
import org.glob3.mobile.generated.Vector2F;
import org.glob3.mobile.generated.Vector2I;
import org.glob3.mobile.specific.G3MBuilder_Android;
import org.glob3.mobile.specific.G3MWidget_Android;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;


public class MainActivity
extends
Activity {

   private G3MWidget_Android _g3mWidget;
   private Geodetic3D position = null;
   private Angle pitch = null, heading = null;

   @Override
   protected void onCreate(final Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);

      /*requestWindowFeature(Window.FEATURE_NO_TITLE);
      getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

      setContentView(R.layout.activity_main);

      // _g3mWidget = createWidget();
      _g3mWidget = createWidgetVR();


      final RelativeLayout placeHolder = (RelativeLayout) findViewById(R.id.g3mWidgetHolder);

      placeHolder.addView(_g3mWidget);

      //_g3mWidget.setAnimatedCameraPosition(Geodetic3D.fromDegrees(28.034468668529083146, -15.904092315837871752, 1634079));

      //      // Buenos Aires, there we go!
      //      _g3mWidget.setAnimatedCameraPosition(Geodetic3D.fromDegrees(-34.615047738942699596, -58.4447233540403559, 35000));*/
   }
   
   @Override
   protected void onDestroy(){
	   clearGlobe();
	   super.onDestroy();
   }
   
   private void generateGlobe(int layer, boolean wireframe, float vertEx){
	   clearGlobe();
	   _g3mWidget = createWidgetPyramidElevations(layer, wireframe, vertEx);
	   if (position != null) {
		   _g3mWidget.setCameraPosition(position);
		   _g3mWidget.setCameraPitch(pitch);
		   _g3mWidget.setCameraHeading(heading);
	   }
       final RelativeLayout placeHolder = (RelativeLayout) findViewById(R.id.g3mWidgetHolder);
       placeHolder.addView(_g3mWidget);
   }
   
   private void clearGlobe(){
	   if (_g3mWidget != null){
		   position = _g3mWidget.getNextCamera().getGeodeticPosition();
		   pitch = _g3mWidget.getNextCamera().getPitch();
		   heading = _g3mWidget.getNextCamera().getHeading();
		   final RelativeLayout placeHolder = (RelativeLayout) findViewById(R.id.g3mWidgetHolder);
		   placeHolder.removeView(_g3mWidget);
		   _g3mWidget.onDestroy();
		   _g3mWidget = null;
	       
	   }
   }
   
   private void generateLoaderDialog(){
		
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final Dialog dialog = new Dialog(this);
		final LinearLayout vi = (LinearLayout) inflater.inflate(R.layout.loader_dialog, null);
		
		Button ok = (Button) vi.findViewById(R.id.ok_button);
		Button cancel = (Button) vi.findViewById(R.id.cancel_button);
		
		cancel.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}			
		});
		
		ok.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Spinner size = (Spinner) vi.findViewById(R.id.size_spinner);
				Spinner wireframe = (Spinner) vi.findViewById(R.id.wireframe_spinner);
				EditText vertExEditText = (EditText) vi.findViewById(R.id.vertex_edittext);
				int sizePosition = size.getSelectedItemPosition();
				int wireFramePosition = wireframe.getSelectedItemPosition();
				float vertEx = -1;
				try {
					vertEx = Float.parseFloat(vertExEditText.getText().toString());
				} catch (Exception e) {}
				if (sizePosition == 0 || wireFramePosition == 0 || vertEx < 0) {
					Toast.makeText(getApplicationContext(), "Error: you should select all options", Toast.LENGTH_LONG).show();;
					return;
				}
				dialog.dismiss();
				if (wireFramePosition == 1) generateGlobe(sizePosition-1,false,vertEx);
				else generateGlobe(sizePosition-1,true,vertEx);
			}
			
		});
		
		dialog.setContentView(vi);
		dialog.setCancelable(false);
		dialog.show();
	}
  
  private void generateGotoDialog(){
	   if (_g3mWidget == null) return;
	   
	   LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final Dialog dialog = new Dialog(this);
		final LinearLayout vi = (LinearLayout) inflater.inflate(R.layout.goto_dialog, null);
		
		Button ok = (Button) vi.findViewById(R.id.ok_button);
		Button cancel = (Button) vi.findViewById(R.id.cancel_button);
		
		cancel.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}			
		});
		
		ok.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				EditText latEdit = (EditText) vi.findViewById(R.id.lat_edit);
				EditText lonEdit = (EditText) vi.findViewById(R.id.lon_edit);
				EditText hgtEdit = (EditText) vi.findViewById(R.id.hgt_edit);
				EditText pitchEdit = (EditText) vi.findViewById(R.id.pitch_edit);
				
				try {
					double lat = Double.parseDouble(latEdit.getText().toString());
					double lon = Double.parseDouble(lonEdit.getText().toString());
					double hgt = Double.parseDouble(hgtEdit.getText().toString());
					double pitch = Double.parseDouble(pitchEdit.getText().toString());
					
					_g3mWidget.setCameraPitch(Angle.fromDegrees(pitch));
					_g3mWidget.setCameraPosition(Geodetic3D.fromDegrees(lat,lon,hgt));
				}
				catch (Exception E) {
					Toast.makeText(getApplicationContext(), "Error: all fields should be numbers", Toast.LENGTH_LONG).show();;
					return;
				}
				dialog.dismiss();
			}
			
		});
		
		dialog.setContentView(vi);
		dialog.setCancelable(false);
		dialog.show();
  }
  
  @Override
  public boolean onCreateOptionsMenu (Menu menu){
	   MenuInflater inflater = getMenuInflater();
	   inflater.inflate(R.menu.main, menu);
	   return true;
  }
  
  @Override
  public boolean onOptionsItemSelected (MenuItem item){
	   switch (item.getItemId()){
	   		case R.id.load: {
	   			generateLoaderDialog();
	   			break;
	   		}
	   		case R.id.clear:
	   			clearGlobe();
	   			break;
	   		case R.id.go:
	   			generateGotoDialog();
	   			break;
	   }
	   return true;
  }
  
  private G3MWidget_Android createWidgetPyramidElevations(int layer, boolean wireframe, float vertEx){
	   final G3MBuilder_Android builder = new G3MBuilder_Android(this);	  
	   final Planet planet = SphericalPlanet.createEarth();
	   builder.setPlanet(planet);
	   LayerSet ls = new LayerSet();
	   ls.addLayer(new BingMapsLayer(BingMapType.Aerial(),
		        "AnU5uta7s5ql_HTrRZcPLI4_zotvNefEeSxIClF1Jf7eS-mLig1jluUdCoecV7jc",
		        TimeInterval.fromDays(30)));
	   MeshRenderer _meshRenderer = new MeshRenderer();
	   builder.addRenderer(_meshRenderer);
	   builder.getPlanetRendererBuilder().setLayerSet(ls);
	   //builder.getPlanetRendererBuilder().setIncrementalTileQuality(true);
	   builder.getPlanetRendererBuilder().setRenderDebug(wireframe);
	   builder.getPlanetRendererBuilder().setVerticalExaggeration(vertEx);
	   String layerServer;
	   Sector layerSector = Sector.fullSphere();
	   switch (layer) {
	   		case 0:
	   			layerServer = "http://193.145.147.50:8080/DemoElevs/elevs/fix-16/";
	   			break;
	   		case 1:
	   			layerServer = "http://193.145.147.50:8080/DemoElevs/elevs/var-16/";
	   			break;
	   		case 2:
	   			layerServer = "http://193.145.147.50:8080/DemoElevs/elevs/fix-euro-16/";
	   			layerSector = Sector.fromDegrees(34,-10,70,52);
	   			break;
	   		case 3:
	   			layerServer = "http://193.145.147.50:8080/DemoElevs/elevs/var-euro-16/";
	   			//layerServer = "http://www.elnublo.net/temporal/var-euro-16/";
	   			layerSector = Sector.fromDegrees(34,-10,70,52);
	   			break;
	   		default:
	   			layerServer = "";
	   }
	   
	   builder.getPlanetRendererBuilder().setElevationDataProvider(new PyramidElevationDataProvider(layerServer,layerSector));
	   /*builder.getPlanetRendererBuilder().setElevationDataProvider(
			   new SingleBilElevationDataProvider(new URL("file:///full-earth-2048x1024.bil",false),
					   Sector.fullSphere(),
					   new Vector2I(2048,1024)));*/
	   
	   boolean showPrimarySectors = wireframe;
	   if (showPrimarySectors){
		   addSectorMesh(_meshRenderer, Sector.fromDegrees(50, -180, 90, -90), planet);
		   addSectorMesh(_meshRenderer, Sector.fromDegrees(50, -90, 90, 0), planet);
		   addSectorMesh(_meshRenderer, Sector.fromDegrees(50, 0, 90, 90), planet);
		   addSectorMesh(_meshRenderer, Sector.fromDegrees(50, 90, 90, 180), planet);
		  
		   addSectorMesh(_meshRenderer, Sector.fromDegrees(0, -180, 50, -90), planet);
		   addSectorMesh(_meshRenderer, Sector.fromDegrees(0, -90, 50, 0), planet);
		   addSectorMesh(_meshRenderer, Sector.fromDegrees(0, 0, 50, 90), planet);
		   addSectorMesh(_meshRenderer, Sector.fromDegrees(0, 90, 50, 180), planet);
		   
		   addSectorMesh(_meshRenderer, Sector.fromDegrees(-50, -180, 0, -90), planet);
		   addSectorMesh(_meshRenderer, Sector.fromDegrees(-50, -90, 0, 0), planet);
		   addSectorMesh(_meshRenderer, Sector.fromDegrees(-50, 0, 0, 90), planet);
		   addSectorMesh(_meshRenderer, Sector.fromDegrees(-50, 90, 0, 180), planet);
		   
		   addSectorMesh(_meshRenderer, Sector.fromDegrees(-90, -180, -50, -90), planet);
		   addSectorMesh(_meshRenderer, Sector.fromDegrees(-90, -90, -50, 0), planet);
		   addSectorMesh(_meshRenderer, Sector.fromDegrees(-90, 0, -50, 90), planet);
		   addSectorMesh(_meshRenderer, Sector.fromDegrees(-90, 90, -50, 180), planet);
	   
	   }
 
	   G3MWidget_Android _widget = builder.createWidget();
	   //_widget.setCameraPosition(Geodetic3D.fromDegrees(36.25,-111.75, 100000));
	   return _widget;
  }
  
  private void addSectorMesh(MeshRenderer renderer, Sector sector, Planet planet){
	   final double POINT_DIV = 100;
	   
	   FloatBufferBuilderFromGeodetic fbb = FloatBufferBuilderFromGeodetic.builderWithFirstVertexAsCenter(
			   planet);
	   fbb.add(sector._upper, 5);
	   //Delta instruction here
	   if (sector._upper._latitude._degrees != 90){
		   double delta = (sector._upper._longitude._radians - sector._lower._longitude._radians) / POINT_DIV;
		   double lonRads = sector._upper._longitude._radians;
		   for (int i=0; i<POINT_DIV; i++){
			   lonRads -= delta;
			   fbb.add(sector._upper._latitude,Geodetic2D.fromRadians(sector._upper._latitude._radians,lonRads)._longitude, 5);
		   }
	   }
	   else fbb.add(sector._upper._latitude, sector._lower._longitude, 5);
	   
	   double delta = (sector._upper._latitude._radians - sector._lower._latitude._radians) / POINT_DIV;
	   double latRads = sector._upper._latitude._radians;
	   for (int i=0; i<POINT_DIV; i++){
		   latRads -= delta;
		   fbb.add(Geodetic2D.fromRadians(latRads,sector._lower._longitude._radians)._latitude,sector._lower._longitude, 5);
	   } 
	   
	   //Delta instruction here
	   if (sector._lower._latitude._degrees != -90){
		   delta = (sector._upper._longitude._radians - sector._lower._longitude._radians) / POINT_DIV;
		   double lonRads = sector._lower._longitude._radians;
		   for (int i=0; i<POINT_DIV; i++){
			   lonRads += delta;
			   fbb.add(sector._lower._latitude,Geodetic2D.fromRadians(sector._lower._latitude._radians,lonRads)._longitude, 5);
		   }
	   }
	   else fbb.add(sector._lower._latitude,sector._upper._longitude, 5);
	   
	   delta = (sector._upper._latitude._radians - sector._lower._latitude._radians) / POINT_DIV;
	   latRads = sector._lower._latitude._radians;
	   for (int i=0; i<POINT_DIV; i++){
		   latRads += delta;
		   fbb.add(Geodetic2D.fromRadians(latRads,sector._upper._longitude._radians)._latitude,sector._upper._longitude, 5);
	   } 
	   
	   renderer.addMesh(new DirectMesh(GLPrimitive.lineStrip(), true, fbb.getCenter(), fbb.create(), 6.0f, 1.0f, 
			   Color.yellow(), null, 0.0f, false));
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


   /*private G3MWidget_Android createWidgetVR() {
      final G3MBuilder_Android builder = new G3MBuilder_Android(this);

      final LayerSet layerSet = new LayerSet();
      layerSet.addLayer(new OSMLayer(TimeInterval.fromDays(30)));
      builder.getPlanetRendererBuilder().setLayerSet(layerSet);

      final CameraRenderer cr = new CameraRenderer();
      cr.addHandler(new DeviceAttitudeCameraHandler(true));
      builder.setCameraRenderer(cr);

      return builder.createWidget();
   }*/


   private G3MWidget_Android createWidget() {
      final G3MBuilder_Android builder = new G3MBuilder_Android(this);

      final LayerSet layerSet = new LayerSet();
      layerSet.addLayer(new OSMLayer(TimeInterval.fromDays(30)));
      builder.getPlanetRendererBuilder().setLayerSet(layerSet);
      builder.getPlanetRendererBuilder().setRenderDebug(true);

//
//      final NonOverlappingMarksRenderer renderer = new NonOverlappingMarksRenderer(30);
//      builder.addRenderer(renderer);
//
//      renderer.setTouchListener(new NonOverlappingMarkTouchListener() {
//         @Override
//         public boolean touchedMark(final NonOverlappingMark mark,
//                                    final Vector2F touchedPixel) {
//            System.out.println("Touched on pixel=" + touchedPixel + ", mark=" + mark);
//            return true;
//         }
//      });
//
//      renderer.addMark(createMark("Label #1", Geodetic3D.fromDegrees(28.131817, -15.440219, 0)));
//      renderer.addMark(createMark(Geodetic3D.fromDegrees(28.947345, -13.523105, 0)));
//      renderer.addMark(createMark(Geodetic3D.fromDegrees(28.473802, -13.859360, 0)));
//      renderer.addMark(createMark(Geodetic3D.fromDegrees(28.467706, -16.251426, 0)));
//      renderer.addMark(createMark(Geodetic3D.fromDegrees(28.701819, -17.762003, 0)));
//      renderer.addMark(createMark(Geodetic3D.fromDegrees(28.086595, -17.105796, 0)));
//      renderer.addMark(createMark(Geodetic3D.fromDegrees(27.810709, -17.917639, 0)));
//
//
//      final boolean testCanvas = false;
//      if (testCanvas) {
//         final ShapesRenderer shapesRenderer = new ShapesRenderer();
//         builder.addRenderer(shapesRenderer);
//
//
//         builder.setInitializationTask(new GInitializationTask() {
//            @Override
//            public void run(final G3MContext context) {
//
//
//               final IImageDownloadListener listener = new IImageDownloadListener() {
//                  @Override
//                  public void onError(final URL url) {
//                  }
//
//
//                  @Override
//                  public void onDownload(final URL url,
//                                         final IImage image,
//                                         final boolean expired) {
//
//                     final ICanvas canvas = context.getFactory().createCanvas();
//                     final int width = 1024;
//                     final int height = 1024;
//                     canvas.initialize(width, height);
//
//                     canvas.setFillColor(Color.fromRGBA(1f, 1f, 0f, 0.5f));
//                     canvas.fillRectangle(0, 0, width, height);
//                     canvas.setLineWidth(4);
//                     canvas.setLineColor(Color.black());
//                     canvas.strokeRectangle(0, 0, width, height);
//
//                     final int steps = 8;
//                     final float leftStep = (float) width / steps;
//                     final float topStep = (float) height / steps;
//
//                     canvas.setLineWidth(1);
//                     canvas.setFillColor(Color.fromRGBA(0f, 0f, 0f, 0.75f));
//                     for (int i = 1; i < steps; i++) {
//                        canvas.fillRectangle(0, topStep * i, width, 1);
//                        canvas.fillRectangle(leftStep * i, 0, 1, height);
//                     }
//
//                     canvas.setFont(GFont.monospaced());
//                     canvas.setFillColor(Color.black());
//                     //                  canvas.fillText("0,0", 0, 0);
//                     //                  canvas.fillText("w,h", width, height);
//                     for (int i = 0; i < steps; i++) {
//                        canvas.fillText("Hellow World", leftStep * i, topStep * i);
//                     }
//
//                     //                  canvas.drawImage(image, width / 4, height / 4); // ok
//
//                     canvas.drawImage(image, width / 8, height / 8); // ok
//                     canvas.drawImage(image, (width / 8) * 3, height / 8, 0.5f); // ok
//
//                     canvas.drawImage(image, width / 8, (height / 8) * 3, image.getWidth() * 2, image.getHeight() * 2); // ok
//                     canvas.drawImage(image, (width / 8) * 3, (height / 8) * 3, image.getWidth() * 2, image.getHeight() * 2, 0.5f); //ok
//
//                     // ok
//                     canvas.drawImage(image, //
//                              0, 0, image.getWidth(), image.getHeight(), //
//                              (width / 8) * 5, (height / 8) * 5, image.getWidth() * 2, image.getHeight() * 2);
//                     // ok
//                     canvas.drawImage(image, //
//                              0, 0, image.getWidth(), image.getHeight(), //
//                              (width / 8) * 7, (height / 8) * 7, image.getWidth() * 2, image.getHeight() * 2, //
//                              0.5f);
//
//
//                     canvas.createImage(new IImageListener() {
//                        @Override
//                        public void imageCreated(final IImage canvasImage) {
//                           final QuadShape quad = new QuadShape( //
//                                    Geodetic3D.fromDegrees(-34.615047738942699596, -58.4447233540403559, 1000), //
//                                    AltitudeMode.ABSOLUTE, //
//                                    canvasImage, //
//                                    canvasImage.getWidth() * 15.0f, //
//                                    canvasImage.getHeight() * 15.0f, //
//                                    true);
//
//                           shapesRenderer.addShape(quad);
//                        }
//                     }, true);
//
//                     canvas.dispose();
//
//                     image.dispose();
//                  }
//
//
//                  @Override
//                  public void onCanceledDownload(final URL url,
//                                                 final IImage image,
//                                                 final boolean expired) {
//                  }
//
//
//                  @Override
//                  public void onCancel(final URL url) {
//                  }
//               };
//
//
//               context.getDownloader().requestImage( //
//                        new URL("file:///g3m-marker.png"), //
//                        1, // priority, //
//                        TimeInterval.zero(), //
//                        false, //
//                        listener, //
//                        true);
//            }
//
//
//            @Override
//            public boolean isDone(final G3MContext context) {
//               return true;
//            }
//         });
//      }


      return builder.createWidget();
   }
}
