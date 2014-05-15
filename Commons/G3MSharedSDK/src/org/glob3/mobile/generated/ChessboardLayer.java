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


  private ChessboardLayer(Color backgroundColor, Color boxColor, int splits, Sector dataSector, LayerTilesRenderParameters parameters, float transparency, LayerCondition condition, String disclaimerInfo)
  {
     super(parameters, transparency, condition, disclaimerInfo);
     _dataSector = new Sector(dataSector);
     _backgroundColor = new Color(backgroundColor);
     _boxColor = new Color(boxColor);
     _splits = splits;
  }


  public static ChessboardLayer newMercator(Color backgroundColor, Color boxColor, int splits, Sector dataSector, int firstLevel, int maxLevel, float transparency, LayerCondition condition)
  {
     return newMercator(backgroundColor, boxColor, splits, dataSector, firstLevel, maxLevel, transparency, condition, "");
  }
  public static ChessboardLayer newMercator(Color backgroundColor, Color boxColor, int splits, Sector dataSector, int firstLevel, int maxLevel, float transparency)
  {
     return newMercator(backgroundColor, boxColor, splits, dataSector, firstLevel, maxLevel, transparency, null, "");
  }
  public static ChessboardLayer newMercator(Color backgroundColor, Color boxColor, int splits, Sector dataSector, int firstLevel, int maxLevel)
  {
     return newMercator(backgroundColor, boxColor, splits, dataSector, firstLevel, maxLevel, 1, null, "");
  }
  public static ChessboardLayer newMercator(Color backgroundColor, Color boxColor, int splits, Sector dataSector, int firstLevel)
  {
     return newMercator(backgroundColor, boxColor, splits, dataSector, firstLevel, 18, 1, null, "");
  }
  public static ChessboardLayer newMercator(Color backgroundColor, Color boxColor, int splits, Sector dataSector)
  {
     return newMercator(backgroundColor, boxColor, splits, dataSector, 2, 18, 1, null, "");
  }
  public static ChessboardLayer newMercator(Color backgroundColor, Color boxColor, int splits)
  {
     return newMercator(backgroundColor, boxColor, splits, Sector.fullSphere(), 2, 18, 1, null, "");
  }
  public static ChessboardLayer newMercator(Color backgroundColor, Color boxColor)
  {
     return newMercator(backgroundColor, boxColor, 8, Sector.fullSphere(), 2, 18, 1, null, "");
  }
  public static ChessboardLayer newMercator(Color backgroundColor)
  {
     return newMercator(backgroundColor, Color.fromRGBA(0.9f, 0.9f, 0.35f, 1), 8, Sector.fullSphere(), 2, 18, 1, null, "");
  }
  public static ChessboardLayer newMercator()
  {
     return newMercator(Color.white(), Color.fromRGBA(0.9f, 0.9f, 0.35f, 1), 8, Sector.fullSphere(), 2, 18, 1, null, "");
  }
  public static ChessboardLayer newMercator(Color backgroundColor, Color boxColor, int splits, Sector dataSector, int firstLevel, int maxLevel, float transparency, LayerCondition condition, String disclaimerInfo)
  {
    return new ChessboardLayer(backgroundColor, boxColor, splits, dataSector, LayerTilesRenderParameters.createDefaultMercator(firstLevel, maxLevel), transparency, condition, disclaimerInfo);
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

  public final java.util.ArrayList<Petition> createTileMapPetitions(G3MRenderContext rc, LayerTilesRenderParameters layerTilesRenderParameters, Tile tile)
  {
    java.util.ArrayList<Petition> petitions = new java.util.ArrayList<Petition>();
    return petitions;
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
    return new ChessboardLayer(_backgroundColor, _boxColor, _splits, _dataSector, _parameters.copy(), _transparency, (_condition == null) ? null : _condition.copy(), _disclaimerInfo);
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

}