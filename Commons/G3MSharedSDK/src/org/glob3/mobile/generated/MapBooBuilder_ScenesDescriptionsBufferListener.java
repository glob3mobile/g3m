package org.glob3.mobile.generated; 
public class MapBooBuilder_ScenesDescriptionsBufferListener extends IBufferDownloadListener
{
  private MapBooBuilderScenesDescriptionsListener _listener;
  private final boolean _autoDelete;

  public MapBooBuilder_ScenesDescriptionsBufferListener(MapBooBuilderScenesDescriptionsListener listener, boolean autoDelete)
  {
     _listener = listener;
     _autoDelete = autoDelete;

  }


  public final void onDownload(URL url, IByteBuffer buffer, boolean expired)
  {

    final JSONBaseObject jsonBaseObject = IJSONParser.instance().parse(buffer);

    if (jsonBaseObject == null)
    {
      ILogger.instance().logError("Can't parse ScenesDescriptionJSON from %s", url.getPath());
      onError(url);
    }
    else
    {
      final JSONArray jsonScenesDescriptions = jsonBaseObject.asArray();
      if (jsonScenesDescriptions == null)
      {
        ILogger.instance().logError("ScenesDescriptionJSON: invalid format (1)");
        onError(url);
      }
      else
      {
        java.util.ArrayList<MapBooSceneDescription> scenesDescriptions = new java.util.ArrayList<MapBooSceneDescription>();

        final int size = jsonScenesDescriptions.size();

        for (int i = 0; i < size; i++)
        {
          final JSONObject jsonSceneDescription = jsonScenesDescriptions.getAsObject(i);
          if (jsonSceneDescription == null)
          {
            ILogger.instance().logError("ScenesDescriptionJSON: invalid format (2) at index #%d", i);
          }
          else
          {
            final String id = jsonSceneDescription.getAsString("id", "<invalid id>");
            final String user = jsonSceneDescription.getAsString("user", "<invalid user>");
            final String name = jsonSceneDescription.getAsString("name", "<invalid name>");
            final String description = jsonSceneDescription.getAsString("description", "");
            final String iconURL = jsonSceneDescription.getAsString("iconURL", "<invalid iconURL>");

            java.util.ArrayList<String> tags = new java.util.ArrayList<String>();
            final JSONArray jsonTags = jsonSceneDescription.getAsArray("tags");
            if (jsonTags == null)
            {
              ILogger.instance().logError("ScenesDescriptionJSON: invalid format (3) at index #%d", i);
            }
            else
            {
              final int tagsCount = jsonTags.size();
              for (int j = 0; j < tagsCount; j++)
              {
                final String tag = jsonTags.getAsString(j, "");
                if (tag.length() > 0)
                {
                  tags.add(tag);
                }
              }
            }

            scenesDescriptions.add(new MapBooSceneDescription(id, user, name, description, iconURL, tags));

          }
        }

        _listener.onDownload(scenesDescriptions);
        if (_autoDelete)
        {
          if (_listener != null)
             _listener.dispose();
        }
      }

      if (jsonBaseObject != null)
         jsonBaseObject.dispose();
    }

    if (buffer != null)
       buffer.dispose();
  }

  public final void onError(URL url)
  {
    _listener.onError();
    if (_autoDelete)
    {
      if (_listener != null)
         _listener.dispose();
    }
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