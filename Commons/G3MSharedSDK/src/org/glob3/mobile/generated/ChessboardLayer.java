package org.glob3.mobile.generated; 
//
//  ChessboardLayer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 5/15/14.
//
//

//
//  ChessboardLayer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 5/15/14.
//
//



public class ChessboardLayer extends ProceduralLayer
{
  private final Sector _dataSector ;

  private final Color _backgroundColor ;
  private final Color _boxColor ;
  private final int _splits;



  public ChessboardLayer(java.util.ArrayList<LayerTilesRenderParameters> parametersVector, Color backgroundColor, Color boxColor, int splits, Sector dataSector, float transparency, LayerCondition condition)
  {
     this(parametersVector, backgroundColor, boxColor, splits, dataSector, transparency, condition, new java.util.ArrayList<Info>());
  }
  public ChessboardLayer(java.util.ArrayList<LayerTilesRenderParameters> parametersVector, Color backgroundColor, Color boxColor, int splits, Sector dataSector, float transparency)
  {
     this(parametersVector, backgroundColor, boxColor, splits, dataSector, transparency, null, new java.util.ArrayList<Info>());
  }
  public ChessboardLayer(java.util.ArrayList<LayerTilesRenderParameters> parametersVector, Color backgroundColor, Color boxColor, int splits, Sector dataSector)
  {
     this(parametersVector, backgroundColor, boxColor, splits, dataSector, 1.0f, null, new java.util.ArrayList<Info>());
  }
  public ChessboardLayer(java.util.ArrayList<LayerTilesRenderParameters> parametersVector, Color backgroundColor, Color boxColor, int splits)
  {
     this(parametersVector, backgroundColor, boxColor, splits, Sector.fullSphere(), 1.0f, null, new java.util.ArrayList<Info>());
  }
  public ChessboardLayer(java.util.ArrayList<LayerTilesRenderParameters> parametersVector, Color backgroundColor, Color boxColor)
  {
     this(parametersVector, backgroundColor, boxColor, 8, Sector.fullSphere(), 1.0f, null, new java.util.ArrayList<Info>());
  }
  public ChessboardLayer(java.util.ArrayList<LayerTilesRenderParameters> parametersVector, Color backgroundColor)
  {
     this(parametersVector, backgroundColor, Color.fromRGBA(0.9f, 0.9f, 0.35f, 1.0f), 8, Sector.fullSphere(), 1.0f, null, new java.util.ArrayList<Info>());
  }
  public ChessboardLayer(java.util.ArrayList<LayerTilesRenderParameters> parametersVector)
  {
     this(parametersVector, Color.white(), Color.fromRGBA(0.9f, 0.9f, 0.35f, 1.0f), 8, Sector.fullSphere(), 1.0f, null, new java.util.ArrayList<Info>());
  }
  public ChessboardLayer(java.util.ArrayList<LayerTilesRenderParameters> parametersVector, Color backgroundColor, Color boxColor, int splits, Sector dataSector, float transparency, LayerCondition condition, java.util.ArrayList<Info> layerInfo)
  {
     super(parametersVector, transparency, condition, layerInfo);
     _dataSector = new Sector(dataSector);
     _backgroundColor = new Color(backgroundColor);
     _boxColor = new Color(boxColor);
     _splits = splits;
  }

