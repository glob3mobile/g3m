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
//class MarksRenderer;


public class MapBoo
{


  public static class MBLayer extends RCObject
  {
    private final String _type;
    private final String _url;
    private final boolean _verbose;

    private MBLayer(String type, String url, boolean verbose)
    {
       _type = type;
       _url = url;
       _verbose = verbose;
    }

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//    MBLayer(MBLayer that);

    public static MapBoo.MBLayer fromJSON(JSONBaseObject jsonBaseObject, boolean verbose)
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
    
      return new MapBoo.MBLayer(type, url, verbose);
    }

    public void dispose()
    {
      if (_verbose)
      {
        ILogger.instance().logInfo("MapBoo: deleting layer");
      }
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

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following type could not be found.
//  class MBHandler;


  public static class MBDataset extends RCObject
  {
    private MBHandler _handler;
    private final String _id;
    private final String _name;
    private java.util.ArrayList<String> _labelingCriteria = new java.util.ArrayList<String>();
    private java.util.ArrayList<String> _infoCriteria = new java.util.ArrayList<String>();
    private final int _timestamp;

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//    MBDataset(MBDataset that);

    private MBDataset(MBHandler handler, String id, String name, java.util.ArrayList<String> labelingCriteria, java.util.ArrayList<String> infoCriteria, int timestamp)
    {
       _handler = handler;
       _id = id;
       _name = name;
       _labelingCriteria = labelingCriteria;
       _infoCriteria = infoCriteria;
       _timestamp = timestamp;
    }

    private String createMarkLabel(JSONObject properties)
    {
      final int criteriaSize = _labelingCriteria.size();
      if ((criteriaSize == 0) || (properties.size() == 0))
      {
        return "<label>";
      }
      else if (criteriaSize == 1)
      {
        return JSONBaseObject.toString(properties.get(_labelingCriteria.get(0)));
      }
      else
      {
        IStringBuilder labelBuilder = IStringBuilder.newStringBuilder();
        for (int i = 0; i < criteriaSize; i++)
        {
          if (i > 0)
          {
            labelBuilder.addString(" ");
          }
          final String value = JSONBaseObject.toString(properties.get(_labelingCriteria.get(i)));
          labelBuilder.addString(value);
        }
    
        final String label = labelBuilder.getString();
        if (labelBuilder != null)
           labelBuilder.dispose();
        return label;
      }
    }
    private MarkTouchListener createMarkTouchListener(JSONObject properties)
    {
      if (_handler == null)
      {
        return null;
      }
    
      final int criteriaSize = _infoCriteria.size();
      if (criteriaSize == 0)
      {
        return null;
      }
    
      JSONObject infoProperties = new JSONObject();
      for (int i = 0; i < criteriaSize; i++)
      {
        final String criteria = _infoCriteria.get(i);
        final JSONBaseObject value = properties.get(criteria);
        if (value != null)
        {
          infoProperties.put(criteria, value.deepCopy());
        }
      }
    
      return new MBFeatureMarkTouchListener(_handler, infoProperties);
    }

    public void dispose()
    {
      super.dispose();
    }

    public static MapBoo.MBDataset fromJSON(MBHandler handler, JSONBaseObject jsonBaseObject, boolean verbose)
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
      java.util.ArrayList<String> labelingCriteria = jsonObject.getAsArray("labelingCriteria").asStringVector();
      java.util.ArrayList<String> infoCriteria = jsonObject.getAsArray("infoCriteria").asStringVector();
      final int timestamp = (int) jsonObject.get("timestamp").asNumber().value();
    
      return new MBDataset(handler, id, name, labelingCriteria, infoCriteria, timestamp);
    }


    public final void apply(URL serverURL, VectorStreamingRenderer vectorStreamingRenderer)
    {
      String properties = "";
      for (int i = 0; i < _labelingCriteria.size(); i++)
      {
        properties += _labelingCriteria.get(i) + "|";
      }
      for (int i = 0; i < _infoCriteria.size(); i++)
      {
        properties += _infoCriteria.get(i) + "|";
      }
    
      vectorStreamingRenderer.addVectorSet(new URL(serverURL, "/public/v1/VectorialStreaming/"), _id, properties, new MBDatasetVectorSetSymbolizer(this), true, DownloadPriority.MEDIUM, TimeInterval.zero(), true, true, false); // haltOnError -  verbose -  readExpired -  deleteSymbolizer
    }

    public final Mark createMark(GEO2DPointGeometry geometry)
    {
      final GEOFeature feature = geometry.getFeature();
      final JSONObject properties = feature.getProperties();
      final Geodetic2D position = geometry.getPosition();
    
      return new Mark(createMarkLabel(properties), new Geodetic3D(position, 0), AltitudeMode.ABSOLUTE, 0, 18, Color.newFromRGBA(1, 1, 1, 1), Color.newFromRGBA(0, 0, 0, 1), null, true, createMarkTouchListener(properties), true); // autoDeleteListener -  autoDeleteUserData -  userData -  labelShadowColor -  labelFontColor -  labelFontSize -  minDistanceToCamera
    }

  }


