

package org.glob3.mobile.client;

<<<<<<< HEAD
import java.util.ArrayList;

import org.glob3.mobile.generated.AltitudeMode;
import org.glob3.mobile.generated.Angle;
import org.glob3.mobile.generated.BoxShape;
import org.glob3.mobile.generated.BusyMeshRenderer;
import org.glob3.mobile.generated.CameraDoubleDragHandler;
import org.glob3.mobile.generated.CameraDoubleTapHandler;
import org.glob3.mobile.generated.CameraRenderer;
import org.glob3.mobile.generated.CameraRotationHandler;
import org.glob3.mobile.generated.CameraSingleDragHandler;
import org.glob3.mobile.generated.CircleShape;
import org.glob3.mobile.generated.Color;
import org.glob3.mobile.generated.CompositeRenderer;
import org.glob3.mobile.generated.DirectMesh;
import org.glob3.mobile.generated.ElevationDataProvider;
import org.glob3.mobile.generated.EllipsoidShape;
import org.glob3.mobile.generated.ErrorRenderer;
import org.glob3.mobile.generated.FixedFocusSceneLighting;
import org.glob3.mobile.generated.FloatBufferBuilderFromColor;
import org.glob3.mobile.generated.FloatBufferBuilderFromGeodetic;
import org.glob3.mobile.generated.G3MContext;
import org.glob3.mobile.generated.G3MEventContext;
import org.glob3.mobile.generated.G3MWidget;
import org.glob3.mobile.generated.GEO2DLineRasterStyle;
import org.glob3.mobile.generated.GEO2DLineStringGeometry;
import org.glob3.mobile.generated.GEO2DMultiLineStringGeometry;
import org.glob3.mobile.generated.GEO2DMultiPolygonGeometry;
import org.glob3.mobile.generated.GEO2DPointGeometry;
import org.glob3.mobile.generated.GEO2DPolygonData;
import org.glob3.mobile.generated.GEO2DPolygonGeometry;
import org.glob3.mobile.generated.GEO2DSurfaceRasterStyle;
import org.glob3.mobile.generated.GEOGeometry;
import org.glob3.mobile.generated.GEOLineRasterSymbol;
import org.glob3.mobile.generated.GEOMultiLineRasterSymbol;
import org.glob3.mobile.generated.GEOPolygonRasterSymbol;
import org.glob3.mobile.generated.GEORenderer;
import org.glob3.mobile.generated.GEOSymbol;
import org.glob3.mobile.generated.GEOSymbolizer;
import org.glob3.mobile.generated.GEOTileRasterizer;
import org.glob3.mobile.generated.GInitializationTask;
import org.glob3.mobile.generated.GLPrimitive;
import org.glob3.mobile.generated.GTask;
import org.glob3.mobile.generated.Geodetic2D;
import org.glob3.mobile.generated.Geodetic3D;
import org.glob3.mobile.generated.HUDErrorRenderer;
import org.glob3.mobile.generated.IBufferDownloadListener;
import org.glob3.mobile.generated.IByteBuffer;
import org.glob3.mobile.generated.ICameraActivityListener;
import org.glob3.mobile.generated.ICameraConstrainer;
import org.glob3.mobile.generated.IDownloader;
import org.glob3.mobile.generated.IImage;
import org.glob3.mobile.generated.IImageDownloadListener;
import org.glob3.mobile.generated.IImageListener;
import org.glob3.mobile.generated.IImageUtils;
import org.glob3.mobile.generated.IJSONParser;
import org.glob3.mobile.generated.IStorage;
import org.glob3.mobile.generated.IThreadUtils;
import org.glob3.mobile.generated.InitialCameraPositionProvider;
import org.glob3.mobile.generated.JSONArray;
import org.glob3.mobile.generated.JSONBaseObject;
import org.glob3.mobile.generated.JSONObject;
import org.glob3.mobile.generated.LayerSet;
import org.glob3.mobile.generated.LayerTilesRenderParameters;
import org.glob3.mobile.generated.LayerTouchEvent;
import org.glob3.mobile.generated.LayerTouchEventListener;
import org.glob3.mobile.generated.LevelTileCondition;
import org.glob3.mobile.generated.Mark;
import org.glob3.mobile.generated.MarkTouchListener;
import org.glob3.mobile.generated.MarksRenderer;
import org.glob3.mobile.generated.Mesh;
import org.glob3.mobile.generated.MeshRenderer;
import org.glob3.mobile.generated.PeriodicalTask;
import org.glob3.mobile.generated.Planet;
import org.glob3.mobile.generated.PlanetRenderer;
import org.glob3.mobile.generated.PlanetRendererBuilder;
import org.glob3.mobile.generated.QuadShape;
import org.glob3.mobile.generated.RectangleF;
import org.glob3.mobile.generated.Renderer;
import org.glob3.mobile.generated.SceneLighting;
import org.glob3.mobile.generated.Sector;
import org.glob3.mobile.generated.Shape;
import org.glob3.mobile.generated.ShapesRenderer;
import org.glob3.mobile.generated.SimpleCameraConstrainer;
import org.glob3.mobile.generated.SimpleInitialCameraPositionProvider;
import org.glob3.mobile.generated.SingleBillElevationDataProvider;
import org.glob3.mobile.generated.StrokeCap;
import org.glob3.mobile.generated.StrokeJoin;
import org.glob3.mobile.generated.TimeInterval;
import org.glob3.mobile.generated.URL;
import org.glob3.mobile.generated.Vector2I;
import org.glob3.mobile.generated.Vector3D;
import org.glob3.mobile.generated.WMSLayer;
import org.glob3.mobile.generated.WMSServerVersion;
import org.glob3.mobile.generated.WidgetUserData;
import org.glob3.mobile.specific.Downloader_WebGL;
=======
>>>>>>> origin/purgatory
import org.glob3.mobile.specific.G3MBuilder_WebGL;
import org.glob3.mobile.specific.G3MWidget_WebGL;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;