  public ChessboardLayer(int mercatorFirstLevel, int mercatorMaxLevel, int wgs84firstLevel, int wgs84maxLevel, Color backgroundColor, Color boxColor, int splits, Sector dataSector, float transparency, LayerCondition condition)
  {
     this(mercatorFirstLevel, mercatorMaxLevel, wgs84firstLevel, wgs84maxLevel, backgroundColor, boxColor, splits, dataSector, transparency, condition, new java.util.ArrayList<Info>());
  }
  public ChessboardLayer(int mercatorFirstLevel, int mercatorMaxLevel, int wgs84firstLevel, int wgs84maxLevel, Color backgroundColor, Color boxColor, int splits, Sector dataSector, float transparency)
  {
     this(mercatorFirstLevel, mercatorMaxLevel, wgs84firstLevel, wgs84maxLevel, backgroundColor, boxColor, splits, dataSector, transparency, null, new java.util.ArrayList<Info>());
  }
  public ChessboardLayer(int mercatorFirstLevel, int mercatorMaxLevel, int wgs84firstLevel, int wgs84maxLevel, Color backgroundColor, Color boxColor, int splits, Sector dataSector)
  {
     this(mercatorFirstLevel, mercatorMaxLevel, wgs84firstLevel, wgs84maxLevel, backgroundColor, boxColor, splits, dataSector, 1.0f, null, new java.util.ArrayList<Info>());
  }
  public ChessboardLayer(int mercatorFirstLevel, int mercatorMaxLevel, int wgs84firstLevel, int wgs84maxLevel, Color backgroundColor, Color boxColor, int splits)
  {
     this(mercatorFirstLevel, mercatorMaxLevel, wgs84firstLevel, wgs84maxLevel, backgroundColor, boxColor, splits, Sector.fullSphere(), 1.0f, null, new java.util.ArrayList<Info>());
  }
  public ChessboardLayer(int mercatorFirstLevel, int mercatorMaxLevel, int wgs84firstLevel, int wgs84maxLevel, Color backgroundColor, Color boxColor)
  {
     this(mercatorFirstLevel, mercatorMaxLevel, wgs84firstLevel, wgs84maxLevel, backgroundColor, boxColor, 8, Sector.fullSphere(), 1.0f, null, new java.util.ArrayList<Info>());
  }
  public ChessboardLayer(int mercatorFirstLevel, int mercatorMaxLevel, int wgs84firstLevel, int wgs84maxLevel, Color backgroundColor)
  {
     this(mercatorFirstLevel, mercatorMaxLevel, wgs84firstLevel, wgs84maxLevel, backgroundColor, Color.fromRGBA(0.9f, 0.9f, 0.35f, 1.0f), 8, Sector.fullSphere(), 1.0f, null, new java.util.ArrayList<Info>());
  }
  public ChessboardLayer(int mercatorFirstLevel, int mercatorMaxLevel, int wgs84firstLevel, int wgs84maxLevel)
  {
     this(mercatorFirstLevel, mercatorMaxLevel, wgs84firstLevel, wgs84maxLevel, Color.white(), Color.fromRGBA(0.9f, 0.9f, 0.35f, 1.0f), 8, Sector.fullSphere(), 1.0f, null, new java.util.ArrayList<Info>());
  }
  public ChessboardLayer(int mercatorFirstLevel, int mercatorMaxLevel, int wgs84firstLevel)
  {
     this(mercatorFirstLevel, mercatorMaxLevel, wgs84firstLevel, 18, Color.white(), Color.fromRGBA(0.9f, 0.9f, 0.35f, 1.0f), 8, Sector.fullSphere(), 1.0f, null, new java.util.ArrayList<Info>());
  }
  public ChessboardLayer(int mercatorFirstLevel, int mercatorMaxLevel)
  {
     this(mercatorFirstLevel, mercatorMaxLevel, 0, 18, Color.white(), Color.fromRGBA(0.9f, 0.9f, 0.35f, 1.0f), 8, Sector.fullSphere(), 1.0f, null, new java.util.ArrayList<Info>());
  }
  public ChessboardLayer(int mercatorFirstLevel)
  {
     this(mercatorFirstLevel, 18, 0, 18, Color.white(), Color.fromRGBA(0.9f, 0.9f, 0.35f, 1.0f), 8, Sector.fullSphere(), 1.0f, null, new java.util.ArrayList<Info>());
  }
  public ChessboardLayer()
  {
     this(2, 18, 0, 18, Color.white(), Color.fromRGBA(0.9f, 0.9f, 0.35f, 1.0f), 8, Sector.fullSphere(), 1.0f, null, new java.util.ArrayList<Info>());
  }
  public ChessboardLayer(int mercatorFirstLevel, int mercatorMaxLevel, int wgs84firstLevel, int wgs84maxLevel, Color backgroundColor, Color boxColor, int splits, Sector dataSector, float transparency, LayerCondition condition, java.util.ArrayList<Info> layerInfo)
  {
     super(LayerTilesRenderParameters.createDefaultMultiProjection(mercatorFirstLevel, mercatorMaxLevel, wgs84firstLevel, wgs84maxLevel), transparency, condition, layerInfo);
     _dataSector = new Sector(dataSector);
     _backgroundColor = new Color(backgroundColor);
     _boxColor = new Color(boxColor);
     _splits = splits;
  }

  public final String getLayerType()
  {
    return "ChessboardLayer";
  }

  public final RenderState getRenderState()
  {
    return RenderState.ready();
  }

  public final Sector getDataSector()
  {
    return _dataSector;
  }

  public final String description()
  {
    return "[ChessboardLayer]";
  }

  public final URL getFeatureInfoURL(Geodetic2D position, Sector sector)
  {
    return new URL();
  }

  public final ChessboardLayer copy()
  {
    return new ChessboardLayer(createParametersVectorCopy(), _backgroundColor, _boxColor, _splits, _dataSector, _transparency, (_condition == null) ? null : _condition.copy(), _layerInfo);
  }

  public final TileImageProvider createTileImageProvider(G3MRenderContext rc, LayerTilesRenderParameters layerTilesRenderParameters)
  {
    return new ChessboardTileImageProvider(_backgroundColor, _boxColor, _splits);
  }

  public final boolean rawIsEquals(Layer that)
  {
    final ChessboardLayer t = (ChessboardLayer) that;
  
    if (!_backgroundColor.isEquals(t._backgroundColor))
    {
      return false;
    }
  
    if (!_boxColor.isEquals(t._boxColor))
    {
      return false;
    }
  
    if (_splits != t._splits)
    {
      return false;
    }
  
    return _dataSector.isEquals(t._dataSector);
  }

  public final java.util.ArrayList<URL> getDownloadURLs(Tile tile)
  {
    java.util.ArrayList<URL> result = new java.util.ArrayList<URL>();
    return result;
  }

}