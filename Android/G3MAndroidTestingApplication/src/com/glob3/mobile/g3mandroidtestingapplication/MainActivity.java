

package com.glob3.mobile.g3mandroidtestingapplication;

import org.glob3.mobile.generated.AltitudeMode;
import org.glob3.mobile.generated.Angle;
import org.glob3.mobile.generated.BoxShape;
import org.glob3.mobile.generated.Color;
import org.glob3.mobile.generated.DownloaderImageBuilder;
import org.glob3.mobile.generated.ElevationDataProvider;
import org.glob3.mobile.generated.Geodetic3D;
import org.glob3.mobile.generated.HUDQuadWidget;
import org.glob3.mobile.generated.HUDRelativePosition;
import org.glob3.mobile.generated.HUDRelativeSize;
import org.glob3.mobile.generated.HUDRenderer;
import org.glob3.mobile.generated.LayerSet;
import org.glob3.mobile.generated.LayerTilesRenderParameters;
import org.glob3.mobile.generated.LevelTileCondition;
import org.glob3.mobile.generated.MapQuestLayer;
import org.glob3.mobile.generated.Mark;
import org.glob3.mobile.generated.MarksRenderer;
import org.glob3.mobile.generated.MeshRenderer;
import org.glob3.mobile.generated.Sector;
import org.glob3.mobile.generated.Shape;
import org.glob3.mobile.generated.ShapesRenderer;
import org.glob3.mobile.generated.SingleBilElevationDataProvider;
import org.glob3.mobile.generated.TimeInterval;
import org.glob3.mobile.generated.URL;
import org.glob3.mobile.generated.Vector2I;
import org.glob3.mobile.generated.Vector3D;
import org.glob3.mobile.generated.WMSLayer;
import org.glob3.mobile.generated.WMSServerVersion;
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

   G3MBuilder_Android builder = null;
   MarksRenderer marksRenderer = new MarksRenderer(false);

   @Override
   protected void onCreate(final Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);

      requestWindowFeature(Window.FEATURE_NO_TITLE);
      getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

      setContentView(R.layout.activity_main);
      builder = new G3MBuilder_Android(this);
      // builder.getPlanetRendererBuilder().setRenderDebug(true);


		if (false) {
			final MeshRenderer meshRenderer = new MeshRenderer();
			meshRenderer.loadBSONMesh(new URL("file:///1951_r.bson"), Color.white());
			builder.addRenderer(meshRenderer);
		}
	
		builder.addRenderer(marksRenderer);
		
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

		
		if (false) {
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
		
		if (false) {
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
			  ElevationDataProvider elevationDataProvider = new SingleBilElevationDataProvider(url, sector, extent);
			  builder.getPlanetRendererBuilder().setElevationDataProvider(elevationDataProvider);

		}
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

{
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
