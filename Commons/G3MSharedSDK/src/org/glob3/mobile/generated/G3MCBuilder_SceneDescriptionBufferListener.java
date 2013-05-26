package org.glob3.mobile.generated; 
public class G3MCBuilder_SceneDescriptionBufferListener extends IBufferDownloadListener
{
  private G3MCBuilder _builder;

  private Layer parseLayer(JSONObject jsonBaseLayer)
  {
    final TimeInterval defaultTimeToCache = TimeInterval.fromDays(30);

    final String layerType = jsonBaseLayer.getAsString("layer", "<layer not present>");
    if (layerType.compareTo("OSM") == 0)
    {
      return new OSMLayer(defaultTimeToCache);
    }
    else if (layerType.compareTo("MapQuest") == 0)
    {
      final String imagery = jsonBaseLayer.getAsString("imagery", "<imagery not present>");
      if (imagery.compareTo("OpenAerial") == 0)
      {
        return MapQuestLayer.newOpenAerial(defaultTimeToCache);
      }
      else if (imagery.compareTo("OSM") == 0)
      {
        return MapQuestLayer.newOSM(defaultTimeToCache);
      }
      else
      {
        ILogger.instance().logError("Unsupported MapQuest imagery \"%s\"", imagery);
        return null;
      }
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
          final int timestamp = (int) jsonObject.getAsNumber("ts", 0);

          if (_builder.getSceneTimestamp() != timestamp)
          {
            final String user = jsonObject.getAsString("user", "<user not present>");
            final String name = jsonObject.getAsString("name", "<name not present>");

            final JSONObject jsonBaseLayer = jsonObject.getAsObject("baseLayer");

            if (jsonBaseLayer == null)
            {
              ILogger.instance().logError("Attribute 'baseLayer' not found in SceneJSON");
            }
            else
            {
              Layer baseLayer = parseLayer(jsonBaseLayer);
              _builder.changeBaseLayer(baseLayer);
              _builder.setSceneTimestamp(timestamp);
            }
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