  public static class MBDatasetVectorSetSymbolizer extends VectorStreamingRenderer.VectorSetSymbolizer
  {
    private final MBDataset _dataset;

    public MBDatasetVectorSetSymbolizer(MBDataset dataset)
    {
       _dataset = dataset;
      _dataset._retain();
    }

    public void dispose()
    {
      _dataset._release();
      super.dispose();
    }

    public final Mark createMark(GEO2DPointGeometry geometry)
    {
      return _dataset.createMark(geometry);
    }
  }


  public static class MBMap
  {
    private MBHandler _handler;
    private final String _id;
    private final String _name;
    private java.util.ArrayList<MapBoo.MBLayer> _layers = new java.util.ArrayList<MapBoo.MBLayer>();
    private java.util.ArrayList<MapBoo.MBDataset> _datasets = new java.util.ArrayList<MapBoo.MBDataset>();
    private final int _timestamp;
    private final boolean _verbose;

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//    MBMap(MBMap that);

    private MBMap(MBHandler handler, String id, String name, java.util.ArrayList<MapBoo.MBLayer> layers, java.util.ArrayList<MapBoo.MBDataset> datasets, int timestamp, boolean verbose)
    {
       _handler = handler;
       _id = id;
       _name = name;
       _layers = layers;
       _datasets = datasets;
       _timestamp = timestamp;
       _verbose = verbose;
    }

    private static java.util.ArrayList<MapBoo.MBLayer> parseLayers(JSONArray jsonArray, boolean verbose)
    {
      java.util.ArrayList<MapBoo.MBLayer> result = new java.util.ArrayList<MapBoo.MBLayer>();
      for (int i = 0; i < jsonArray.size(); i++)
      {
        MBLayer layer = MBLayer.fromJSON(jsonArray.get(i), verbose);
        if (layer != null)
        {
          result.add(layer);
        }
      }
      return result;
    }
    private static java.util.ArrayList<MapBoo.MBDataset> parseDatasets(MBHandler handler, JSONArray jsonArray, boolean verbose)
    {
      java.util.ArrayList<MapBoo.MBDataset> result = new java.util.ArrayList<MapBoo.MBDataset>();
      for (int i = 0; i < jsonArray.size(); i++)
      {
        MBDataset dataset = MBDataset.fromJSON(handler, jsonArray.get(i), verbose);
        if (dataset != null)
        {
          result.add(dataset);
        }
      }
      return result;
    }


    public static MapBoo.MBMap fromJSON(MBHandler handler, JSONBaseObject jsonBaseObject, boolean verbose)
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
      java.util.ArrayList<MapBoo.MBLayer> layers = parseLayers(jsonObject.get("layerSet").asArray(), verbose);
      java.util.ArrayList<MapBoo.MBDataset> datasets = parseDatasets(handler, jsonObject.get("datasets").asArray(), verbose);
      final int timestamp = (int) jsonObject.get("timestamp").asNumber().value();
    
      return new MBMap(handler, id, name, layers, datasets, timestamp, verbose);
    }

