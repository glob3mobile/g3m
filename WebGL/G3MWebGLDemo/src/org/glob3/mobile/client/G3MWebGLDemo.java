

package org.glob3.mobile.client;

<<<<<<< HEAD
import java.util.ArrayList;

import org.glob3.mobile.generated.*;
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
      
      final boolean useMapQuest = true;
      if (useMapQuest) {
    	  final MapQuestLayer mqlOSM = MapQuestLayer.newOSM(TimeInterval.fromDays(30));
    	  layerSet.addLayer(mqlOSM);
    	  //layerSet.addLayer(new MapBoxLayer("examples.map-9ijuk24y", TimeInterval.fromDays(30)));
      }

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

      builder.getPlanetRendererBuilder().setLayerSet(layerSet);


      // set elevations
      //      final Sector sector = Sector.fromDegrees(27.967811065876, -17.0232177085356, 28.6103464294992, -16.0019401695656);
      //      final Vector2I extent = new Vector2I(256, 256);
      //      final URL url = NasaBillElevationDataURL.compoundURL(sector, extent);
      //      final ElevationDataProvider elevationDataProvider = new SingleBillElevationDataProvider(url, sector, extent);
      //      builder.getPlanetRendererBuilder().setElevationDataProvider(elevationDataProvider);
      //      builder.getPlanetRendererBuilder().setVerticalExaggeration(2.0f);

      
      if (true) {      
    	  // testing selecting shapes
          // GeoTileRasterizer is needed to draw RasterShapes
          GEOTileRasterizer geoTileRasterizer = new GEOTileRasterizer();
          builder.getPlanetRendererBuilder().addTileRasterizer(geoTileRasterizer);
          final ShapesRenderer shapesRenderer = new ShapesRenderer(geoTileRasterizer);
          builder.addRenderer(shapesRenderer);
    	  
    	  final double factor = 3000;
    	  final Vector3D radius1 = new Vector3D(factor, factor, factor);
    	  final Vector3D radius2 = new Vector3D(factor*1.5, factor*1.5, factor*1.5);
    	  final Vector3D radiusBox = new Vector3D(factor, factor*1.5, factor*2);

    	  Shape box1 = new BoxShape(new Geodetic3D(Angle.fromDegrees(39.70),
    			  Angle.fromDegrees(2.80),
    			  radiusBox._z/2),
    			  AltitudeMode.ABSOLUTE,
    			  radiusBox,
    			  0,
    			  Color.fromRGBA(0,    1, 0, 1));
    	  shapesRenderer.addShape(box1);

    	  Shape ellipsoid1 = new EllipsoidShape(new Geodetic3D(Angle.fromDegrees(39.80),
    			  Angle.fromDegrees(2.90),
    			  radius1._z),
    			  AltitudeMode.ABSOLUTE,
    			  new URL("http://serdis.dis.ulpgc.es/~atrujill/glob3m/IGO/SelectingShapes/world.jpg", false),
    			  radius1,
    			  (short)32,
    			  (short)0,
    			  false,
    			  false);
    	  shapesRenderer.addShape(ellipsoid1);

    	  Shape mercator1 = new EllipsoidShape(new Geodetic3D(Angle.fromDegrees(39.60),
    			  Angle.fromDegrees(3),
    			  radius2._x),
    			  AltitudeMode.ABSOLUTE,
    			  new URL("http://serdis.dis.ulpgc.es/~atrujill/glob3m/IGO/SelectingShapes/mercator_debug.png", false),
    			  radius2,
    			  (short)32,
    			  (short)0,
    			  false,
    			  true);
    	  shapesRenderer.addShape(mercator1);
    	  
    	  // DRAWING POINTS
    	  {
    		  Shape point = new PointShape(new Geodetic3D(Angle.fromDegrees(39.70),
    				  Angle.fromDegrees(3.30),
    				  radiusBox._z),
    				  AltitudeMode.ABSOLUTE,
    				  8,
    				  Color.fromRGBA(0, 0, 1, 1));
    		  shapesRenderer.addShape(point);
    	  }
    	  {
    		  Shape point = new PointShape(new Geodetic3D(Angle.fromDegrees(39.55),
    				  Angle.fromDegrees(3.40),
    				  radiusBox._z),
    				  AltitudeMode.ABSOLUTE,
    				  6,
    				  Color.fromRGBA(0, 0, 1, 1));
    		  shapesRenderer.addShape(point);
    	  }
    	  {
    		  Shape point = new PointShape(new Geodetic3D(Angle.fromDegrees(39.70),
    				  Angle.fromDegrees(3.50),
    				  radiusBox._z),
    				  AltitudeMode.ABSOLUTE,
    				  4,
    				  Color.fromRGBA(0, 0, 1, 1));
    		  shapesRenderer.addShape(point);
    	  }

    	  // DRAWING LINES
    	  {
    		  Shape line = new LineShape(new Geodetic3D(Angle.fromDegrees(39.69),
    				  Angle.fromDegrees(3.31),
    				  radiusBox._z),
    				  new Geodetic3D(Angle.fromDegrees(39.56),
    						  Angle.fromDegrees(3.39),
    						  radiusBox._z),
    						  AltitudeMode.ABSOLUTE,
    						  5,
    						  Color.fromRGBA(1, 0.5f, 0, 1));
    		  shapesRenderer.addShape(line);
    	  }
    	  {
    		  Shape line = new LineShape(new Geodetic3D(Angle.fromDegrees(39.56),
    				  Angle.fromDegrees(3.41),
    				  radiusBox._z),
    				  new Geodetic3D(Angle.fromDegrees(39.69),
    						  Angle.fromDegrees(3.49),
    						  radiusBox._z),   		                                
    						  AltitudeMode.ABSOLUTE,
    						  5,
    						  Color.fromRGBA(1, 0.5f, 0, 1));
    		  shapesRenderer.addShape(line);
    	  }
    	  {
    		  Shape line = new LineShape(new Geodetic3D(Angle.fromDegrees(39.70),
    				  Angle.fromDegrees(3.31),
    				  radiusBox._z),
    				  new Geodetic3D(Angle.fromDegrees(39.70),
    						  Angle.fromDegrees(3.49),
    						  radiusBox._z),
    						  AltitudeMode.ABSOLUTE,
    						  5,
    						  Color.fromRGBA(1, 0.5f, 0, 1));
    		  shapesRenderer.addShape(line);
    	  }

    	  // DRAWING RASTER LINES
    	  {
    		  Shape rasterLine = new RasterLineShape(new Geodetic2D(Angle.fromDegrees(39.40),
    				  Angle.fromDegrees(2.70)),
    				  new Geodetic2D(Angle.fromDegrees(39.40),
    						  Angle.fromDegrees(3.00)),
    						  2,
    						  Color.fromRGBA(0, 0, 1, 1));
    		  shapesRenderer.addShape(rasterLine);
    	  }

    	  // DRAWING RASTER POLYGON
    	  {
    		  java.util.ArrayList<Geodetic2D> vertices = new java.util.ArrayList<Geodetic2D>();
    		  vertices.add(new Geodetic2D(Angle.fromDegrees(39.50), Angle.fromDegrees(3.10)));
    		  vertices.add(new Geodetic2D(Angle.fromDegrees(39.38), Angle.fromDegrees(3.20)));
    		  vertices.add(new Geodetic2D(Angle.fromDegrees(39.40), Angle.fromDegrees(3.28)));
    		  vertices.add(new Geodetic2D(Angle.fromDegrees(39.60), Angle.fromDegrees(3.25)));

    		  Shape pol1 = new RasterPolygonShape(vertices,
    				  2,
    				  Color.green(),
    				  Color.fromRGBA(1, 1, 1, 0.6f));
    		  shapesRenderer.addShape(pol1);
    	  }

    	  // DRAWING JSON
    	  shapesRenderer.loadJSONSceneJS(new URL("http://serdis.dis.ulpgc.es/~atrujill/glob3m/IGO/SelectingShapes/seymour-plane.json", false), 
    			  "http://serdis.dis.ulpgc.es/~atrujill/glob3m/IGO/SelectingShapes/", 
    			  false, 
    			  new Geodetic3D(Angle.fromDegrees(39.70), Angle.fromDegrees(2.60), 7*factor),
    			  AltitudeMode.ABSOLUTE, 
    			  new ShapeLoadListener() {
    		  @Override
    		  public void onBeforeAddShape(SGShape shape) {
    			  double scale = factor/5;
    			  shape.setScale(scale, scale, scale);
    			  shape.setPitch(Angle.fromDegrees(120));
    			  shape.setHeading(Angle.fromDegrees(-110));
    		  }
    		  @Override
    		  public void dispose() {}
    		  @Override
    		  public void onAfterAddShape(SGShape shape) {}
    	  });
 
    	  /*
    	  Shape plane = SceneJSShapesParser.parseFromJSON("file:///seymour-plane.json", 
    			  "file:////", 
    			  false,
    			  new Geodetic3D(Angle.fromDegrees(39.70),
    					  Angle.fromDegrees(2.60),
    					  7*factor),
    					  AltitudeMode.ABSOLUTE);
    	  double scale = factor/5;
    	  plane.setScale(scale, scale, scale);
    	  plane.setPitch(Angle.fromDegrees(120));
    	  plane.setHeading(Angle.fromDegrees(-110));
    	  shapesRenderer.addShape(plane);*/



    	  // adding touch listener
    	  ShapeTouchListener myShapeTouchListener = new ShapeTouchListener() {
    		  Shape _selectedShape = null;
    		  
    		  public boolean touchedShape(Shape shape) {
    			      			  
    			  if (_selectedShape == null) {
    				  shape.select();
    				  _selectedShape = shape;
    			  } else {
    				  if (_selectedShape==shape) {
    					  shape.unselect();
    					  _selectedShape = null;
    				  } else {
    					  _selectedShape.unselect();
    					  _selectedShape = shape;
    					  shape.select();
    				  }
    			  }
    			  return true;
    		  }
    	  };
      
    	  shapesRenderer.setShapeTouchListener(myShapeTouchListener, true);
      }

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


      _widget = builder.createWidget();
      
      if (true) {
    	  Geodetic3D position = new Geodetic3D(Angle.fromDegrees(39.00), Angle.fromDegrees(2.90), 150000);
    	  _widget.setCameraPosition(position);
    	  _widget.setCameraHeading(Angle.fromDegrees(5.0));
    	  _widget.setCameraPitch(Angle.fromDegrees(24.0));
      }


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
