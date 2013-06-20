package org.glob3.mobile.generated; 
public class G3MCBuilder_SceneDescriptionBufferListener extends IBufferDownloadListener
{
  private G3MCBuilder _builder;

  private MapQuestLayer parseMapQuestLayer(JSONObject jsonBaseLayer, TimeInterval timeToCache)
  {
    final String imagery = jsonBaseLayer.getAsString("imagery", "<imagery not present>");
    if (imagery.compareTo("OpenAerial") == 0)
    {
      return MapQuestLayer.newOpenAerial(timeToCache);
    }

    // defaults to OSM
    return MapQuestLayer.newOSM(timeToCache);
  }

  private BingMapsLayer parseBingMapsLayer(JSONObject jsonBaseLayer, TimeInterval timeToCache)
  {
    final String key = jsonBaseLayer.getAsString("key", "");
    final String imagerySet = jsonBaseLayer.getAsString("imagerySet", "Aerial");

    return new BingMapsLayer(imagerySet, key, timeToCache);
  }

  private CartoDBLayer parseCartoDBLayer(JSONObject jsonBaseLayer, TimeInterval timeToCache)
  {
    final String userName = jsonBaseLayer.getAsString("userName", "");
    final String table = jsonBaseLayer.getAsString("table", "");

    return new CartoDBLayer(userName, table, timeToCache);
  }

  private MapBoxLayer parseMapBoxLayer(JSONObject jsonBaseLayer, TimeInterval timeToCache)
  {
    final String mapKey = jsonBaseLayer.getAsString("mapKey", "");

    return new MapBoxLayer(mapKey, timeToCache);
  }

  private WMSLayer parseWMSLayer(JSONObject jsonBaseLayer)
  {

    final String mapLayer = jsonBaseLayer.getAsString("layerName", "");
    final URL mapServerURL = new URL(jsonBaseLayer.getAsString("server", ""), false);
    final String versionStr = jsonBaseLayer.getAsString("version", "");
    WMSServerVersion mapServerVersion = WMSServerVersion.WMS_1_1_0;
    if (versionStr.compareTo("WMS_1_3_0") == 0)
    {
      mapServerVersion = WMSServerVersion.WMS_1_3_0;
    }
    final String queryLayer = jsonBaseLayer.getAsString("queryLayer", "");
    final String style = jsonBaseLayer.getAsString("style", "");
    final URL queryServerURL = new URL("", false);
    final WMSServerVersion queryServerVersion = mapServerVersion;
    final double lowerLat = jsonBaseLayer.getAsNumber("lowerLat", -90.0);
    final double lowerLon = jsonBaseLayer.getAsNumber("lowerLon", -180.0);
    final double upperLat = jsonBaseLayer.getAsNumber("upperLat", 90.0);
    final double upperLon = jsonBaseLayer.getAsNumber("upperLon", 180.0);
    final Sector sector = new Sector(new Geodetic2D(Angle.fromDegrees(lowerLat), Angle.fromDegrees(lowerLon)), new Geodetic2D(Angle.fromDegrees(upperLat), Angle.fromDegrees(upperLon)));
    String imageFormat = jsonBaseLayer.getAsString("imageFormat", "image/png");
    if (imageFormat.compareTo("JPG") == 0)
    {
      imageFormat = "image/jpeg";
    }
    final String srs = jsonBaseLayer.getAsString("projection", "EPSG_4326");
    LayerTilesRenderParameters layerTilesRenderParameters = null;
    if (srs.compareTo("EPSG_4326") == 0)
    {
      layerTilesRenderParameters = LayerTilesRenderParameters.createDefaultNonMercator(Sector.fullSphere());
    }
    else if (srs.compareTo("EPSG_900913") == 0)
    {
      layerTilesRenderParameters = LayerTilesRenderParameters.createDefaultMercator(0, 17);
    }
    final boolean isTransparent = jsonBaseLayer.getAsBoolean("transparent", false);
    final double expiration = jsonBaseLayer.getAsNumber("expiration", 0);
    final long milliseconds = IMathUtils.instance().round(expiration);
    final TimeInterval timeToCache = TimeInterval.fromMilliseconds(milliseconds);
    final boolean readExpired = jsonBaseLayer.getAsBoolean("acceptExpiration", false);

    return new WMSLayer(mapLayer, mapServerURL, mapServerVersion, queryLayer, queryServerURL, queryServerVersion, sector, imageFormat, (srs.compareTo("EPSG_4326") == 0) ? "EPSG:4326" : "EPSG:900913", style, isTransparent, null, timeToCache, readExpired, layerTilesRenderParameters);
  }

