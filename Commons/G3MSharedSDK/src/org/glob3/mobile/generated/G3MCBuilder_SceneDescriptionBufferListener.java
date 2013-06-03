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

  private Layer parseLayer(JSONObject jsonBaseLayer)
  {
    final TimeInterval defaultTimeToCache = TimeInterval.fromDays(30);

    /*
     "OSM"
     "MapQuest"
     "BingMaps"
     "MapBox"
     "CartoDB"
     
     "WMS"
     */

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

    final JSONBaseObject jsonBaseObject = IJSONParser.instance().parse(buffer);

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

//          {
//            user: "aaa",
//            id: "2g59wh610g6c1kmkt0l",
//            name: "Example10",
//            description: "Description",
//            realTime: "0",
//            iconURL: "http://http://design.jboss.org/arquillian/logo/final/arquillian_icon_256px.png",
//            bgColor: "001933",
//            baseLayer: {
//              layer: "MapQuest",
//              imagery: "OSM"
//            },
//            tags: [
//                    ""
//                  ],
//            ts: 27
//          }

          final int timestamp = (int) jsonObject.getAsNumber("ts", 0);

          if (_builder.getSceneTimestamp() != timestamp)
          {
            final JSONString jsonUser = jsonObject.getAsString("user");
            if (jsonUser == null)
            {
              ILogger.instance().logError("Attribute 'user' not found in SceneJSON");
            }
            else
            {
              _builder.setSceneUser(jsonUser.value());
            }

            //id

            final JSONString jsonName = jsonObject.getAsString("name");
            if (jsonName == null)
            {
              ILogger.instance().logError("Attribute 'name' not found in SceneJSON");
            }
            else
            {
              _builder.setSceneName(jsonName.value());
            }

            final JSONString jsonDescription = jsonObject.getAsString("description");
            if (jsonDescription == null)
            {
              ILogger.instance().logError("Attribute 'description' not found in SceneJSON");
            }
            else
            {
              _builder.setSceneDescription(jsonDescription.value());
            }

            //realTime
            //iconURL
            //bgColor

            final JSONString jsonBGColor = jsonObject.getAsString("bgColor");
            if (jsonBGColor == null)
            {
              ILogger.instance().logError("Attribute 'bgColor' not found in SceneJSON");
            }
            else
            {
              final Color bgColor = Color.parse(jsonBGColor.value());
              if (bgColor == null)
              {
                ILogger.instance().logError("Invalid format in attribute 'bgColor' (%s)", jsonBGColor.value());
              }
              else
              {
                _builder.setSceneBackgroundColor(bgColor);
              }
            }

            final JSONObject jsonBaseLayer = jsonObject.getAsObject("baseLayer");
            if (jsonBaseLayer == null)
            {
              ILogger.instance().logError("Attribute 'baseLayer' not found in SceneJSON");
            }
            else
            {
              Layer baseLayer = parseLayer(jsonBaseLayer);
              if (baseLayer == null)
              {
                ILogger.instance().logError("Can't parse attribute 'baseLayer' in SceneJSON");
              }
              else
              {
                _builder.changeBaseLayer(baseLayer);
              }
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