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
//class IImageBuilder;
//class LabelImageBuilder;


public class MapBoo
{


  public static class MBLayer
  {
    private final String _type;
    private final String _url;
    private final String _attribution;
    private final boolean _verbose;

    private MBLayer(String type, String url, String attribution, boolean verbose)
    {
       _type = type;
       _url = url;
       _attribution = attribution;
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
      final String attribution = jsonObject.getAsString("attribution", "");
    
      return new MapBoo.MBLayer(type, url, attribution, verbose);
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

  public abstract static class MBSymbology extends RCObject
  {
    protected MBHandler _handler;
    protected final String _datasetID;
    protected final String _datasetName;

    protected MBSymbology(MBHandler handler, String datasetID, String datasetName)
    {
       _handler = handler;
       _datasetID = datasetID;
       _datasetName = datasetName;

    }

    public void dispose()
    {
      super.dispose();
    }

    public static MapBoo.MBSymbology fromJSON(MBHandler handler, String datasetID, String datasetName, JSONBaseObject jsonBaseObject)
    {
      if (jsonBaseObject == null)
      {
        return null;
      }
    
      final JSONObject jsonObject = jsonBaseObject.asObject();
    
      final String type = jsonObject.get("type").asString().value();
      if (type.equals("Vector"))
      {
        return MBVectorSymbology.fromJSON(handler, datasetID, datasetName, jsonObject);
      }
    
      ILogger.instance().logError("Symbology type=\"%s\" not supported", type);
      return null;
    }

    public abstract void apply(URL serverURL, VectorStreamingRenderer vectorStreamingRenderer, VectorStreamingRenderer.VectorSetSymbolizer symbolizer, boolean deleteSymbolizer);

  }


  public abstract static class MBShape
  {
//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//    MBShape(MBShape that);

    protected MBShape()
    {

    }

    public static MapBoo.MBShape fromJSON(JSONBaseObject jsonBaseObject)
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
    
      final String type = jsonObject.getAsString("type").value();
      if (type.equals("Circle"))
      {
        return MBCircleShape.fromJSON(jsonObject);
      }
    
      ILogger.instance().logError("Shape type \"%s\" not supported", type);
    
      return null;
    }

    public void dispose()
    {

    }

    public abstract IImageBuilder createImageBuilder();

  }


  public static class MBCircleShape extends MBShape
  {
    private final Color _color ;
    private final int _radius;

    private MBCircleShape(Color color, int radius)
    {
       _color = new Color(color);
       _radius = radius;

    }

    public static MapBoo.MBCircleShape fromJSON(JSONObject jsonObject)
    {
      final JSONArray colorArray = jsonObject.getAsArray("color");
      final float red = (float) colorArray.getAsNumber(0).value();
      final float green = (float) colorArray.getAsNumber(1).value();
      final float blue = (float) colorArray.getAsNumber(2).value();
      final float alpha = (float) colorArray.getAsNumber(3).value();
    
      final int radius = (int) jsonObject.getAsNumber("radius").value();
    
      return new MBCircleShape(Color.fromRGBA(red, green, blue, alpha), radius);
    }

    public void dispose()
    {
      super.dispose();
    }

    public final IImageBuilder createImageBuilder()
    {
      return new CircleImageBuilder(_color, _radius);
    }

  }


  public static class MBVectorSymbology extends MBSymbology
  {
    private java.util.ArrayList<String> _labeling = new java.util.ArrayList<String>();
    private final MBShape _shape;
    private java.util.ArrayList<String> _info = new java.util.ArrayList<String>();

