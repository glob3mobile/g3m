package org.glob3.mobile.generated; 
//
//  MapBoo.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 8/21/15.
//
//

//
//  MapBoo.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 8/21/15.
//
//



//class IG3MBuilder;
//class LayerSet;
//class IDownloader;
//class JSONBaseObject;
//class JSONArray;

public class MapBoo
{



  public abstract static class Layer
  {
    private final String _type;
    private final String _url;

    private Layer(String type, String url)
    {
       _type = type;
       _url = url;
    }

    public static MapBoo.Layer fromJSON(JSONBaseObject jsonBaseObject)
    {
      if (jsonBaseObject == null)
      {
        return null;
      }
    
      final JSONObject jsonObject = jsonBaseObject.asObject();
      if (jsonObject == null)
      {
        return null;
      }
    
      final String type = jsonObject.get("type").asString().value();
      final String url = jsonObject.getAsString("url", "");
    
      return new MapBoo.Layer(type, url);
    }

  }


  public static class Map
  {
    private final String _id;
    private final String _name;
    private final java.util.ArrayList<MapBoo.Layer> _layers = new java.util.ArrayList<MapBoo.Layer>();
    private java.util.ArrayList<String> _datasetsIDs = new java.util.ArrayList<String>();
    private final int _timestamp;

    private Map(String id, String name, java.util.ArrayList<MapBoo.Layer> layers, java.util.ArrayList<String> datasetsIDs, int timestamp)
    {
       _id = id;
       _name = name;
       _layers = layers;
       _datasetsIDs = datasetsIDs;
       _timestamp = timestamp;
    }

    private static java.util.ArrayList<MapBoo.Layer> parseLayers(JSONArray jsonArray)
    {
      final java.util.ArrayList<MapBoo.Layer> result = new java.util.ArrayList<MapBoo.Layer>();
      for (int i = 0; i < jsonArray.size(); i++)
      {
        final Layer layer = Layer.fromJSON(jsonArray.get(i));
        if (layer != null)
        {
          result.add(layer);
        }
      }
      return result;
    }
    private static java.util.ArrayList<String> parseDatasetsIDs(JSONArray jsonArray)
    {
      java.util.ArrayList<String> result = new java.util.ArrayList<String>();
      for (int i = 0; i < jsonArray.size(); i++)
      {
        result.add(jsonArray.get(i).asString().value());
      }
      return result;
    }

    public static MapBoo.Map fromJSON(JSONBaseObject jsonBaseObject)
    {
      if (jsonBaseObject == null)
      {
        return null;
      }
    
      final JSONObject jsonObject = jsonBaseObject.asObject();
      if (jsonObject == null)
      {
        return null;
      }
    
      final String id = jsonObject.get("id").asString().value();
      final String name = jsonObject.get("name").asString().value();
      final java.util.ArrayList<MapBoo.Layer> layers = parseLayers(jsonObject.get("layerSet").asArray());
      java.util.ArrayList<String> datasetsIDs = parseDatasetsIDs(jsonObject.get("datasets").asArray());
      final int timestamp = (int) jsonObject.get("timestamp").asNumber().value();
    
      return new Map(id, name, layers, datasetsIDs, timestamp);
    }

    public void dispose()
    {
      for (int i = 0; i < _layers.size(); i++)
      {
        final Layer layer = _layers.get(i);
        if (layer != null)
           layer.dispose();
      }
    }
  }


  public interface MapsHandler
  {
    void dispose();

    void onMaps(java.util.ArrayList<MapBoo.Map> maps);

    void onDownloadError();
    void onParseError();

  }

  public static class MapsParserAsyncTask extends GAsyncTask
  {
    private MapsHandler _handler;
    private boolean _deleteHandler;
    private IByteBuffer _buffer;
    private boolean _parseError;
    private final java.util.ArrayList<Map> _maps = new java.util.ArrayList<Map>();


    public MapsParserAsyncTask(MapsHandler handler, boolean deleteHandler, IByteBuffer buffer)
    {
       _handler = handler;
       _deleteHandler = deleteHandler;
       _buffer = buffer;
       _parseError = true;
    }

    public void dispose()
    {
      if (_buffer != null)
         _buffer.dispose();
    
      for (int i = 0; i < _maps.size(); i++)
      {
        final Map map = _maps.get(i);
        if (map != null)
           map.dispose();
      }
    
      if (_deleteHandler && (_handler != null))
      {
        if (_handler != null)
           _handler.dispose();
      }
    }

    public final void runInBackground(G3MContext context)
    {
      final JSONBaseObject jsonBaseObject = IJSONParser.instance().parse(_buffer);
    
      if (_buffer != null)
         _buffer.dispose(); // release some memory
      _buffer = null;
    
      if (jsonBaseObject != null)
      {
        final JSONArray jsonArray = jsonBaseObject.asArray();
        if (jsonArray != null)
        {
          _parseError = false;
    
          for (int i = 0; i < jsonArray.size(); i++)
          {
            final Map map = Map.fromJSON(jsonArray.get(i));
            if (map == null)
            {
              _parseError = true;
              break;
            }
            else
            {
              _maps.add(map);
            }
          }
        }
    
        if (jsonBaseObject != null)
           jsonBaseObject.dispose();
      }
    }

    public final void onPostExecute(G3MContext context)
    {
      if (_parseError)
      {
        _handler.onParseError();
      }
      else
      {
        _handler.onMaps(_maps);
        _maps.clear(); // moved maps ownership to _handler
      }
    }

  }


  public static class MapsBufferDownloadListener extends IBufferDownloadListener
  {
    private MapsHandler _handler;
    private boolean _deleteHandler;
    private final IThreadUtils _threadUtils;


    public MapsBufferDownloadListener(MapsHandler handler, boolean deleteHandler, IThreadUtils threadUtils)
    {
       _handler = handler;
       _deleteHandler = deleteHandler;
       _threadUtils = threadUtils;
    }

    public void dispose()
    {
      if (_deleteHandler && (_handler != null))
      {
        if (_handler != null)
           _handler.dispose();
      }
    }

    public final void onDownload(URL url, IByteBuffer buffer, boolean expired)
    {
      _threadUtils.invokeAsyncTask(new MapsParserAsyncTask(_handler, _deleteHandler, buffer), true);
      _handler = null; // moves ownership to MapsParserAsyncTask
    }

    public final void onError(URL url)
    {
      _handler.onDownloadError();
    
      if (_deleteHandler)
      {
        if (_handler != null)
           _handler.dispose();
        _handler = null;
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


  private IG3MBuilder _builder;
  private final URL _serverURL;

  private LayerSet _layerSet;
  private IDownloader _downloader;
  private final IThreadUtils _threadUtils;

  public MapBoo(IG3MBuilder builder, URL serverURL)
  {
     _builder = builder;
     _serverURL = serverURL;
     _layerSet = null;
    _layerSet = new LayerSet();
    _layerSet.addLayer(new ChessboardLayer());
  
    _builder.getPlanetRendererBuilder().setLayerSet(_layerSet);
  
    _downloader = _builder.getDownloader();
    _threadUtils = _builder.getThreadUtils();
  }

  public void dispose()
  {
    if (_builder != null)
       _builder.dispose();
  }

  public final void requestMaps(MapsHandler handler)
  {
     requestMaps(handler, true);
  }
  public final void requestMaps(MapsHandler handler, boolean deleteHandler)
  {
  
    _downloader.requestBuffer(new URL(_serverURL, "/public/map/"), DownloadPriority.HIGHEST, TimeInterval.zero(), false, new MapsBufferDownloadListener(handler, deleteHandler, _threadUtils), true); // readExpired
  }

}