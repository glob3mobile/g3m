package org.glob3.mobile.client;

import org.glob3.mobile.generated.AltitudeMode;
import org.glob3.mobile.generated.BingMapType;
import org.glob3.mobile.generated.BingMapsLayer;
import org.glob3.mobile.generated.CameraDoubleDragHandler;
import org.glob3.mobile.generated.CameraDoubleTapHandler;
import org.glob3.mobile.generated.CameraRenderer;
import org.glob3.mobile.generated.CameraRotationHandler;
import org.glob3.mobile.generated.CameraSingleDragHandler;
import org.glob3.mobile.generated.Color;
import org.glob3.mobile.generated.ColumnLayoutImageBuilder;
import org.glob3.mobile.generated.DeviceAttitudeCameraConstrainer;
import org.glob3.mobile.generated.DownloaderImageBuilder;
import org.glob3.mobile.generated.G3MContext;
import org.glob3.mobile.generated.GFont;
import org.glob3.mobile.generated.GInitializationTask;
import org.glob3.mobile.generated.GTask;
import org.glob3.mobile.generated.Geodetic3D;
import org.glob3.mobile.generated.ICanvas;
import org.glob3.mobile.generated.IDeviceLocation;
import org.glob3.mobile.generated.IImage;
import org.glob3.mobile.generated.IImageDownloadListener;
import org.glob3.mobile.generated.IImageListener;
import org.glob3.mobile.generated.ILogger;
import org.glob3.mobile.generated.LabelImageBuilder;
import org.glob3.mobile.generated.LayerSet;
import org.glob3.mobile.generated.MapQuestLayer;
import org.glob3.mobile.generated.NASAElevationDataProvider;
import org.glob3.mobile.generated.NonOverlappingMark;
import org.glob3.mobile.generated.NonOverlappingMarksRenderer;
import org.glob3.mobile.generated.OSMLayer;
import org.glob3.mobile.generated.QuadShape;
import org.glob3.mobile.generated.ShapesRenderer;
import org.glob3.mobile.generated.TimeInterval;
import org.glob3.mobile.generated.URL;
import org.glob3.mobile.specific.Downloader_WebGL;
import org.glob3.mobile.specific.G3MBuilder_WebGL;
import org.glob3.mobile.specific.G3MWidget_WebGL;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;
//import org.glob3.mobile.generated.GEORasterLineSymbol;
//import org.glob3.mobile.generated.GEORasterPolygonSymbol;

public class G3MWebGLTestingApplication implements EntryPoint {

	private static final String _g3mWidgetHolderId = "g3mWidgetHolder";
	private G3MWidget_WebGL _g3mWidget = null;

	private native void runUserPlugin() /*-{
		$wnd.onLoadG3M();
	}-*/;