  private Layer parseLayer(JSONBaseObject jsonBaseObjectLayer)
  {

    if (jsonBaseObjectLayer.asNull() != null)
    {
      return null;
    }

    final TimeInterval defaultTimeToCache = TimeInterval.fromDays(30);

    final JSONObject jsonBaseLayer = jsonBaseObjectLayer.asObject();
    if (jsonBaseLayer == null)
    {
      ILogger.instance().logError("Layer is not a json object");
      return null;
    }

    final String layerType = jsonBaseLayer.getAsString("layer", "<layer not present>");
    if (layerType.compareTo("OSM") == 0)
    {
      return new OSMLayer(defaultTimeToCache);
    }
    else if (layerType.compareTo("MapQuest") == 0)
    {
      return parseMapQuestLayer(jsonBaseLayer, defaultTimeToCache);
    }
    else if (layerType.compareTo("BingMaps") == 0)
    {
      return parseBingMapsLayer(jsonBaseLayer, defaultTimeToCache);
    }
    else if (layerType.compareTo("CartoDB") == 0)
    {
      return parseCartoDBLayer(jsonBaseLayer, defaultTimeToCache);
    }
    else if (layerType.compareTo("MapBox") == 0)
    {
      return parseMapBoxLayer(jsonBaseLayer, defaultTimeToCache);
    }
    else if (layerType.compareTo("WMS") == 0)
    {
      return parseWMSLayer(jsonBaseLayer);
    }
    else
    {
      ILogger.instance().logError("Unsupported layer type \"%s\"", layerType);
      return null;
    }
  }

  public G3MCBuilder_SceneDescriptionBufferListener(G3MCBuilder builder)
  {
     _builder = builder;
  }


  public final void onDownload(URL url, IByteBuffer buffer, boolean expired)
  {

    final JSONBaseObject jsonBaseObject = IJSONParser.instance().parse(buffer, true);

    if (jsonBaseObject == null)
    {
      ILogger.instance().logError("Can't parse SceneJSON from %s", url.getPath());
    }
    else
    {
      final JSONObject jsonObject = jsonBaseObject.asObject();
      if (jsonObject == null)
      {
        ILogger.instance().logError("Invalid SceneJSON (1)");
      }
      else
      {
        final JSONString error = jsonObject.getAsString("error");
        if (error == null)
        {
          final int timestamp = (int) jsonObject.getAsNumber("ts", 0);

          if (_builder.getSceneTimestamp() != timestamp)
          {
            final JSONString jsonUser = jsonObject.getAsString("user");
            if (jsonUser != null)
            {
              _builder.setSceneUser(jsonUser.value());
            }

            //id

            final JSONString jsonName = jsonObject.getAsString("name");
            if (jsonName != null)
            {
              _builder.setSceneName(jsonName.value());
            }

            final JSONString jsonDescription = jsonObject.getAsString("description");
            if (jsonDescription != null)
            {
              _builder.setSceneDescription(jsonDescription.value());
            }

            final JSONString jsonBGColor = jsonObject.getAsString("bgColor");
            if (jsonBGColor != null)
            {
              final Color bgColor = Color.parse(jsonBGColor.value());
              if (bgColor == null)
              {
                ILogger.instance().logError("Invalid format in attribute 'bgColor' (%s)", jsonBGColor.value());
              }
              else
              {
                _builder.setSceneBackgroundColor(bgColor);
                if (bgColor != null)
                   bgColor.dispose();
              }
            }

            final JSONBaseObject jsonBaseLayer = jsonObject.get("baseLayer");
            if (jsonBaseLayer != null)
            {
              _builder.setSceneBaseLayer(parseLayer(jsonBaseLayer));
            }

            final JSONBaseObject jsonOverlayLayer = jsonObject.get("overlayLayer");
            if (jsonOverlayLayer != null)
            {
              _builder.setSceneOverlayLayer(parseLayer(jsonOverlayLayer));
            }

            //tags

            _builder.setSceneTimestamp(timestamp);
          }
        }
        else
        {
          ILogger.instance().logError("Server Error: %s", error.value());
        }
      }

      if (jsonBaseObject != null)
         jsonBaseObject.dispose();
    }

    if (buffer != null)
       buffer.dispose();

    //    int __TODO_flag_initialization_task_as_initialized;
    //    _initializationTask->setInitialized(true);
  }

  public final void onError(URL url)
  {
    ILogger.instance().logError("Can't download SceneJSON from %s", url.getPath());
  }

  public final void onCancel(URL url)
  {
    // do nothing
  }

  public final void onCanceledDownload(URL url, IByteBuffer buffer, boolean expired)
  {
    // do nothing
  }

}