    public void dispose()
    {
      if (_verbose)
      {
        ILogger.instance().logInfo("MapBoo: deleting map");
      }
    
      for (int i = 0; i < _datasets.size(); i++)
      {
        MBDataset dataset = _datasets.get(i);
        dataset._release();
      }
    
      for (int i = 0; i < _layers.size(); i++)
      {
        MBLayer layer = _layers.get(i);
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

    public final void apply(URL serverURL, LayerSet layerSet, VectorStreamingRenderer vectorStreamingRenderer)
    {
      for (int i = 0; i < _layers.size(); i++)
      {
        MBLayer layer = _layers.get(i);
        layer.apply(layerSet);
      }
    
      for (int i = 0; i < _datasets.size(); i++)
      {
        MBDataset dataset = _datasets.get(i);
        dataset.apply(serverURL, vectorStreamingRenderer);
      }
    }
  }


  public interface MBHandler
  {
    void dispose();

    void onMapDownloadError();
    void onMapParseError();
    void onSelectedMap(MapBoo.MBMap map);

    void onFeatureTouched(JSONObject properties);
  }


  public static class MBFeatureMarkTouchListener extends MarkTouchListener
  {
    private MBHandler _handler;
    private final JSONObject _properties;

    public MBFeatureMarkTouchListener(MBHandler handler, JSONObject properties)
    {
       _handler = handler;
       _properties = properties;
    }

    public final boolean touchedMark(Mark mark)
    {
      _handler.onFeatureTouched(_properties);
    }

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
    private MBHandler _handler;
    private IByteBuffer _buffer;
    private MBMap _map;
    private final boolean _verbose;

    public MapParserAsyncTask(MapBoo mapboo, MBHandler handler, IByteBuffer buffer, boolean verbose)
    {
       _mapboo = mapboo;
       _handler = handler;
       _buffer = buffer;
       _verbose = verbose;
       _map = null;
    }

    public void dispose()
    {
      if (_buffer != null)
         _buffer.dispose();
      if (_map != null)
         _map.dispose();
      super.dispose();
    }

    public final void runInBackground(G3MContext context)
    {
      if (_verbose)
      {
        ILogger.instance().logInfo("MapBoo: parsing map");
      }
    
      final JSONBaseObject jsonBaseObject = IJSONParser.instance().parse(_buffer);
    
      if (_buffer != null)
         _buffer.dispose(); // release some memory
      _buffer = null;
    
      _map = MBMap.fromJSON(_handler, jsonBaseObject, _verbose);
    
      if (jsonBaseObject != null)
         jsonBaseObject.dispose();
    }

    public final void onPostExecute(G3MContext context)
    {
      if (_map == null)
      {
        if (_verbose)
        {
          ILogger.instance().logInfo("MapBoo: error parsing map");
        }
    
        _mapboo.onMapParseError();
      }
      else
      {
        if (_verbose)
        {
          ILogger.instance().logInfo("MapBoo: parsed map");
        }
    
        _mapboo.onMap(_map);
        _map = null; // moved ownership to _mapboo
      }
    }

  }


  public static class MapsParserAsyncTask extends GAsyncTask
  {
    private MBHandler _handler;
    private MBMapsHandler _mapsHandler;
    private boolean _deleteHandler;
    private IByteBuffer _buffer;
    private final boolean _verbose;

    private boolean _parseError;
    private java.util.ArrayList<MBMap> _maps = new java.util.ArrayList<MBMap>();


    public MapsParserAsyncTask(MBHandler handler, MBMapsHandler mapsHandler, boolean deleteHandler, IByteBuffer buffer, boolean verbose)
    {
       _handler = handler;
       _mapsHandler = mapsHandler;
       _deleteHandler = deleteHandler;
       _buffer = buffer;
       _verbose = verbose;
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
    
      if (_deleteHandler && (_mapsHandler != null))
      {
        if (_mapsHandler != null)
           _mapsHandler.dispose();
      }
      super.dispose();
    }

    public final void runInBackground(G3MContext context)
    {
      if (_verbose)
      {
        ILogger.instance().logInfo("MapBoo: parsing maps");
      }
    
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
            MBMap map = MBMap.fromJSON(_handler, jsonArray.get(i), _verbose);
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
        if (_verbose)
        {
          ILogger.instance().logInfo("MapBoo: error parsing maps");
        }
    
        _mapsHandler.onParseError();
      }
      else
      {
        if (_verbose)
        {
          ILogger.instance().logInfo("MapBoo: parsed maps");
        }
    
        _mapsHandler.onMaps(_maps);
        _maps.clear(); // moved maps ownership to _handler
      }
    }

  }