	@Override
	public void onModuleLoad() {
		final Panel g3mWidgetHolder = RootPanel.get(_g3mWidgetHolderId);

<<<<<<< HEAD
		_g3mWidget = createWidgetStreamingElevations();
=======
		_g3mWidget = createWidgetVR();
>>>>>>> devAttitude
		g3mWidgetHolder.add(_g3mWidget);

		// // Buenos Aires, there we go!
		// _g3mWidget.setAnimatedCameraPosition(Geodetic3D.fromDegrees(-34.615047738942699596,
		// -58.4447233540403559, 35000));

		// Canarias
		_g3mWidget.setAnimatedCameraPosition(Geodetic3D.fromDegrees(
				28.034468668529083146, -15.904092315837871752, 1634079));
	}

<<<<<<< HEAD
	private G3MWidget_WebGL createWidgetStreamingElevations() {
		final G3MBuilder_WebGL builder = new G3MBuilder_WebGL();

		final LayerSet layerSet = new LayerSet();
		// layerSet.addLayer(MapQuestLayer.newOSM(TimeInterval.fromDays(30)));

		layerSet.addLayer(new BingMapsLayer(
				BingMapType.AerialWithLabels(),
				"AnU5uta7s5ql_HTrRZcPLI4_zotvNefEeSxIClF1Jf7eS-mLig1jluUdCoecV7jc",
				TimeInterval.fromDays(30)));

		builder.getPlanetRendererBuilder().setLayerSet(layerSet);

		final String proxy = "/war/proxy.php?url=";
		builder.setDownloader(new Downloader_WebGL( //
				8, // maxConcurrentOperationCount
				10, // delayMillis
				proxy));

		NASAElevationDataProvider edp = new NASAElevationDataProvider();

		builder.getPlanetRendererBuilder().setElevationDataProvider(edp);

		builder.getPlanetRendererBuilder().setVerticalExaggeration(2.5f);
=======
	private G3MWidget_WebGL createWidgetVR() {
		final G3MBuilder_WebGL builder = new G3MBuilder_WebGL();

		final LayerSet layerSet = new LayerSet();
		layerSet.addLayer(new OSMLayer(TimeInterval.fromDays(30)));
		builder.getPlanetRendererBuilder().setLayerSet(layerSet);

		builder.addCameraConstraint(new DeviceAttitudeCameraConstrainer());

		final String proxy = "proxy.php?url=";
		builder.setDownloader(new Downloader_WebGL(8, 10, proxy));
>>>>>>> devAttitude

		return builder.createWidget();
	}

	private static NonOverlappingMark createMark(final Geodetic3D position) {
		final URL markBitmapURL = new URL("/g3m-marker.png");
		final URL anchorBitmapURL = new URL("/anchorWidget.png");

		return new NonOverlappingMark( //
				new DownloaderImageBuilder(markBitmapURL), //
				new DownloaderImageBuilder(anchorBitmapURL), //
				position);
	}

	private static NonOverlappingMark createMark(final String label,
			final Geodetic3D position) {
		final URL markBitmapURL = new URL("/g3m-marker.png");
		final URL anchorBitmapURL = new URL("/anchorWidget.png");

		final ColumnLayoutImageBuilder imageBuilderWidget = new ColumnLayoutImageBuilder( //
				new DownloaderImageBuilder(markBitmapURL), //
				new LabelImageBuilder(label, GFont.monospaced()) //
		);

		return new NonOverlappingMark( //
				imageBuilderWidget, //
				new DownloaderImageBuilder(anchorBitmapURL), //
				position);
	}

	private static G3MWidget_WebGL createWidget() {
		final G3MBuilder_WebGL builder = new G3MBuilder_WebGL();

		final LayerSet layerSet = new LayerSet();
		layerSet.addLayer(MapQuestLayer.newOSM(TimeInterval.fromDays(30)));

		builder.getPlanetRendererBuilder().setLayerSet(layerSet);

		final String proxy = null; // "http://galileo.glob3mobile.com/" +
									// "proxy.php?url="
		builder.setDownloader(new Downloader_WebGL( //
				8, // maxConcurrentOperationCount
				10, // delayMillis
				proxy));

		final NonOverlappingMarksRenderer renderer = new NonOverlappingMarksRenderer(
				30);
		builder.addRenderer(renderer);

		renderer.addMark(createMark("Label #1",
				Geodetic3D.fromDegrees(28.131817, -15.440219, 0)));
		renderer.addMark(createMark(Geodetic3D.fromDegrees(28.947345,
				-13.523105, 0)));
		renderer.addMark(createMark(Geodetic3D.fromDegrees(28.473802,
				-13.859360, 0)));
		renderer.addMark(createMark(Geodetic3D.fromDegrees(28.467706,
				-16.251426, 0)));
		renderer.addMark(createMark(Geodetic3D.fromDegrees(28.701819,
				-17.762003, 0)));
		renderer.addMark(createMark(Geodetic3D.fromDegrees(28.086595,
				-17.105796, 0)));
		renderer.addMark(createMark(Geodetic3D.fromDegrees(27.810709,
				-17.917639, 0)));

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
								final IImage image, final boolean expired) {
							final ICanvas canvas = context.getFactory()
									.createCanvas();
							final int width = 1024;
							final int height = 1024;
							canvas.initialize(width, height);

							canvas.setFillColor(Color
									.fromRGBA(1f, 1f, 0f, 0.5f));
							canvas.fillRectangle(0, 0, width, height);
							canvas.setLineWidth(4);
							canvas.setLineColor(Color.black());
							canvas.strokeRectangle(0, 0, width, height);

							final int steps = 8;
							final float leftStep = (float) width / steps;
							final float topStep = (float) height / steps;

							canvas.setLineWidth(1);
							canvas.setFillColor(Color.fromRGBA(0f, 0f, 0f,
									0.75f));
							for (int i = 1; i < steps; i++) {
								canvas.fillRectangle(0, topStep * i, width, 1);
								canvas.fillRectangle(leftStep * i, 0, 1, height);
							}

							canvas.setFont(GFont.monospaced());
							canvas.setFillColor(Color.black());
							// canvas.fillText("0,0", 0, 0);
							// canvas.fillText("w,h", width, height);
							for (int i = 0; i < steps; i++) {
								canvas.fillText("Hellow World", leftStep * i,
										topStep * i);
							}

							final float width8 = (float) width / 8;
							final float height8 = (float) height / 8;
							canvas.drawImage(image, width8, height8); // ok
							canvas.drawImage(image, width8 * 3, height8, 0.5f); // ok

							final int imageWidth = image.getWidth();
							final int imageHeight = image.getHeight();
							canvas.drawImage(image, width8, height8 * 3,
									imageWidth * 2, imageHeight * 2); // ok
							canvas.drawImage(image, width8 * 3, height8 * 3,
									imageWidth * 2, imageHeight * 2, 0.5f); // ok

							// ok
							canvas.drawImage(
									image, //
									0, 0, imageWidth,
									imageHeight, //
									width8 * 5, height8 * 5, imageWidth * 2,
									imageHeight * 2);
							// ok
							canvas.drawImage(
									image, //
									0, 0, imageWidth,
									imageHeight, //
									width8 * 7, height8 * 7, imageWidth * 2,
									imageHeight * 2, //
									0.5f);

							canvas.createImage(new IImageListener() {
								@Override
								public void imageCreated(
										final IImage canvasImage) {
									final QuadShape quad = new QuadShape(
											//
											Geodetic3D.fromDegrees(
													-34.615047738942699596,
													-58.4447233540403559, 1000), //
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
								final IImage image, final boolean expired) {
						}

						@Override
						public void onCancel(final URL url) {
						}
					};

					context.getDownloader().requestImage( //
							new URL("/g3m-marker.png"), //
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
<<<<<<< HEAD

	public CameraRenderer createCameraRenderer() {
		CameraRenderer cameraRenderer = new CameraRenderer();
		final boolean useInertia = true;
		cameraRenderer.addHandler(new CameraSingleDragHandler(useInertia));
		final boolean allowRotationInDoubleDrag = false;
		cameraRenderer.addHandler(new CameraDoubleDragHandler());
		// cameraRenderer.addHandler(new CameraZoomAndRotateHandler());

		cameraRenderer.addHandler(new CameraRotationHandler());
		cameraRenderer.addHandler(new CameraDoubleTapHandler());

		return cameraRenderer;
	}

	// public void testBranch_zrender_touchhandlers() {
	// final G3MBuilder_WebGL builder = new G3MBuilder_WebGL();
	//
	// //const Planet* planet = Planet::createEarth();
	// final Planet planet = Planet.createSphericalEarth();
	// //final Planet planet = Planet.createFlatEarth();
	// builder.setPlanet(planet);
	//
	// // create shape
	// ShapesRenderer shapesRenderer = new ShapesRenderer();
	// Shape box = new BoxShape(new Geodetic3D(Angle.fromDegrees(28.4),
	// Angle.fromDegrees(-16.4),
	// 0),
	// AltitudeMode.ABSOLUTE,
	// new Vector3D(3000, 3000, 20000),
	// 2,
	// Color.fromRGBA(1.0f, 1.0f, 0.0f, 0.5f),
	// Color.newFromRGBA(0.0f, 0.75f, 0.0f, 0.75f));
	// shapesRenderer.addShape(box);
	// builder.addRenderer(shapesRenderer);
	//
	// // create elevations for Tenerife from bil file
	// Sector sector = Sector.fromDegrees (27.967811065876, // min latitude
	// -17.0232177085356, // min longitude
	// 28.6103464294992, // max latitude
	// -16.0019401695656); // max longitude
	// Vector2I extent = new Vector2I(256, 256); // image resolution
	// URL url = new
	// URL("http://serdis.dis.ulpgc.es/~atrujill/glob3m/IGO/Tenerife-256x256.bil",
	// false);
	// ElevationDataProvider elevationDataProvider = new
	// SingleBilElevationDataProvider(url, sector, extent);
	// builder.getPlanetRendererBuilder().setElevationDataProvider(elevationDataProvider);
	// builder.getPlanetRendererBuilder().setVerticalExaggeration(4.0f);
	//
	// _widget = builder.createWidget();
	//
	// // set camera looking at Tenerife
	// Geodetic3D position = new Geodetic3D(Angle.fromDegrees(27.60),
	// Angle.fromDegrees(-16.54), 55000.0);
	// _widget.setCameraPosition(position);
	// _widget.setCameraPitch(Angle.fromDegrees(-50.0));
	// }
=======
>>>>>>> devAttitude

}