    private String createMarkLabel(JSONObject properties)
    {
      final int labelingSize = _labeling.size();
      if ((labelingSize == 0) || (properties.size() == 0))
      {
        return "<label>";
      }
      else if (labelingSize == 1)
      {
        return JSONBaseObject.toString(properties.get(_labeling.get(0)));
      }
      else
      {
        IStringBuilder labelBuilder = IStringBuilder.newStringBuilder();
        for (int i = 0; i < labelingSize; i++)
        {
          if (i > 0)
          {
            labelBuilder.addString(" ");
          }
          final String value = JSONBaseObject.toString(properties.get(_labeling.get(i)));
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
    
      final int infoSize = _info.size();
      if (infoSize == 0)
      {
        return null;
      }
    
      JSONObject infoProperties = new JSONObject();
      for (int i = 0; i < infoSize; i++)
      {
        final String info = _info.get(i);
        final JSONBaseObject value = properties.get(info);
        if (value != null)
        {
          infoProperties.put(info, value.deepCopy());
        }
      }
    
      return new MBFeatureMarkTouchListener(_datasetName, _handler, _info, infoProperties);
    }

    private IImageBuilder createImageBuilder(JSONObject properties)
    {
      final boolean hasLabeling = (_labeling.size() != 0) && (properties.size() != 0);
      final boolean hasShape = (_shape != null);
    
      if (hasLabeling)
      {
        if (hasShape)
        {
          return new ColumnLayoutImageBuilder(createLabelImageBuilder(createMarkLabel(properties)), _shape.createImageBuilder());
        }
    
        return createLabelImageBuilder(createMarkLabel(properties));
      }
    
      if (hasShape)
      {
        return _shape.createImageBuilder();
      }
    
      return createLabelImageBuilder("[X]");
    }
    private LabelImageBuilder createLabelImageBuilder(String label)
    {
      return new LabelImageBuilder(label, GFont.sansSerif(18, true), 2.0f, Color.white(), Color.black(), 2.0f, 0.0f, 0.0f); // shadowOffsetY -  shadowOffsetX -  shadowBlur -  shadowColor -  color -  margin
    }

    public void dispose()
    {
      if (_shape != null)
         _shape.dispose();

      super.dispose();
    }

    public static MapBoo.MBVectorSymbology fromJSON(MBHandler handler, String datasetID, String datasetName, JSONObject jsonObject)
    {
      java.util.ArrayList<String> labeling = jsonObject.getAsArray("labeling").asStringVector();
      final MBShape shape = MBShape.fromJSON(jsonObject.get("shape"));
      java.util.ArrayList<String> info = jsonObject.getAsArray("info").asStringVector();
    
      return new MBVectorSymbology(handler, datasetID, datasetName, labeling, shape, info);
    }

    public MBVectorSymbology(MBHandler handler, String datasetID, String datasetName, java.util.ArrayList<String> labeling, MBShape shape, java.util.ArrayList<String> info)
    {
       super(handler, datasetID, datasetName);
       _labeling = labeling;
       _shape = shape;
       _info = info;
    }

    public final void apply(URL serverURL, VectorStreamingRenderer vectorStreamingRenderer, VectorStreamingRenderer.VectorSetSymbolizer symbolizer, boolean deleteSymbolizer)
    {
      String properties = "";
      for (int i = 0; i < _labeling.size(); i++)
      {
        properties += _labeling.get(i) + "|";
      }
      for (int i = 0; i < _info.size(); i++)
      {
        properties += _info.get(i) + "|";
      }
    
      final VectorStreamingRenderer.VectorSetSymbolizer sym;
      boolean deleteSym;
      if (symbolizer == null)
      {
        sym = new MBDatasetVectorSetSymbolizer(this);
        deleteSym = true;
      }
      else
      {
        sym = symbolizer;
        deleteSym = deleteSymbolizer;
      }
    
      vectorStreamingRenderer.addVectorSet(new URL(serverURL, "/public/v1/VectorialStreaming/"), _datasetID, properties, sym, deleteSym, DownloadPriority.MEDIUM, TimeInterval.zero(), true, true, false); // haltOnError -  verbose -  readExpired
    }

    public final Mark createFeatureMark(GEO2DPointGeometry geometry)
    {
      final GEOFeature feature = geometry.getFeature();
      final JSONObject properties = feature.getProperties();
      final Geodetic2D position = geometry.getPosition();
    
      return new Mark(createImageBuilder(properties), new Geodetic3D(position, 0), AltitudeMode.ABSOLUTE, 0, null, true, createMarkTouchListener(properties), true); // autoDeleteListener -  autoDeleteUserData -  userData -  minDistanceToCamera
    }

    public final Mark createClusterMark(VectorStreamingRenderer.Cluster cluster, long featuresCount)
    {
      final Geodetic3D position = new Geodetic3D(cluster.getPosition()._latitude, cluster.getPosition()._longitude, 0);
    
      final String label = IStringUtils.instance().toString(cluster.getSize());
    
      // float labelFontSize = (float) (14.0 * ((float) cluster->getSize() / featuresCount) + 16.0) ;
      float labelFontSize = 18.0f;
    
      Mark mark = new Mark(new StackLayoutImageBuilder(new CircleImageBuilder(Color.white(), 32), new LabelImageBuilder(label, GFont.sansSerif(labelFontSize, true), 2.0f, Color.black(), Color.transparent(), 5.0f, 0.0f, 0.0f, Color.white(), 4.0f)), position, AltitudeMode.ABSOLUTE, 0); // minDistanceToCamera -  cornerRadius -  backgroundColor -  shadowOffsetY -  shadowOffsetX -  shadowBlur -  shadowColor -  color -  margin
    
      return mark;
    }

  }


  public static class MBSymbolizedDataset
  {
    private final String _datasetID;
    private final String _datasetName;
    private final String _datasetAttribution;
    private final MBSymbology _symbology;

    private MBSymbolizedDataset(String datasetID, String datasetName, String datasetAttribution, MBSymbology symbology)
    {
       _datasetID = datasetID;
       _datasetName = datasetName;
       _datasetAttribution = datasetAttribution;
       _symbology = symbology;

    }


    public void dispose()
    {
      if (_symbology != null)
      {
        _symbology._release();
      }
    }

    public static MapBoo.MBSymbolizedDataset fromJSON(MBHandler handler, JSONBaseObject jsonBaseObject, boolean verbose)
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
    
      final String datasetID = jsonObject.get("datasetID").asString().value();
      final String datasetName = jsonObject.getAsString("datasetName", "");
      final String datasetAttribution = jsonObject.getAsString("datasetAttribution", "");
      final MBSymbology symbology = MBSymbology.fromJSON(handler, datasetID, datasetName, jsonObject.get("symbology"));
    
      return new MBSymbolizedDataset(datasetID, datasetName, datasetAttribution, symbology);
    }

    public final void apply(URL serverURL, VectorStreamingRenderer vectorStreamingRenderer, VectorStreamingRenderer.VectorSetSymbolizer symbolizer, boolean deleteSymbolizer)
    {
      _symbology.apply(serverURL, vectorStreamingRenderer, symbolizer, deleteSymbolizer);
    }

  }


