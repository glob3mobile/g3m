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



//C++ TO JAVA CONVERTER TODO TASK: The following method format was not recognized, possibly due to an unrecognized macro:
  ChessboardLayer(const java.util.ArrayList<LayerTilesRenderParameters> parametersVector, const Color& backgroundColor = Color.white(), const Color& boxColor = Color.fromRGBA(0.9f, 0.9f, 0.35f, 1.0f), const int splits = 8, const Sector& dataSector = Sector.FULL_SPHERE, const float transparency = 1.0f, const LayerCondition* condition = null, java.util.ArrayList<Info>* layerInfo = new java.util.ArrayList<Info>()) : ProceduralLayer(parametersVector, transparency, condition, layerInfo), _dataSector(dataSector), _backgroundColor(backgroundColor), _boxColor(boxColor), _splits(splits)
  {
  }

//C++ TO JAVA CONVERTER TODO TASK: The following statement was not recognized, possibly due to an unrecognized macro:
  ChessboardLayer(const int mercatorFirstLevel = 2, const int mercatorMaxLevel = 18, const int wgs84firstLevel = 0, const int wgs84maxLevel = 18, const Color& backgroundColor = Color.white(), const Color& boxColor = Color.fromRGBA(0.9f, 0.9f, 0.35f, 1.0f), const int splits = 8, const Sector& dataSector = Sector.FULL_SPHERE, const float transparency = 1.0f, const LayerCondition* condition = null, java.util.ArrayList<Info>* layerInfo = new java.util.ArrayList<Info>());

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