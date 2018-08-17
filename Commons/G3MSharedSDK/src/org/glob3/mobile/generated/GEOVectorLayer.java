package org.glob3.mobile.generated;import java.util.*;

//
//  GEOVectorLayer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/8/14.
//
//

//
//  GEOVectorLayer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/8/14.
//
//




//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class TileImageContribution;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class GEOVectorTileImageProvider;

public class GEOVectorLayer extends VectorLayer
{
  private QuadTree _quadTree = new QuadTree();

  private GEOVectorTileImageProvider _tileImageProvider;


  public GEOVectorLayer(java.util.ArrayList<const LayerTilesRenderParameters> parametersVector, float transparency, LayerCondition condition)
  {
	  this(parametersVector, transparency, condition, new java.util.ArrayList<const Info*>());
  }
  public GEOVectorLayer(java.util.ArrayList<const LayerTilesRenderParameters> parametersVector, float transparency)
  {
	  this(parametersVector, transparency, null, new java.util.ArrayList<const Info*>());
  }
  public GEOVectorLayer(java.util.ArrayList<const LayerTilesRenderParameters> parametersVector)
  {
	  this(parametersVector, 1.0f, null, new java.util.ArrayList<const Info*>());
  }
//C++ TO JAVA CONVERTER NOTE: Java does not allow default values for parameters. Overloaded methods are inserted above.
//ORIGINAL LINE: GEOVectorLayer(const java.util.ArrayList<const LayerTilesRenderParameters*>& parametersVector, const float transparency = 1.0f, const LayerCondition* condition = null, java.util.ArrayList<const Info*>* layerInfo = new java.util.ArrayList<const Info*>()) : VectorLayer(parametersVector, transparency, condition, layerInfo), _tileImageProvider(null)
  public GEOVectorLayer(java.util.ArrayList<const LayerTilesRenderParameters> parametersVector, float transparency, LayerCondition condition, java.util.ArrayList<Info> layerInfo)
  {
	  super(parametersVector, transparency, condition, layerInfo);
	  _tileImageProvider = null;
  
  }

  public GEOVectorLayer(int mercatorFirstLevel, int mercatorMaxLevel, int wgs84firstLevel, int wgs84maxLevel, float transparency, LayerCondition condition)
  {
	  this(mercatorFirstLevel, mercatorMaxLevel, wgs84firstLevel, wgs84maxLevel, transparency, condition, new java.util.ArrayList<const Info*>());
  }
  public GEOVectorLayer(int mercatorFirstLevel, int mercatorMaxLevel, int wgs84firstLevel, int wgs84maxLevel, float transparency)
  {
	  this(mercatorFirstLevel, mercatorMaxLevel, wgs84firstLevel, wgs84maxLevel, transparency, null, new java.util.ArrayList<const Info*>());
  }
  public GEOVectorLayer(int mercatorFirstLevel, int mercatorMaxLevel, int wgs84firstLevel, int wgs84maxLevel)
  {
	  this(mercatorFirstLevel, mercatorMaxLevel, wgs84firstLevel, wgs84maxLevel, 1.0f, null, new java.util.ArrayList<const Info*>());
  }
  public GEOVectorLayer(int mercatorFirstLevel, int mercatorMaxLevel, int wgs84firstLevel)
  {
	  this(mercatorFirstLevel, mercatorMaxLevel, wgs84firstLevel, 18, 1.0f, null, new java.util.ArrayList<const Info*>());
  }
  public GEOVectorLayer(int mercatorFirstLevel, int mercatorMaxLevel)
  {
	  this(mercatorFirstLevel, mercatorMaxLevel, 0, 18, 1.0f, null, new java.util.ArrayList<const Info*>());
  }
  public GEOVectorLayer(int mercatorFirstLevel)
  {
	  this(mercatorFirstLevel, 18, 0, 18, 1.0f, null, new java.util.ArrayList<const Info*>());
  }
  public GEOVectorLayer()
  {
	  this(2, 18, 0, 18, 1.0f, null, new java.util.ArrayList<const Info*>());
  }
//C++ TO JAVA CONVERTER NOTE: Java does not allow default values for parameters. Overloaded methods are inserted above.
//ORIGINAL LINE: GEOVectorLayer(const int mercatorFirstLevel = 2, const int mercatorMaxLevel = 18, const int wgs84firstLevel = 0, const int wgs84maxLevel = 18, const float transparency = 1.0f, const LayerCondition* condition = null, java.util.ArrayList<const Info*>* layerInfo = new java.util.ArrayList<const Info*>()) : VectorLayer(LayerTilesRenderParameters::createDefaultMultiProjection(mercatorFirstLevel, mercatorMaxLevel, wgs84firstLevel, wgs84maxLevel), transparency, condition, layerInfo), _tileImageProvider(null)
  public GEOVectorLayer(int mercatorFirstLevel, int mercatorMaxLevel, int wgs84firstLevel, int wgs84maxLevel, float transparency, LayerCondition condition, java.util.ArrayList<Info> layerInfo)
  {
	  super(LayerTilesRenderParameters.createDefaultMultiProjection(mercatorFirstLevel, mercatorMaxLevel, wgs84firstLevel, wgs84maxLevel), transparency, condition, layerInfo);
	  _tileImageProvider = null;
  
  }

