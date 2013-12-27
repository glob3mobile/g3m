

package com.glob3.mobile.g3mandroidtestingapplication;

import java.util.Random;

import org.glob3.mobile.generated.AltitudeMode;
import org.glob3.mobile.generated.Angle;
import org.glob3.mobile.generated.Color;
import org.glob3.mobile.generated.ElevationDataProvider;
import org.glob3.mobile.generated.EllipsoidShape;
import org.glob3.mobile.generated.G3MContext;
import org.glob3.mobile.generated.G3MWidget;
import org.glob3.mobile.generated.GTask;
import org.glob3.mobile.generated.Geodetic3D;
import org.glob3.mobile.generated.MarksRenderer;
import org.glob3.mobile.generated.MeshRenderer;
import org.glob3.mobile.generated.PeriodicalTask;
import org.glob3.mobile.generated.Planet;
import org.glob3.mobile.generated.PlanetRenderer;
import org.glob3.mobile.generated.Sector;
import org.glob3.mobile.generated.Shape;
import org.glob3.mobile.generated.ShapesRenderer;
import org.glob3.mobile.generated.SingleBillElevationDataProvider;
import org.glob3.mobile.generated.TimeInterval;
import org.glob3.mobile.generated.URL;
import org.glob3.mobile.generated.Vector2I;
import org.glob3.mobile.generated.Vector3D;
import org.glob3.mobile.generated.AltitudeMode;
import org.glob3.mobile.generated.Color;
import org.glob3.mobile.generated.Geodetic3D;
import org.glob3.mobile.generated.LayerSet;
import org.glob3.mobile.generated.LayerTilesRenderParameters;
import org.glob3.mobile.generated.LevelTileCondition;
import org.glob3.mobile.generated.Mark;
import org.glob3.mobile.generated.MarksRenderer;
import org.glob3.mobile.generated.MeshRenderer;
import org.glob3.mobile.generated.Sector;
import org.glob3.mobile.generated.TimeInterval;
import org.glob3.mobile.generated.URL;
import org.glob3.mobile.generated.WMSLayer;
import org.glob3.mobile.generated.WMSServerVersion;
import org.glob3.mobile.specific.G3MBuilder_Android;
import org.glob3.mobile.specific.G3MWidget_Android;
import org.glob3.mobile.generated.BoxShape;
import org.glob3.mobile.generated.Vector3D;
import org.glob3.mobile.generated.Vector2I;

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
		// builder.getPlanetRendererBuilder().setRenderDebug(true);

		// final ShapesRenderer shapesRenderer = new ShapesRenderer();
		// builder.addRenderer(shapesRenderer);

		final MarksRenderer marksRenderer = new MarksRenderer(true);
		builder.addRenderer(marksRenderer);

		if (false) {
			final MeshRenderer meshRenderer = new MeshRenderer();
			meshRenderer.loadBSONMesh(new URL("file:///1951_r.bson"), Color.white());
			builder.addRenderer(meshRenderer);
		}

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

		//      if (false) {
		//
		//         final int time = 10; // SECS
		//
		//         final GTask elevationTask = new GTask() {
		//
		//            ElevationDataProvider _elevationDataProvider1 = new SingleBillElevationDataProvider(new URL(
		//                                                                   "file:///full-earth-2048x1024.bil", false),
		//                                                                   Sector.fullSphere(), new Vector2I(2048, 1024));
		//
		//
		//            @Override
		//            public void run(final G3MContext context) {
		//               final PlanetRenderer pr = _g3mWidget.getG3MWidget().getPlanetRenderer();
		//
		//               final Random r = new Random();
		//
		//               final int i = r.nextInt() % 4;
		//               switch (i) {
		//                  case 0:
		//                     pr.setElevationDataProvider(_elevationDataProvider1, false);
		//                     break;
		//                  case 1:
		//
		//                     final ElevationDataProvider _elevationDataProvider2 = new SingleBillElevationDataProvider(new URL(
		//                              "file:///caceres-2008x2032.bil", false), Sector.fromDegrees(39.4642996294239623,
		//                              -6.3829977122432933, 39.4829891936013553, -6.3645288909498845), new Vector2I(2008, 2032), 0);
		//
		//
		//                     pr.setElevationDataProvider(_elevationDataProvider2, true);
		//                     break;
		//                  case 2:
		//                     pr.setVerticalExaggeration(r.nextInt() % 5);
		//                     break;
		//                  case 3:
		//                     pr.setElevationDataProvider(null, false);
		//                     break;
		//
		//                  default:
		//                     break;
		//               }
		//
		//               final ElevationDataProvider edp = pr.getElevationDataProvider();
		//               if (edp != null) {
		//                  edp.setEnabled((r.nextInt() % 2) == 0);
		//               }
		//            }
		//         };
		//
		//         builder.addPeriodicalTask(new PeriodicalTask(TimeInterval.fromSeconds(time), elevationTask));
		//
		//      }
		
		boolean testingTransparencies = false;

		if (testingTransparencies){
			LayerSet layerSet = new LayerSet();

			WMSLayer blueMarble = new WMSLayer("bmng200405",
					new URL("http://www.nasa.network.com/wms?", false),
					WMSServerVersion.WMS_1_1_0,
					Sector.fullSphere(),
					"image/jpeg",
					"EPSG:4326",
					"",
					false,
					new LevelTileCondition(0, 6),
					TimeInterval.fromDays(30),
					true,
					new LayerTilesRenderParameters(Sector.fullSphere(),
							2, 4,
							0, 6,
							LayerTilesRenderParameters.defaultTileTextureResolution(),
							LayerTilesRenderParameters.defaultTileMeshResolution(),
							false)
					);
			layerSet.addLayer(blueMarble);

			WMSLayer i3Landsat = new WMSLayer("esat",
					new URL("http://data.worldwind.arc.nasa.gov/wms?", false),
					WMSServerVersion.WMS_1_1_0,
					Sector.fullSphere(),
					"image/jpeg",
					"EPSG:4326",
					"",
					false,
					new LevelTileCondition(7, 100),
					TimeInterval.fromDays(30),
					true,
					new LayerTilesRenderParameters(Sector.fullSphere(),
							2, 4,
							0, 12,
							LayerTilesRenderParameters.defaultTileTextureResolution(),
							LayerTilesRenderParameters.defaultTileMeshResolution(),
							false));
			layerSet.addLayer(i3Landsat);

			WMSLayer pnoa = new WMSLayer("PNOA",
					new URL("http://www.idee.es/wms/PNOA/PNOA", false),
					WMSServerVersion.WMS_1_1_0,
					Sector.fromDegrees(21, -18, 45, 6),
					"image/png",
					"EPSG:4326",
					"",
					true,
					null,
					TimeInterval.fromDays(30),
					true,
					null,
					(float) 0.5);
			layerSet.addLayer(pnoa);

			builder.getPlanetRendererBuilder().setLayerSet(layerSet);
		}

		if (false) {
			marksRenderer.addMark(new Mark("HIGH MARK",
					Geodetic3D.fromDegrees(0, 0, 100000),
					AltitudeMode.RELATIVE_TO_GROUND
					));

			marksRenderer.addMark(new Mark("LOW MARK",
					Geodetic3D.fromDegrees(0, 0, 100),
					AltitudeMode.RELATIVE_TO_GROUND
					));
		}

		
		if (true) {
			ShapesRenderer shapesRenderer = new ShapesRenderer();
		
		  {
			    Shape box = new BoxShape(new Geodetic3D(Angle.fromDegrees(28.4),
			                                             Angle.fromDegrees(-16.4),
			                                             0),
			                              AltitudeMode.ABSOLUTE,
			                              new Vector3D(3000.0, 3000.0, 20000.0),
			                              2,
			                              Color.fromRGBA(0.0f,    1.0f, 0.0f, 0.5f),
			                              Color.newFromRGBA(0f, 0.75f, 0f, 0.75f));
			    shapesRenderer.addShape(box);
			  }
			  {
			    Shape box = new BoxShape(new Geodetic3D(Angle.fromDegrees(26),
			                                             Angle.fromDegrees(0),
			                                             0),
			                                             AltitudeMode.ABSOLUTE,
			                              new Vector3D(200000, 200000, 5000000),
			                              2,
			                              Color.fromRGBA(1f,    0f, 0f, 0.5f),
			                              Color.newFromRGBA(0f, 0.75f, 0f, 0.75f));
			    //box->setAnimatedScale(1, 1, 20);
			    shapesRenderer.addShape(box);
			  }
			  
			  builder.addRenderer(shapesRenderer);

		}
		
		if (true) {
			  float verticalExaggeration = 4.0f;
			  builder.getPlanetRendererBuilder().setVerticalExaggeration(verticalExaggeration);

			  //ElevationDataProvider* elevationDataProvider = NULL;
			  //builder.getPlanetRendererBuilder()->setElevationDataProvider(elevationDataProvider);

			  //  ElevationDataProvider* elevationDataProvider = new SingleBillElevationDataProvider(URL("file:///full-earth-2048x1024.bil", false),
			  //                                                                                     Sector::fullSphere(),
			  //                                                                                     Vector2I(2048, 1024));
			/*
			  ElevationDataProvider* elevationDataProvider = new SingleBillElevationDataProvider(URL("file:///caceres-2008x2032.bil", false),
			                                                                                     Sector::fromDegrees(                                                                                 39.4642996294239623,                                                                                -6.3829977122432933,                                                                                  39.4829891936013553,-6.3645288909498845),                                                              Vector2I(2008, 2032),0);*/
			  // obtaining valid elevation data url
			  Sector sector = Sector.fromDegrees (27.967811065876,                  // min latitude
			                                      -17.0232177085356,                // min longitude
			                                      28.6103464294992,                 // max latitude
			                                      -16.0019401695656);               // max longitude
			  Vector2I extent = new Vector2I(256, 256);                             // image resolution
			  
			  URL url = new URL("http://128.102.22.115/elev?REQUEST=GetMap&SERVICE=WMS&VERSION=1.3.0&LAYERS=srtm3&STYLES=&FORMAT=image/bil&BGCOLOR=0xFFFFFF&TRANSPARENT=TRUE&CRS=EPSG:4326&BBOX=-17.0232177085356,27.967811065876,-16.0019401695656,28.6103464294992&WIDTH=256&HEIGHT=256", false);
			  
			  // add this image to the builder
			  ElevationDataProvider elevationDataProvider = new SingleBillElevationDataProvider(url, sector, extent);
			  builder.getPlanetRendererBuilder().setElevationDataProvider(elevationDataProvider);

		}

		_g3mWidget = builder.createWidget();
		_placeHolder = (RelativeLayout) findViewById(R.id.g3mWidgetHolder);
		_placeHolder.addView(_g3mWidget);

		  Geodetic3D position = new Geodetic3D(Angle.fromDegrees(27.50), Angle.fromDegrees(-16.58), 250000);
		  _g3mWidget.setCameraPosition(position);
		  _g3mWidget.setCameraPitch(Angle.fromDegrees(25));


	}


	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