  public static class MapBufferDownloadListener extends IBufferDownloadListener
  {
    private MapBoo _mapboo;
    private MBHandler _handler;
    private final IThreadUtils _threadUtils;
    private final boolean _verbose;
    public MapBufferDownloadListener(MapBoo mapboo, MBHandler handler, IThreadUtils threadUtils, boolean verbose)
    {
       _mapboo = mapboo;
       _handler = handler;
       _threadUtils = threadUtils;
       _verbose = verbose;
    }

    public final void onDownload(URL url, IByteBuffer buffer, boolean expired)
    {
      if (_verbose)
      {
        ILogger.instance().logInfo("MapBoo: downloaded map");
      }
    
      _threadUtils.invokeAsyncTask(new MapParserAsyncTask(_mapboo, _handler, buffer, _verbose), true);
    }

    public final void onError(URL url)
    {
      if (_verbose)
      {
        ILogger.instance().logInfo("MapBoo: error downloading map");
      }
    
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
    private MBHandler _handler;
    private MBMapsHandler _mapsHandler;
    private boolean _deleteHandler;
    private final IThreadUtils _threadUtils;
    private final boolean _verbose;


    public MapsBufferDownloadListener(MBHandler handler, MBMapsHandler mapsHandler, boolean deleteHandler, IThreadUtils threadUtils, boolean verbose)
    {
       _handler = handler;
       _mapsHandler = mapsHandler;
       _deleteHandler = deleteHandler;
       _threadUtils = threadUtils;
       _verbose = verbose;
    }

    public void dispose()
    {
      if (_deleteHandler && (_mapsHandler != null))
      {
        if (_mapsHandler != null)
           _mapsHandler.dispose();
      }
      super.dispose();
    }

    public final void onDownload(URL url, IByteBuffer buffer, boolean expired)
    {
      if (_verbose)
      {
        ILogger.instance().logInfo("MapBoo: downloaded maps");
      }
      _threadUtils.invokeAsyncTask(new MapsParserAsyncTask(_handler, _mapsHandler, _deleteHandler, buffer, _verbose), true);
      _mapsHandler = null; // moves ownership to MapsParserAsyncTask
    }

    public final void onError(URL url)
    {
      if (_verbose)
      {
        ILogger.instance().logInfo("MapBoo: error downloading maps");
      }
      _mapsHandler.onDownloadError();
    
      if (_deleteHandler)
      {
        if (_mapsHandler != null)
           _mapsHandler.dispose();
        _mapsHandler = null;
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
  private final boolean _verbose;

  private String _mapID;

  private LayerSet _layerSet;
  private VectorStreamingRenderer _vectorStreamingRenderer;
  private MarksRenderer _markRenderer;
  private IDownloader _downloader;
  private final IThreadUtils _threadUtils;

  private void requestMap()
  {
    if (_verbose)
    {
      ILogger.instance().logInfo("MapBoo: requesting map \"%s\"", _mapID);
    }
  
    _downloader.requestBuffer(new URL(_serverURL, "/public/v1/map/" + _mapID), DownloadPriority.HIGHEST, TimeInterval.zero(), false, new MapBufferDownloadListener(this, _handler, _threadUtils, _verbose), true); // readExpired
  }
  private void applyMap(MapBoo.MBMap map)
  {
    if (_verbose)
    {
      ILogger.instance().logInfo("MapBoo: applying map \"%s\"", map.getID());
    }
  
    // clean current map
    _vectorStreamingRenderer.removeAllVectorSets();
    _layerSet.removeAllLayers(true);
  
    map.apply(_serverURL, _layerSet, _vectorStreamingRenderer);
  
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


//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  MapBoo(MapBoo that);

  public MapBoo(IG3MBuilder builder, URL serverURL, MBHandler handler, boolean verbose)
  {
     _builder = builder;
     _serverURL = serverURL;
     _handler = handler;
     _verbose = verbose;
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

  public final void requestMaps(MBMapsHandler mapsHandler)
  {
     requestMaps(mapsHandler, true);
  }
  public final void requestMaps(MBMapsHandler mapsHandler, boolean deleteHandler)
  {
    if (_verbose)
    {
      ILogger.instance().logInfo("MapBoo: loading maps");
    }
    _downloader.requestBuffer(new URL(_serverURL, "/public/v1/map/"), DownloadPriority.HIGHEST, TimeInterval.zero(), false, new MapsBufferDownloadListener(_handler, mapsHandler, deleteHandler, _threadUtils, _verbose), true); // readExpired
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