  public static class MBDatasetVectorSetSymbolizer extends VectorStreamingRenderer.VectorSetSymbolizer
  {
    private final MBVectorSymbology _symbology;

    public MBDatasetVectorSetSymbolizer(MBVectorSymbology symbology)
    {
       _symbology = symbology;
      _symbology._retain();
    }

    public void dispose()
    {
      _symbology._release();
      super.dispose();
    }

    public final Mark createFeatureMark(GEO2DPointGeometry geometry)
    {
      return _symbology.createFeatureMark(geometry);
    }

    public final Mark createClusterMark(VectorStreamingRenderer.Cluster cluster, long featuresCount)
    {
      return _symbology.createClusterMark(cluster, featuresCount);
    }

  }


  public static class MBMap
  {
    private final String _id;
    private final String _name;
    private java.util.ArrayList<MapBoo.MBLayer> _layers = new java.util.ArrayList<MapBoo.MBLayer>();
    private java.util.ArrayList<MapBoo.MBSymbolizedDataset> _symbolizedDatasets = new java.util.ArrayList<MapBoo.MBSymbolizedDataset>();
    private final int _timestamp;
    private final boolean _verbose;

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//    MBMap(MBMap that);

    private MBMap(String id, String name, java.util.ArrayList<MapBoo.MBLayer> layers, java.util.ArrayList<MapBoo.MBSymbolizedDataset> symbolizedDatasets, int timestamp, boolean verbose)
    {
       _id = id;
       _name = name;
       _layers = layers;
       _symbolizedDatasets = symbolizedDatasets;
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
    private static java.util.ArrayList<MapBoo.MBSymbolizedDataset> parseSymbolizedDatasets(MBHandler handler, JSONArray jsonArray, boolean verbose)
    {
      java.util.ArrayList<MapBoo.MBSymbolizedDataset> result = new java.util.ArrayList<MapBoo.MBSymbolizedDataset>();
      for (int i = 0; i < jsonArray.size(); i++)
      {
        MBSymbolizedDataset symbolizedDataset = MBSymbolizedDataset.fromJSON(handler, jsonArray.get(i), verbose);
        if (symbolizedDataset != null)
        {
          result.add(symbolizedDataset);
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
    
      final JSONBaseObject jsonErrorCode = jsonObject.get("errorCode");
      if (jsonErrorCode != null)
      {
        final JSONString jsonStringErrorCode = jsonErrorCode.asString();
        if (jsonStringErrorCode != null)
        {
          final String errorCode = jsonStringErrorCode.value();
          final String errorDescription = jsonObject.getAsString("errorDescription", "");
          ILogger.instance().logError("Error: %s %s", errorCode, errorDescription);
        }
        return null;
      }
    
      final String id = jsonObject.get("id").asString().value();
      final String name = jsonObject.get("name").asString().value();
      java.util.ArrayList<MapBoo.MBLayer> layers = parseLayers(jsonObject.get("layerSet").asArray(), verbose);
      java.util.ArrayList<MapBoo.MBSymbolizedDataset> symDatasets = parseSymbolizedDatasets(handler, jsonObject.get("symbolizedDatasets").asArray(), verbose);
      final int timestamp = (int) jsonObject.get("timestamp").asNumber().value();
    
      return new MBMap(id, name, layers, symDatasets, timestamp, verbose);
    }

    public void dispose()
    {
      if (_verbose)
      {
        ILogger.instance().logInfo("MapBoo: deleting map");
      }
    
      for (int i = 0; i < _symbolizedDatasets.size(); i++)
      {
        MBSymbolizedDataset symbolizedDataset = _symbolizedDatasets.get(i);
        if (symbolizedDataset != null)
           symbolizedDataset.dispose();
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

    public final void apply(URL serverURL, LayerSet layerSet, VectorStreamingRenderer vectorStreamingRenderer, VectorStreamingRenderer.VectorSetSymbolizer symbolizer, boolean deleteSymbolizer)
    {
      for (int i = 0; i < _layers.size(); i++)
      {
        MBLayer layer = _layers.get(i);
        layer.apply(layerSet);
      }
    
      for (int i = 0; i < _symbolizedDatasets.size(); i++)
      {
        MBSymbolizedDataset symbolizedDataset = _symbolizedDatasets.get(i);
        symbolizedDataset.apply(serverURL, vectorStreamingRenderer, symbolizer, deleteSymbolizer);
      }
    }
  }


  public interface MBHandler
  {
    void dispose();

    void onMapDownloadError();
    void onMapParseError();
    void onSelectedMap(MapBoo.MBMap map);

    void onFeatureTouched(String datasetName, java.util.ArrayList<String> info, JSONObject properties);
  }


  public static class MBFeatureMarkTouchListener extends MarkTouchListener
  {
    private final String _datasetName;
    private MBHandler _handler;
    private java.util.ArrayList<String> _info = new java.util.ArrayList<String>();
    private final JSONObject _properties;

    public MBFeatureMarkTouchListener(String datasetName, MBHandler handler, java.util.ArrayList<String> info, JSONObject properties)
    {
       _datasetName = datasetName;
       _handler = handler;
       _info = info;
       _properties = properties;
    }

    public final boolean touchedMark(Mark mark)
    {
      _handler.onFeatureTouched(_datasetName, _info, _properties);
      return true;
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
    private final VectorStreamingRenderer.VectorSetSymbolizer _symbolizer;
    private final boolean _deleteSymbolizer;

    public MapParserAsyncTask(MapBoo mapboo, MBHandler handler, IByteBuffer buffer, boolean verbose, VectorStreamingRenderer.VectorSetSymbolizer symbolizer, boolean deleteSymbolizer)
    {
       _mapboo = mapboo;
       _handler = handler;
       _buffer = buffer;
       _verbose = verbose;
       _symbolizer = symbolizer;
       _deleteSymbolizer = deleteSymbolizer;
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
    
        _mapboo.onMap(_map, _symbolizer, _deleteSymbolizer);
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
    private final VectorStreamingRenderer.VectorSetSymbolizer _symbolizer;
    private final boolean _deleteSymbolizer;

    public MapBufferDownloadListener(MapBoo mapboo, MBHandler handler, IThreadUtils threadUtils, boolean verbose, VectorStreamingRenderer.VectorSetSymbolizer symbolizer, boolean deleteSymbolizer)
    {
       _mapboo = mapboo;
       _handler = handler;
       _threadUtils = threadUtils;
       _verbose = verbose;
       _symbolizer = symbolizer;
       _deleteSymbolizer = deleteSymbolizer;
    }

    public final void onDownload(URL url, IByteBuffer buffer, boolean expired)
    {
      if (_verbose)
      {
        ILogger.instance().logInfo("MapBoo: downloaded map");
      }
    
      _threadUtils.invokeAsyncTask(new MapParserAsyncTask(_mapboo, _handler, buffer, _verbose, _symbolizer, _deleteSymbolizer), true);
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

  private void requestMap(VectorStreamingRenderer.VectorSetSymbolizer symbolizer, boolean deleteSymbolizer)
  {
    if (_verbose)
    {
      ILogger.instance().logInfo("MapBoo: requesting map \"%s\"", _mapID);
    }
  
    _downloader.requestBuffer(new URL(_serverURL, "/public/v1/map/" + _mapID), DownloadPriority.HIGHEST, TimeInterval.zero(), false, new MapBufferDownloadListener(this, _handler, _threadUtils, _verbose, symbolizer, deleteSymbolizer), true); // readExpired
  }
  private void applyMap(MapBoo.MBMap map, VectorStreamingRenderer.VectorSetSymbolizer symbolizer, boolean deleteSymbolizer)
  {
    if (_verbose)
    {
      ILogger.instance().logInfo("MapBoo: applying map \"%s\"", map.getID());
    }
  
    // clean current map
    _vectorStreamingRenderer.removeAllVectorSets();
    _layerSet.removeAllLayers(true);
  
    map.apply(_serverURL, _layerSet, _vectorStreamingRenderer, symbolizer, deleteSymbolizer);
  
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
     _mapID = "";
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

  public final void setMapID(String mapID, VectorStreamingRenderer.VectorSetSymbolizer symbolizer, boolean deleteSymbolizer)
  {
    if (!_mapID.equals(mapID))
    {
      _mapID = mapID;
      requestMap(symbolizer, deleteSymbolizer);
    }
  }

  public final void setMapID(String mapID)
  {
    setMapID(mapID, null, true);
  }

  public final void setMap(MapBoo.MBMap map)
  {
    final String mapID = map.getID();
    if (!_mapID.equals(mapID))
    {
      _mapID = mapID;
  
      applyMap(map, null, true);
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
  public final void onMap(MapBoo.MBMap map, VectorStreamingRenderer.VectorSetSymbolizer symbolizer, boolean deleteSymbolizer)
  {
    applyMap(map, symbolizer, deleteSymbolizer);
  }

  public final void reloadMap()
  {
    requestMap(null, true);
  }

}