public class G3MWebGLDemo
         implements
            EntryPoint {

   private static final String _g3mWidgetHolderId = "g3mWidgetHolder";
   private G3MWidget_WebGL     _widget            = null;

   @Override
   public void onModuleLoad() {
      /*
      ======================================================================
       WARNING / CUIDADO / ACHTUNG
      ----------------------------------------------------------------------         
       Don't touch this project for quick & dirty tests.  If you do it, 
       your commit rights will be revoked and you'll spend your after-life
       burning at the hell.
      ======================================================================
      */

      final G3MBuilder_WebGL builder = new G3MBuilder_WebGL();
      _widget = builder.createWidget();

      final Panel g3mWidgetHolder = RootPanel.get(_g3mWidgetHolderId);
      g3mWidgetHolder.add(_widget);
   }


<<<<<<< HEAD
   private GInitializationTask createMarkersInitializationTask() {
      final GInitializationTask initializationTask = new GInitializationTask() {

         @Override
         public void run(final G3MContext context) {

            final IDownloader downloader = context.getDownloader();

            final IBufferDownloadListener listener = new IBufferDownloadListener() {

               @Override
               public void onDownload(final URL url,
                                      final IByteBuffer buffer,
                                      final boolean expired) {

                  final String response = buffer.getAsString();
                  final IJSONParser parser = context.getJSONParser();
                  final JSONBaseObject jsonObject = parser.parse(response);
                  final JSONObject object = jsonObject.asObject();
                  final JSONArray list = object.getAsArray("list");
                  for (int i = 0; i < list.size(); i++) {

                     final JSONObject city = list.getAsObject(i);

                     final JSONObject coords = city.getAsObject("coord");
                     final Geodetic2D position = new Geodetic2D(Angle.fromDegrees(coords.getAsNumber("lat").value()),
                              Angle.fromDegrees(coords.getAsNumber("lon").value()));
                     final JSONArray weather = city.getAsArray("weather");
                     final JSONObject weatherObject = weather.getAsObject(0);

                     String icon = "";
                     if (weatherObject.getAsString("icon", "DOUBLE").equals("DOUBLE")) {
                        icon = "" + (int) weatherObject.getAsNumber("icon").value() + "d.png";
                        if (icon.length() < 7) {
                           icon = "0" + icon;
                        }
                     }
                     else {
                        icon = weatherObject.getAsString("icon", "DOUBLE") + ".png";
                     }

                     _markersRenderer.addMark(new Mark(//
                              city.getAsString("name", ""), //
                              new URL("http://openweathermap.org/img/w/" + icon, false), //
                              new Geodetic3D(position, 0), //
                              AltitudeMode.RELATIVE_TO_GROUND, 0, //
                              true, //
                              14, //
                              Color.white(), //
                              Color.black(), //
                              2));
                  }

                  _markersParsed = true;
               }


               @Override
               public void onError(final URL url) {
                  Window.alert("Error retrieving weather data");
               }


               @Override
               public void onCancel(final URL url) {
                  // DO Nothing
               }


               @Override
               public void onCanceledDownload(final URL url,
                                              final IByteBuffer data,
                                              final boolean expired) {
                  // Do Nothing
               }
            };

            downloader.requestBuffer( //
                     new URL("http://openweathermap.org/data/2.1/find/city?bbox=-80,-180,80,180,4&cluster=yes", false), //
                     0, //
                     TimeInterval.fromHours(1.0), //
                     false, listener, //
                     false);
         }

			final ErrorRenderer errorRenderer = new HUDErrorRenderer();
			_widget.initWidget(//
					storage, //
					downloader, //
					threadUtils, //
					cameraActivityListener, //
					planet, //
					cameraConstraints, //
					cameraRenderer, //
					mainRenderer, //
					busyRenderer, //
					errorRenderer, //
					backgroundColor, //
					logFPS, //
					logDownloaderStatistics, //
					initializationTask, //
					autoDeleteInitializationTask, //
					periodicalTasks, //
					userData, //
					lighting, //
					initialCameraPositionProvider);
		}
	}


	private GInitializationTask createMarkersInitializationTask() {
		final GInitializationTask initializationTask = new GInitializationTask() {

			@Override
			public void run(final G3MContext context) {

				final IDownloader downloader = context.getDownloader();

				final IBufferDownloadListener listener = new IBufferDownloadListener() {

					@Override
					public void onDownload(final URL url,
							final IByteBuffer buffer,
							final boolean expired) {

						final String response = buffer.getAsString();
						final IJSONParser parser = context.getJSONParser();
						final JSONBaseObject jsonObject = parser.parse(response);
						final JSONObject object = jsonObject.asObject();
						final JSONArray list = object.getAsArray("list");
						for (int i = 0; i < list.size(); i++) {

							final JSONObject city = list.getAsObject(i);

							final JSONObject coords = city.getAsObject("coord");
							final Geodetic2D position = new Geodetic2D(Angle.fromDegrees(coords.getAsNumber("lat").value()),
									Angle.fromDegrees(coords.getAsNumber("lon").value()));
							final JSONArray weather = city.getAsArray("weather");
							final JSONObject weatherObject = weather.getAsObject(0);

							String icon = "";
							if (weatherObject.getAsString("icon", "DOUBLE").equals("DOUBLE")) {
								icon = "" + (int) weatherObject.getAsNumber("icon").value() + "d.png";
								if (icon.length() < 7) {
									icon = "0" + icon;
								}
							}
							else {
								icon = weatherObject.getAsString("icon", "DOUBLE") + ".png";
							}

							_markersRenderer.addMark(new Mark(//
									city.getAsString("name", ""), //
									new URL("http://openweathermap.org/img/w/" + icon, false), //
									new Geodetic3D(position, 0), //
									AltitudeMode.RELATIVE_TO_GROUND, 0, //
									true, //
									14, //
									Color.white(), //
									Color.black(), //
									2));
						}

						_markersParsed = true;
					}


					@Override
					public void onError(final URL url) {
						Window.alert("Error retrieving weather data");
					}


					@Override
					public void onCancel(final URL url) {
						// DO Nothing
					}


					@Override
					public void onCanceledDownload(final URL url,
							final IByteBuffer data,
							final boolean expired) {
						// Do Nothing
					}
				};

				downloader.requestBuffer( //
						new URL("http://openweathermap.org/data/2.1/find/city?bbox=-80,-180,80,180,4&cluster=yes", false), //
						0, //
						TimeInterval.fromHours(1.0), //
						false, listener, //
						false);
			}


			@Override
			public boolean isDone(final G3MContext context) {
				if (_markersParsed) {
					_widget.setAnimatedCameraPosition(new Geodetic3D(Angle.fromDegrees(45d), Angle.fromDegrees(0.d), 3000000),
							TimeInterval.fromSeconds(3));
					return true;
				}
				return false;
			}
		};
		return initializationTask;
	}


	public void initDefaultWithBuilder() {
		final G3MBuilder_WebGL builder = new G3MBuilder_WebGL();

		final boolean useMarkers = false;
		if (useMarkers) {
			// marks renderer
			final boolean readyWhenMarksReady = true;
			final MarksRenderer marksRenderer = new MarksRenderer(readyWhenMarksReady);

			/*
			 * marksRenderer.setMarkTouchListener(new MarkTouchListener() {
			 * 
			 * @Override public boolean touchedMark(final Mark mark) {
			 * Window.alert("Touched on mark: " + mark.getLabel()); return true;
			 * } }, true);
			 */

			final Mark m1 = new Mark(
					//
					"Paris",
					new URL(
							"http://icons.iconarchive.com/icons/icons-land/vista-map-markers/48/Map-Marker-Flag-3-Left-Chartreuse-icon.png",
							false), //
							new Geodetic3D(Angle.fromDegrees(48.859746), Angle.fromDegrees(2.352051), 0), AltitudeMode.RELATIVE_TO_GROUND,
							0, true, 15);
			// m1->addTouchListener(listener);
			marksRenderer.addMark(m1);

			final Mark m2 = new Mark( //
					"Las Palmas", //
					new URL(
							"http://icons.iconarchive.com/icons/icons-land/vista-map-markers/48/Map-Marker-Flag-3-Right-Pink-icon.png",
							false), //
							new Geodetic3D(Angle.fromDegrees(28.116956), Angle.fromDegrees(-15.440453), 0), //
							AltitudeMode.RELATIVE_TO_GROUND, 0, //
							true, 15);

			// m2->addTouchListener(listener);
			marksRenderer.addMark(m2);

			builder.addRenderer(marksRenderer);
		}

		/*
      final ShapesRenderer shapesRenderer = new ShapesRenderer();
      builder.addRenderer(shapesRenderer);
		 */

		// builder.setInitializationTask(createMarkersInitializationTask());


		final LayerSet layerSet = new LayerSet();

		/*
		 * final boolean blueMarble = false; if (blueMarble) { final WMSLayer
		 * blueMarbleL = new WMSLayer( // "bmng200405", // new
		 * URL("http://www.nasa.network.com/wms?", false), //
		 * WMSServerVersion.WMS_1_1_0, // Sector.fullSphere(), // "image/jpeg",
		 * // "EPSG:4326", // "", // false, // //new LevelTileCondition(0, 6),
		 * null, // TimeInterval.fromDays(30), // true);
		 * layerSet.addLayer(blueMarbleL);
		 * blueMarbleL.addTerrainTouchEventListener(new
		 * TerrainTouchEventListener() {
		 * 
		 * @Override public boolean onTerrainTouch(G3MEventContext context,
		 * TerrainTouchEvent ev) { Window.alert("touching terrain blueMarble");
		 * return false; }
		 * 
		 * @Override public void dispose() {} }); }
		 */

		final boolean useOrtoAyto = false;
		if (useOrtoAyto) {

			final LayerTilesRenderParameters ltrp = new LayerTilesRenderParameters(Sector.fullSphere(), 2, 4, 0, 19, new Vector2I(
					256, 256), LayerTilesRenderParameters.defaultTileMeshResolution(), false);

			final WMSLayer ortoAyto = new WMSLayer( //
					"orto_refundida", //
					new URL("http://195.57.27.86/wms_etiquetas_con_orto.mapdef?", false), //
					WMSServerVersion.WMS_1_1_0, //
					new Sector( //
							new Geodetic2D(Angle.fromDegrees(39.350228), Angle.fromDegrees(-6.508713)), //
							new Geodetic2D(Angle.fromDegrees(39.536351), Angle.fromDegrees(-6.25946))), //
							"image/jpeg", //
							"EPSG:4326", //
							"", //
							false, //
							new LevelTileCondition(4, 19), //
							TimeInterval.fromDays(30), //
							true, //
							ltrp);
			layerSet.addLayer(ortoAyto);
		}

		final boolean useOsm = false;
		if (useOsm) {
			final WMSLayer osm = new WMSLayer("osm_auto:all", // layer name
					new URL("http://129.206.228.72/cached/osm", false), // server
					// url
					WMSServerVersion.WMS_1_1_0, // server version
					Sector.fullSphere(), // initial bounding box
					"image/jpeg", // image format
					"EPSG:4326", // SRS
					"", // style
					false, // include transparency
					null, // layer condition
					TimeInterval.fromDays(30), // time interval to cache
					true); // read expired
			layerSet.addLayer(osm);
			osm.addLayerTouchEventListener(new LayerTouchEventListener() {

				@Override
				public boolean onTerrainTouch(final G3MEventContext context,
						final LayerTouchEvent ev) {
					final Geodetic3D position = ev.getPosition();
					Window.alert("touching terrain on osm layer " + Double.toString(position._latitude._degrees) + ","
							+ Double.toString(position._longitude._degrees));
					return false;
				}


				@Override
				public void dispose() {
				}
			});
		}

		final boolean useLatlon = false;
		if (useLatlon) {
			final WMSLayer latlon = new WMSLayer("latlon", new URL("http://wms.latlon.org/", false), WMSServerVersion.WMS_1_1_0,
					Sector.fromDegrees(-85.05, -180.0, 85.5, 180.0), "image/jpeg", "EPSG:4326", "", false, null, // layer condition
					TimeInterval.fromDays(30), // time interval to cache
					true); // read expired
			layerSet.addLayer(latlon);
		}

		final boolean useBing = false;
		if (useBing) {
			final WMSLayer bing = new WMSLayer( //
					"ve", //
					new URL("http://worldwind27.arc.nasa.gov/wms/virtualearth?", false), //
					WMSServerVersion.WMS_1_1_0, //
					Sector.fullSphere(), //
					"image/jpeg", //
					"EPSG:4326", //
					"", //
					false, //
					null, // layer condition
					TimeInterval.fromDays(30), // time interval to cache
					true);
			layerSet.addLayer(bing);
		}
		

		boolean testingTransparencies = false;
		if (testingTransparencies){
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
		}
		
		//builder.getPlanetRendererBuilder().setLayerSet(layerSet);

		/*
		 * final WMSLayer political = new WMSLayer("topp:cia", new
		 * URL("http://worldwind22.arc.nasa.gov/geoserver/wms?", false),
		 * WMSServerVersion.WMS_1_1_0, Sector.fullSphere(), "image/png",
		 * "EPSG:4326", "countryboundaries", true, null,
		 * TimeInterval.fromDays(30), true); layerSet.addLayer(political);
		 */

		/*
		 * final MapQuestLayer mqlOSM =
		 * MapQuestLayer.newOSM(TimeInterval.fromDays(30));
		 * layerSet.addLayer(mqlOSM);
		 */

		/*
		 * final WMSLayer bingLayer = LayerBuilder.createOSMLayer(true);
		 * layerSet.addLayer(bingLayer);
		 * bingLayer.addTerrainTouchEventListener(new
		 * TerrainTouchEventListener() {
		 * 
		 * @Override public boolean onTerrainTouch(G3MEventContext context,
		 * TerrainTouchEvent ev) { Geodetic2D position =
		 * ev.getPosition().asGeodetic2D();
		 * Window.alert("touching terrain at coords (" +
		 * NumberFormat.getFormat("#.00").format(position.latitude().degrees())
		 * + ", " +
		 * NumberFormat.getFormat("#.00").format(position.longitude().degrees())
		 * + ")"); //URL url = bingLayer.getFeatureInfoURL(position,
		 * ev.getSector()); //Window.alert(url.toString());
		 * 
		 * return false; }
		 * 
		 * @Override public void dispose() {} });
		 */

		/*

      final WMSLayer blueMarble = LayerBuilder.createBlueMarbleLayer(true);
      layerSet.addLayer(blueMarble);*/


      //      layerSet.addLayer(MapQuestLayer.newOpenAerial(TimeInterval.fromDays(30)));

      /*
       * final WMSLayer pnoa = LayerBuilder.createPNOALayer(true);
       * layerSet.addLayer(pnoa);
       */

      /*
      // testing visible sector listener
      final VisibleSectorListener myListener = new VisibleSectorListener() {
         @Override
         public void onVisibleSectorChange(final Sector visibleSector,
                                           final Geodetic3D cameraPosition) {
            Window.alert("Visible Sector from lat(" + visibleSector.lower().latitude().degrees() + "), lon("
                         + visibleSector.lower().longitude().degrees() + ") to lat(" + visibleSector.upper().latitude().degrees()
                         + "), lon(" + visibleSector.upper().longitude().degrees() + ")");
         }
      };

      builder.getPlanetRendererBuilder().addVisibleSectorListener(myListener, TimeInterval.fromMilliseconds(2000));
       */


      /*
       * // testing getfeatureinfo final IBufferDownloadListener myListener =
       * new IBufferDownloadListener() {
       * 
       * @Override public void onDownload(final URL url, final IByteBuffer
       * buffer, final boolean expired) { final String response =
       * buffer.getAsString(); Window.alert("GetFeatureInfo URL: " +
       * response); }
       * 
       * 
       * @Override public void onError(final URL url) { }
       * 
       * 
       * @Override public void onCancel(final URL url) { }
       * 
       * 
       * @Override public void onCanceledDownload(final URL url, final
       * IByteBuffer data, final boolean expired) { } };
       * 
       * pnoa.addTerrainTouchEventListener(new TerrainTouchEventListener() {
       * 
       * @Override public boolean onTerrainTouch(final G3MEventContext
       * context, final TerrainTouchEvent event) { final URL url =
       * event.getLayer
       * ().getFeatureInfoURL(event.getPosition().asGeodetic2D(),
       * event.getSector());
       * Window.alert("Get Feature Info URL for this position: " +
       * url.getPath()); //final IDownloader myDownloader =
       * _widget.getG3MContext().getDownloader();
       * //myDownloader.requestBuffer(url, (long)0,
       * //TimeInterval.fromHours(1.0), false, myListener, false); return
       * false; }
       * 
       * 
       * @Override public void dispose() { } });
       */

      /*
      final GInitializationTask initializationTask = new GInitializationTask() {
          @Override
          public void run(final G3MContext context) {
             final URL url = new URL("ws://192.168.0.103:8888/tube/scene/2g59wh610g6c1kmkt0l", false);
             final IWebSocketListener listener = new IWebSocketListener() {
                @Override
                public void onOpen(final IWebSocket ws) {
                   ILogger.instance().logError(ws + " opened!");
                }


                @Override
                public void onMesssage(final IWebSocket ws,
                                       final String message) {
                   ILogger.instance().logError(ws + " message \"" + message + "\"");
                }


                @Override
                public void onError(final IWebSocket ws,
                                    final String error) {
                   ILogger.instance().logError(ws + " error \"" + error + "\"");
                }


                @Override
                public void onClose(final IWebSocket ws) {
                   ILogger.instance().logError(ws + " closed!");
                }


                @Override
                public void dispose() {
                }
             };
             context.getFactory().createWebSocket(url, listener, true, true);

          }


          @Override
          public boolean isDone(final G3MContext context) {
             return true;
          }
       };

      builder.setInitializationTask(initializationTask);

		 */

		//builder.getPlanetRendererBuilder().setLayerSet(layerSet);

		// set elevations
		//      final Sector sector = Sector.fromDegrees(27.967811065876, -17.0232177085356, 28.6103464294992, -16.0019401695656);
		//      final Vector2I extent = new Vector2I(256, 256);
		//      final URL url = NasaBillElevationDataURL.compoundURL(sector, extent);
		//      final ElevationDataProvider elevationDataProvider = new SingleBillElevationDataProvider(url, sector, extent);
		//      builder.getPlanetRendererBuilder().setElevationDataProvider(elevationDataProvider);
		//      builder.getPlanetRendererBuilder().setVerticalExaggeration(2.0f);


		// camera constrainer
		if (false) {
			final ICameraConstrainer myCameraConstrainer = new ICameraConstrainer() {
				private boolean firstTime = true;


				@Override
				public void dispose() {
				}


				@Override
				public boolean onCameraChange(final Planet planet,
						final Camera previousCamera,
						final Camera nextCamera) {
					if (firstTime) {
						final Geodetic3D position = new Geodetic3D(Angle.fromDegrees(28), Angle.fromDegrees(-16), 4e5);
						nextCamera.setGeodeticPosition(position);
						firstTime = false;
					}
					else {
						final double maxHeight = 5e5;
						final double minLat = 26.5, maxLat = 30.5, minLon = -19.5, maxLon = -12.5;
						final Geodetic3D cameraPosition = nextCamera.getGeodeticPosition();
						final double lat = cameraPosition._latitude._degrees;
						final double lon = cameraPosition._longitude._degrees;
						final double pitch = nextCamera.getPitch()._degrees;
						final double heading = nextCamera.getHeading()._degrees;
						if ((cameraPosition._height > maxHeight) || (lon < minLon) || (lon > maxLon) || (lat < minLat)
								|| (lat > maxLat) || (pitch > 0.01) || (Math.abs(heading) > 0.01)) {
							nextCamera.copyFrom(previousCamera);
						}
					}
					return true;
				}
			};
			builder.addCameraConstraint(myCameraConstrainer);
			builder.setPlanet(Planet.createFlatEarth());
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


		_widget = builder.createWidget();


		  Geodetic3D position = new Geodetic3D(Angle.fromDegrees(27.50), Angle.fromDegrees(-16.58), 25000);
		  _widget.setCameraPosition(position);
		  _widget.setCameraPitch(Angle.fromDegrees(75));


		/*
      // set the camera looking at
      final Geodetic3D position = new Geodetic3D(Angle.fromDegrees(28), Angle.fromDegrees(-16), 7);
      _widget.setCameraPosition(position);
      _widget.setCameraPitch(Angle.fromDegrees(75));*/


      /*
       * // testing downloading from url final IBufferDownloadListener
       * myListener = new IBufferDownloadListener() {
       * 
       * @Override public void onDownload(final URL url, final IByteBuffer
       * buffer, boolean expired) { final String response =
       * buffer.getAsString(); Window.alert("Downloaded text: " + response); }
       * 
       * 
       * @Override public void onError(final URL url) { }
       * 
       * 
       * @Override public void onCancel(final URL url) { // TODO
       * Auto-generated method stub }
       * 
       * 
       * @Override public void onCanceledDownload(final URL url, final
       * IByteBuffer data, boolean expired) { }
       * 
       * };
       * 
       * final IDownloader myDownloader =
       * _widget.getG3MContext().getDownloader();
       * myDownloader.requestBuffer(new
       * URL("http://serdis.dis.ulpgc.es/~atrujill/glob3m/Tutorial/sample.txt"
       * , false), (long)0, TimeInterval.fromHours(1.0), false, myListener,
       * false);
       */

   }
=======
>>>>>>> origin/purgatory
}