  public void dispose()
  {
	//  delete _symbolizer;
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
//ORIGINAL LINE: const Sector getDataSector() const
  public final Sector getDataSector()
  {
	///#error todo;
	return Sector.fullSphere();
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: String getLayerType() const
  public final String getLayerType()
  {
	return "GEOVectorLayer";
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean rawIsEquals(const Layer* that) const
  public final boolean rawIsEquals(Layer that)
  {
	return false;
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
	return "[GEOVectorLayer]";
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: GEOVectorLayer* copy() const
  public final GEOVectorLayer copy()
  {
	return new GEOVectorLayer();
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: TileImageProvider* createTileImageProvider(const G3MRenderContext* rc, const LayerTilesRenderParameters* layerTilesRenderParameters) const
  public final TileImageProvider createTileImageProvider(G3MRenderContext rc, LayerTilesRenderParameters layerTilesRenderParameters)
  {
	if (_tileImageProvider == null)
	{
	  _tileImageProvider = new GEOVectorTileImageProvider(this);
	}
	_tileImageProvider._retain();
	return _tileImageProvider;
  }

  public final void clear()
  {
	_quadTree.clear();
	notifyChanges();
  }

  public final void addSymbol(GEORasterSymbol symbol)
  {
	final Sector sector = symbol.getSector();
  
	if (sector == null)
	{
	  ILogger.instance().logError("GEORasterSymbol has not sector, can't rasterize");
	  if (symbol != null)
		  symbol.dispose();
	}
	else
	{
	  final boolean added = _quadTree.add(sector, symbol);
	  if (added)
	  {
		notifyChanges();
	  }
	  else
	  {
		if (symbol != null)
			symbol.dispose();
	  }
	}
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const TileImageContribution* contribution(const Tile* tile) const
  public final TileImageContribution contribution(Tile tile)
  {
	if ((_condition == null) || _condition.isAvailable(tile))
	{
	  return (_quadTree.getSector().touchesWith(tile._sector) && !_quadTree.isEmpty() ? TileImageContribution.fullCoverageTransparent(_transparency) : null);
	}
	return null;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const QuadTree& getQuadTree() const
  public final QuadTree getQuadTree()
  {
	return _quadTree;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const java.util.ArrayList<URL*> getDownloadURLs(const Tile* tile) const
  public final java.util.ArrayList<URL> getDownloadURLs(Tile tile)
  {
	java.util.ArrayList<URL> result = new java.util.ArrayList<URL>();
	return result;
  }

}
