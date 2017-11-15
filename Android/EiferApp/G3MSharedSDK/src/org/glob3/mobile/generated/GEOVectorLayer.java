package org.glob3.mobile.generated; 
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




//class TileImageContribution;
//class GEOVectorTileImageProvider;

public class GEOVectorLayer extends VectorLayer
{
  private QuadTree _quadTree = new QuadTree();

  private GEOVectorTileImageProvider _tileImageProvider;


  public GEOVectorLayer(java.util.ArrayList<LayerTilesRenderParameters> parametersVector, float transparency, LayerCondition condition)
  {
     this(parametersVector, transparency, condition, new java.util.ArrayList<Info>());
  }
  public GEOVectorLayer(java.util.ArrayList<LayerTilesRenderParameters> parametersVector, float transparency)
  {
     this(parametersVector, transparency, null, new java.util.ArrayList<Info>());
  }
  public GEOVectorLayer(java.util.ArrayList<LayerTilesRenderParameters> parametersVector)
  {
     this(parametersVector, 1.0f, null, new java.util.ArrayList<Info>());
  }
  public GEOVectorLayer(java.util.ArrayList<LayerTilesRenderParameters> parametersVector, float transparency, LayerCondition condition, java.util.ArrayList<Info> layerInfo)
  {
     super(parametersVector, transparency, condition, layerInfo);
     _tileImageProvider = null;
  
  }

  public GEOVectorLayer(int mercatorFirstLevel, int mercatorMaxLevel, int wgs84firstLevel, int wgs84maxLevel, float transparency, LayerCondition condition)
  {
     this(mercatorFirstLevel, mercatorMaxLevel, wgs84firstLevel, wgs84maxLevel, transparency, condition, new java.util.ArrayList<Info>());
  }
  public GEOVectorLayer(int mercatorFirstLevel, int mercatorMaxLevel, int wgs84firstLevel, int wgs84maxLevel, float transparency)
  {
     this(mercatorFirstLevel, mercatorMaxLevel, wgs84firstLevel, wgs84maxLevel, transparency, null, new java.util.ArrayList<Info>());
  }
  public GEOVectorLayer(int mercatorFirstLevel, int mercatorMaxLevel, int wgs84firstLevel, int wgs84maxLevel)
  {
     this(mercatorFirstLevel, mercatorMaxLevel, wgs84firstLevel, wgs84maxLevel, 1.0f, null, new java.util.ArrayList<Info>());
  }
  public GEOVectorLayer(int mercatorFirstLevel, int mercatorMaxLevel, int wgs84firstLevel)
  {
     this(mercatorFirstLevel, mercatorMaxLevel, wgs84firstLevel, 18, 1.0f, null, new java.util.ArrayList<Info>());
  }
  public GEOVectorLayer(int mercatorFirstLevel, int mercatorMaxLevel)
  {
     this(mercatorFirstLevel, mercatorMaxLevel, 0, 18, 1.0f, null, new java.util.ArrayList<Info>());
  }
  public GEOVectorLayer(int mercatorFirstLevel)
  {
     this(mercatorFirstLevel, 18, 0, 18, 1.0f, null, new java.util.ArrayList<Info>());
  }
  public GEOVectorLayer()
  {
     this(2, 18, 0, 18, 1.0f, null, new java.util.ArrayList<Info>());
  }
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
    super.dispose();
  }

  public final Sector getDataSector()
  {
    ///#error todo;
    return Sector.fullSphere();
  }

  public final String getLayerType()
  {
    return "GEOVectorLayer";
  }

  public final boolean rawIsEquals(Layer that)
  {
    return false;
  }

  public final URL getFeatureInfoURL(Geodetic2D position, Sector sector)
  {
    return new URL();
  }

  public final RenderState getRenderState()
  {
    return RenderState.ready();
  }

  public final String description()
  {
    return "[GEOVectorLayer]";
  }

  public final GEOVectorLayer copy()
  {
    return new GEOVectorLayer();
  }

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

  public final TileImageContribution contribution(Tile tile)
  {
    if ((_condition == null) || _condition.isAvailable(tile))
    {
      return (_quadTree.getSector().touchesWith(tile._sector) && !_quadTree.isEmpty() ? TileImageContribution.fullCoverageTransparent(_transparency) : null);
    }
    return null;
  }

  public final QuadTree getQuadTree()
  {
    return _quadTree;
  }

  public final java.util.ArrayList<URL> getDownloadURLs(Tile tile)
  {
    java.util.ArrayList<URL> result = new java.util.ArrayList<URL>();
    return result;
  }

}