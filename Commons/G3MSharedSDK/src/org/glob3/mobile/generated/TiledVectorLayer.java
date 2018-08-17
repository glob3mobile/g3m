package org.glob3.mobile.generated;import java.util.*;

//
//  TiledVectorLayer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 4/30/14.
//
//

//
//  TiledVectorLayer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 4/30/14.
//
//


//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class TileImageContribution;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IDownloader;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IBufferDownloadListener;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IStringUtils;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class GEORasterSymbolizer;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class TiledVectorLayerTileImageProvider;

public class TiledVectorLayer extends VectorLayer
{
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
  private final GEORasterSymbolizer _symbolizer;
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
  public GEORasterSymbolizer _symbolizer = new internal();
//#endif
  private final String _urlTemplate;
  private final Sector _dataSector = new Sector();
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
  private final TimeInterval _timeToCache = new TimeInterval();
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
  public final TimeInterval _timeToCache = new internal();
//#endif
  private final boolean _readExpired;

//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
  private final IMathUtils _mu;
  private final IStringUtils _su;
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
  public IMathUtils _mu = new internal();
  public IStringUtils _su = new internal();
//#endif
  private TiledVectorLayerTileImageProvider _tileImageProvider;

  private TiledVectorLayer(GEORasterSymbolizer symbolizer, String urlTemplate, Sector dataSector, java.util.ArrayList<const LayerTilesRenderParameters> parametersVector, TimeInterval timeToCache, boolean readExpired, float transparency, LayerCondition condition, java.util.ArrayList<Info> layerInfo)
  {
	  super(parametersVector, transparency, condition, layerInfo);
	  _symbolizer = symbolizer;
	  _urlTemplate = urlTemplate;
	  _dataSector = new Sector(dataSector);
	  _timeToCache = new TimeInterval(timeToCache);
	  _readExpired = readExpired;
	  _tileImageProvider = null;
	  _su = null;
	  _mu = null;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const URL createURL(const Tile* tile) const
  private URL createURL(Tile tile)
  {
  
	if (_mu == null)
	{
	  _mu = IMathUtils.instance();
	}
  
	if (_su == null)
	{
	  _su = IStringUtils.instance();
	}
  
	final Sector sector = tile._sector;
  
  
	final LayerTilesRenderParameters parameters = _parametersVector.get(_selectedLayerTilesRenderParametersIndex);
  
	final Vector2I tileTextureResolution = parameters._tileTextureResolution;
  
	final int level = tile._level;
	final int column = tile._column;
	final int numRows = (int)(parameters._topSectorSplitsByLatitude * _mu.pow(2.0, level));
	final int row = numRows - tile._row - 1;
  
	final double north = MercatorUtils.latitudeToMeters(sector._upper._latitude);
	final double south = MercatorUtils.latitudeToMeters(sector._lower._latitude);
	final double east = MercatorUtils.longitudeToMeters(sector._upper._longitude);
	final double west = MercatorUtils.longitudeToMeters(sector._lower._longitude);
  
	String path = _urlTemplate;
	path = _su.replaceAll(path, "{width}", _su.toString(tileTextureResolution._x));
	path = _su.replaceAll(path, "{height}", _su.toString(tileTextureResolution._y));
	path = _su.replaceAll(path, "{x}", _su.toString(column));
	path = _su.replaceAll(path, "{y}", _su.toString(row));
	path = _su.replaceAll(path, "{y2}", _su.toString(tile._row));
	path = _su.replaceAll(path, "{level}", _su.toString(level));
	path = _su.replaceAll(path, "{lowerLatitude}", _su.toString(sector._lower._latitude._degrees));
	path = _su.replaceAll(path, "{lowerLongitude}", _su.toString(sector._lower._longitude._degrees));
	path = _su.replaceAll(path, "{upperLatitude}", _su.toString(sector._upper._latitude._degrees));
	path = _su.replaceAll(path, "{upperLongitude}", _su.toString(sector._upper._longitude._degrees));
	path = _su.replaceAll(path, "{north}", _su.toString(north));
	path = _su.replaceAll(path, "{south}", _su.toString(south));
	path = _su.replaceAll(path, "{west}", _su.toString(west));
	path = _su.replaceAll(path, "{east}", _su.toString(east));
  
	return new URL(path, false);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: String getLayerType() const
  protected final String getLayerType()
  {
	return "TiledVectorLayer";
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean rawIsEquals(const Layer* that) const
  protected final boolean rawIsEquals(Layer that)
  {
	final TiledVectorLayer t = (TiledVectorLayer) that;
  
	if (!_urlTemplate.equals(t._urlTemplate))
	{
	  return false;
	}
  
	return _dataSector.isEquals(t._dataSector);
  }



  public static TiledVectorLayer newMercator(GEORasterSymbolizer symbolizer, String urlTemplate, Sector dataSector, int firstLevel, int maxLevel, TimeInterval timeToCache, boolean readExpired, float transparency, LayerCondition condition)
  {
	  return newMercator(symbolizer, urlTemplate, dataSector, firstLevel, maxLevel, timeToCache, readExpired, transparency, condition, new java.util.ArrayList<const Info*>());
  }
  public static TiledVectorLayer newMercator(GEORasterSymbolizer symbolizer, String urlTemplate, Sector dataSector, int firstLevel, int maxLevel, TimeInterval timeToCache, boolean readExpired, float transparency)
  {
	  return newMercator(symbolizer, urlTemplate, dataSector, firstLevel, maxLevel, timeToCache, readExpired, transparency, null, new java.util.ArrayList<const Info*>());
  }
  public static TiledVectorLayer newMercator(GEORasterSymbolizer symbolizer, String urlTemplate, Sector dataSector, int firstLevel, int maxLevel, TimeInterval timeToCache, boolean readExpired)
  {
	  return newMercator(symbolizer, urlTemplate, dataSector, firstLevel, maxLevel, timeToCache, readExpired, 1, null, new java.util.ArrayList<const Info*>());
  }
  public static TiledVectorLayer newMercator(GEORasterSymbolizer symbolizer, String urlTemplate, Sector dataSector, int firstLevel, int maxLevel, TimeInterval timeToCache)
  {
	  return newMercator(symbolizer, urlTemplate, dataSector, firstLevel, maxLevel, timeToCache, true, 1, null, new java.util.ArrayList<const Info*>());
  }
  public static TiledVectorLayer newMercator(GEORasterSymbolizer symbolizer, String urlTemplate, Sector dataSector, int firstLevel, int maxLevel)
  {
	  return newMercator(symbolizer, urlTemplate, dataSector, firstLevel, maxLevel, TimeInterval.fromDays(30), true, 1, null, new java.util.ArrayList<const Info*>());
  }
//C++ TO JAVA CONVERTER NOTE: Java does not allow default values for parameters. Overloaded methods are inserted above.
//ORIGINAL LINE: static TiledVectorLayer* newMercator(const GEORasterSymbolizer* symbolizer, const String& urlTemplate, const Sector& dataSector, const int firstLevel, const int maxLevel, const TimeInterval& timeToCache = TimeInterval::fromDays(30), const boolean readExpired = true, const float transparency = 1, const LayerCondition* condition = null, java.util.ArrayList<const Info*>* layerInfo = new java.util.ArrayList<const Info*>())
  public static TiledVectorLayer newMercator(GEORasterSymbolizer symbolizer, String urlTemplate, Sector dataSector, int firstLevel, int maxLevel, TimeInterval timeToCache, boolean readExpired, float transparency, LayerCondition condition, java.util.ArrayList<Info> layerInfo)
  {
	final java.util.ArrayList<LayerTilesRenderParameters> parametersVector = new java.util.ArrayList<LayerTilesRenderParameters>();
	parametersVector.add(LayerTilesRenderParameters.createDefaultMercator(firstLevel, maxLevel));
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: return new TiledVectorLayer(symbolizer, urlTemplate, dataSector, parametersVector, timeToCache, readExpired, transparency, condition, layerInfo);
	return new TiledVectorLayer(symbolizer, urlTemplate, new Sector(dataSector), parametersVector, new TimeInterval(timeToCache), readExpired, transparency, condition, layerInfo);
  }

  public void dispose()
  {
	if (_symbolizer != null)
		_symbolizer.dispose();
	if (_tileImageProvider != null)
	{
	  _tileImageProvider.layerDeleted(this);
	  _tileImageProvider._release();
	}
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	super.dispose();
//#endif
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: URL getFeatureInfoURL(const Geodetic2D& position, const Sector& sector) const
  public final URL getFeatureInfoURL(Geodetic2D position, Sector sector)
  {
	return new URL();
  }

  public final RenderState getRenderState()
  {
	return RenderState.ready();
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const String description() const
  public final String description()
  {
	return "[TiledVectorLayer urlTemplate=\"" + _urlTemplate + "\"]";
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: TiledVectorLayer* copy() const
  public final TiledVectorLayer copy()
  {
  
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: return new TiledVectorLayer(_symbolizer->copy(), _urlTemplate, _dataSector, createParametersVectorCopy(), _timeToCache, _readExpired, _transparency, (_condition == null) ? null : _condition->copy(), _layerInfo);
	return new TiledVectorLayer(_symbolizer.copy(), _urlTemplate, new Sector(_dataSector), createParametersVectorCopy(), new TimeInterval(_timeToCache), _readExpired, _transparency, (_condition == null) ? null : _condition.copy(), _layerInfo);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const TileImageContribution* contribution(const Tile* tile) const
  public final TileImageContribution contribution(Tile tile)
  {
	if ((_condition == null) || _condition.isAvailable(tile))
	{
	  return (_dataSector.touchesWith(tile._sector) ? TileImageContribution.fullCoverageTransparent(_transparency) : null);
	}
	return null;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: TileImageProvider* createTileImageProvider(const G3MRenderContext* rc, const LayerTilesRenderParameters* layerTilesRenderParameters) const
  public final TileImageProvider createTileImageProvider(G3MRenderContext rc, LayerTilesRenderParameters layerTilesRenderParameters)
  {
	if (_tileImageProvider == null)
	{
	  _tileImageProvider = new TiledVectorLayerTileImageProvider(this, rc.getDownloader(), rc.getThreadUtils());
	}
	_tileImageProvider._retain();
	return _tileImageProvider;
  }

  public static class RequestGEOJSONBufferData
  {
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
	public final URL _url = new URL();
	public final TimeInterval _timeToCache = new TimeInterval();
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	public final URL _url = new public();
	public final TimeInterval _timeToCache = new public();
//#endif
	public final boolean _readExpired;

	public RequestGEOJSONBufferData(URL url, TimeInterval timeToCache, boolean readExpired)
	{
		_url = new URL(url);
		_timeToCache = new TimeInterval(timeToCache);
		_readExpired = readExpired;
	}
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: TiledVectorLayer::RequestGEOJSONBufferData* getRequestGEOJSONBufferData(const Tile* tile) const
  public final TiledVectorLayer.RequestGEOJSONBufferData getRequestGEOJSONBufferData(Tile tile)
  {
	final LayerTilesRenderParameters parameters = _parametersVector.get(_selectedLayerTilesRenderParametersIndex);
  
	if (tile._level > parameters._maxLevel)
	{
	  final Tile parentTile = tile.getParent();
	  if (parentTile != null)
	  {
		return getRequestGEOJSONBufferData(parentTile);
	  }
	}
  
	return new RequestGEOJSONBufferData(createURL(tile), _timeToCache, _readExpired);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const GEORasterSymbolizer* symbolizerCopy() const
  public final GEORasterSymbolizer symbolizerCopy()
  {
	return _symbolizer.copy();
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const Sector getDataSector() const
  public final Sector getDataSector()
  {
	return _dataSector;
  }

  public final void setSymbolizer(GEORasterSymbolizer symbolizer, boolean deletePrevious)
  {
	if (_symbolizer != symbolizer)
	{
	  if (deletePrevious)
	  {
		if (_symbolizer != null)
			_symbolizer.dispose();
	  }
	  _symbolizer = symbolizer;
	  notifyChanges();
	}
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const java.util.ArrayList<URL*> getDownloadURLs(const Tile* tile) const
  public final java.util.ArrayList<URL> getDownloadURLs(Tile tile)
  {
	java.util.ArrayList<URL> result = new java.util.ArrayList<URL>();
	result.add(new URL(createURL(tile)));
	return result;
  }

}
