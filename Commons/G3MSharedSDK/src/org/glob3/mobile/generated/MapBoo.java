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
//class VectorStreamingRenderer;
//class MarksRenderer;


public class MapBoo
{



  public static class MBLayer
  {
    private final String _type;
    private final String _url;

    private MBLayer(String type, String url)
    {
       _type = type;
       _url = url;
    }


    public static MapBoo.MBLayer fromJSON(JSONBaseObject jsonBaseObject)
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
    
      return new MapBoo.MBLayer(type, url);
    }

    public void dispose()
    {
    }

    public final void apply(LayerSet layerSet)
    {
      if (_type.equals("URLTemplate"))
      {
        URLTemplateLayer layer = URLTemplateLayer.newMercator(_url, Sector.fullSphere(), false, 1, 18, TimeInterval.fromDays(30)); // maxLevel -  firstLevel -  isTransparent
    
        layerSet.addLayer(layer);
      }
      else
      {
        ILogger.instance().logError("MapBoo::MBLayer: unknown type \"%s\"", _type);
      }
    }

  }


  public static class MBMap
  {
    private final String _id;
    private final String _name;
    private final java.util.ArrayList<MapBoo.MBLayer> _layers;
    private java.util.ArrayList<String> _datasetsIDs = new java.util.ArrayList<String>();
    private final int _timestamp;

    private MBMap(String id, String name, java.util.ArrayList<MapBoo.MBLayer> layers, java.util.ArrayList<String> datasetsIDs, int timestamp)
    {
       _id = id;
       _name = name;
       _layers = layers;
       _datasetsIDs = datasetsIDs;
       _timestamp = timestamp;
    }

    private static java.util.ArrayList<MapBoo.MBLayer> parseLayers(JSONArray jsonArray)
    {
      java.util.ArrayList<MapBoo.MBLayer> result = new java.util.ArrayList<MapBoo.MBLayer>();
      for (int i = 0; i < jsonArray.size(); i++)
      {
        MBLayer layer = MBLayer.fromJSON(jsonArray.get(i));
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

    public static MapBoo.MBMap fromJSON(JSONBaseObject jsonBaseObject)
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
      java.util.ArrayList<MapBoo.MBLayer> layers = parseLayers(jsonObject.get("layerSet").asArray());
      java.util.ArrayList<String> datasetsIDs = parseDatasetsIDs(jsonObject.get("datasets").asArray());
      final int timestamp = (int) jsonObject.get("timestamp").asNumber().value();
    
      return new MBMap(id, name, layers, datasetsIDs, timestamp);
    }

    public void dispose()
    {
      for (int i = 0; i < _layers.size(); i++)
      {
        final MBLayer layer = _layers.get(i);
        if (layer != null)
          layer.dispose();
      }
    }

    public final String getName()
    {
      return _name;
    }

    public final String getID()
    {
      return _id;
    }

    public final void apply(LayerSet layerSet, VectorStreamingRenderer vectorStreamingRenderer)
    {
      for (int i = 0; i < _layers.size(); i++)
      {
        final MBLayer layer = _layers.get(i);
        layer.apply(layerSet);
      }
    
      for (int i = 0; i < _datasetsIDs.size(); i++)
      {
        final String datasetID = _datasetsIDs.get(i);
      }
    }
  }

  public interface MBHandler
  {
    void dispose();

    void onMapDownloadError();
    void onMapParseError();
    void onSelectedMap(MapBoo.MBMap map);
  }


  public interface MBMapsHandler
  {
    void dispose();

    void onMaps(java.util.ArrayList<MapBoo.MBMap> maps);

    void onDownloadError();
    void onParseError();

  }


  public static class MapParserAsyncTask extends GAsyncTask
  {
    private MapBoo _mapboo;
    private IByteBuffer _buffer;
    private MBMap _map;

    public MapParserAsyncTask(MapBoo mapboo, IByteBuffer buffer)
    {
       _mapboo = mapboo;
       _buffer = buffer;
       _map = null;
    }

    public void dispose()
    {
      if (_buffer != null)
         _buffer.dispose();
      if (_map != null)
         _map.dispose();
    }

    public final void runInBackground(G3MContext context)
    {
      final JSONBaseObject jsonBaseObject = IJSONParser.instance().parse(_buffer);
    
      if (_buffer != null)
         _buffer.dispose(); // release some memory
      _buffer = null;
    
      _map = MBMap.fromJSON(jsonBaseObject);
    
      if (jsonBaseObject != null)
         jsonBaseObject.dispose();
    }

    public final void onPostExecute(G3MContext context)
    {
      if (_map == null)
      {
        _mapboo.onMapParseError();
      }
      else
      {
        _mapboo.onMap(_map);
        _map = null; // moved ownership to _mapboo
      }
    }

  }


  public static class MapsParserAsyncTask extends GAsyncTask
  {
    private MBMapsHandler _handler;
    private boolean _deleteHandler;
    private IByteBuffer _buffer;

    private boolean _parseError;
    private java.util.ArrayList<MBMap> _maps = new java.util.ArrayList<MBMap>();


    public MapsParserAsyncTask(MBMapsHandler handler, boolean deleteHandler, IByteBuffer buffer)
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
        MBMap map = _maps.get(i);
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
            MBMap map = MBMap.fromJSON(jsonArray.get(i));
            if (map == null)
            {
              _parseError = true;
              break;
            }
            _maps.add(map);
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


  public static class MapBufferDownloadListener extends IBufferDownloadListener
  {
    private MapBoo _mapboo;
    private final IThreadUtils _threadUtils;

    public MapBufferDownloadListener(MapBoo mapboo, IThreadUtils threadUtils)
    {
       _mapboo = mapboo;
       _threadUtils = threadUtils;
    }

    public final void onDownload(URL url, IByteBuffer buffer, boolean expired)
    {
      _threadUtils.invokeAsyncTask(new MapParserAsyncTask(_mapboo, buffer), true);
    }

    public final void onError(URL url)
    {
      _mapboo.onMapDownloadError();
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


  public static class MapsBufferDownloadListener extends IBufferDownloadListener
  {
    private MBMapsHandler _handler;
    private boolean _deleteHandler;
    private final IThreadUtils _threadUtils;


    public MapsBufferDownloadListener(MBMapsHandler handler, boolean deleteHandler, IThreadUtils threadUtils)
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
  private MBHandler _handler;

  private String _mapID;

  private LayerSet _layerSet;
  private VectorStreamingRenderer _vectorStreamingRenderer;
  private MarksRenderer _markRenderer;
  private IDownloader _downloader;
  private final IThreadUtils _threadUtils;

  private void requestMap()
  {
    _downloader.requestBuffer(new URL(_serverURL, "/public/map/" + _mapID), DownloadPriority.HIGHEST, TimeInterval.zero(), false, new MapBufferDownloadListener(this, _threadUtils), true); // readExpired
  }
  private void applyMap(MapBoo.MBMap map)
  {
    //  renderer->addVectorSet(URL("http://192.168.1.12:8080/server-mapboo/public/VectorialStreaming/"),
    //                         "GEONames-PopulatedPlaces_LOD",
    //                         new G3MVectorStreamingDemoScene_Symbolizer(),
    //                         true, // deleteSymbolizer
    //                         //DownloadPriority::LOWER,
    //                         DownloadPriority::HIGHER,
    //                         TimeInterval::zero(),
    //                         true, // readExpired
    //                         true // verbose
    //                         );
  
    // clean current map
    _vectorStreamingRenderer.removeAllVectorSets();
    _layerSet.removeAllLayers(true);
  
    map.apply(_layerSet, _vectorStreamingRenderer);
  
    // just in case nobody put a layer
    if (_layerSet.size() == 0)
    {
      _layerSet.addLayer(new ChessboardLayer());
    }
  
    if (_handler != null)
    {
      _handler.onSelectedMap(map);
    }
  
    if (map != null)
       map.dispose();
  }


  public MapBoo(IG3MBuilder builder, URL serverURL, MBHandler handler)
  {
     _builder = builder;
     _serverURL = serverURL;
     _handler = handler;
     _layerSet = null;
    _layerSet = new LayerSet();
    _layerSet.addLayer(new ChessboardLayer());
  
    _builder.getPlanetRendererBuilder().setLayerSet(_layerSet);
  
    _markRenderer = new MarksRenderer(false, true, true); // progressiveInitialization -  renderInReverse -  readyWhenMarksReady
    _builder.addRenderer(_markRenderer);
  
    _vectorStreamingRenderer = new VectorStreamingRenderer(_markRenderer);
    _builder.addRenderer(_vectorStreamingRenderer);
  
    _downloader = _builder.getDownloader();
    _threadUtils = _builder.getThreadUtils();
  }

  public void dispose()
  {
    if (_builder != null)
       _builder.dispose();
    if (_handler != null)
       _handler.dispose();
  }

  public final void requestMaps(MBMapsHandler handler)
  {
     requestMaps(handler, true);
  }
  public final void requestMaps(MBMapsHandler handler, boolean deleteHandler)
  {
    _downloader.requestBuffer(new URL(_serverURL, "/public/map/"), DownloadPriority.HIGHEST, TimeInterval.zero(), false, new MapsBufferDownloadListener(handler, deleteHandler, _threadUtils), true); // readExpired
  }

  public final void setMapID(String mapID)
  {
    if (!_mapID.equals(mapID))
    {
      _mapID = mapID;
      requestMap();
    }
  }
  public final void setMap(MapBoo.MBMap map)
  {
    final String mapID = map.getID();
    if (!_mapID.equals(mapID))
    {
      _mapID = mapID;
  
      applyMap(map);
    }
  }

  public final void onMapDownloadError()
  {
    if (_handler != null)
    {
      _handler.onMapDownloadError();
    }
  }
  public final void onMapParseError()
  {
    if (_handler != null)
    {
      _handler.onMapParseError();
    }
  }
  public final void onMap(MapBoo.MBMap map)
  {
    applyMap(map);
  }

  public final void reloadMap()
  {
    requestMap();
  }

}