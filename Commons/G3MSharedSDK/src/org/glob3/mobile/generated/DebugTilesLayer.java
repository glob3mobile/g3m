package org.glob3.mobile.generated;
//
//  DebugTilesLayer.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 10/1/26.
//
//

//
//  DebugTilesLayer.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 10/1/26.
//
//



// Transparent tiles with the ID, sector and bounds of each tile drawn on them, to put over any other layer
public class DebugTilesLayer extends ProceduralLayer
{
  private final GFont _font;
  private final Color _color ;
  private final boolean _showIDLabel;
  private final boolean _showSectorLabels;
  private final boolean _showTileBounds;

  protected final String getLayerType()
  {
    return "DebugTilesLayer";
  }

  protected final boolean rawIsEquals(Layer that)
  {
    final DebugTilesLayer t = (DebugTilesLayer) that;
  
    // GFont has no isEquals, so the font is not compared
    if (!_color.isEquals(t._color))
    {
      return false;
    }
  
    if (_showIDLabel != t._showIDLabel)
    {
      return false;
    }
  
    if (_showSectorLabels != t._showSectorLabels)
    {
      return false;
    }
  
    return (_showTileBounds == t._showTileBounds);
  }


  public DebugTilesLayer(java.util.ArrayList<LayerTilesRenderParameters> parametersVector, GFont font, Color color, boolean showIDLabel, boolean showSectorLabels, boolean showTileBounds, float transparency, LayerCondition condition, java.util.ArrayList<Info> layerInfo)
  {
     super(parametersVector, transparency, condition, layerInfo);
     _font = font;
     _color = color;
     _showIDLabel = showIDLabel;
     _showSectorLabels = showSectorLabels;
     _showTileBounds = showTileBounds;
  }

  public DebugTilesLayer()
  {
     super(LayerTilesRenderParameters.createDefaultMultiProjection(2, 18, 0, 18), 1.0f, null, new java.util.ArrayList<Info>());
     _font = new GFont(GFont.monospaced(15));
     _color = Color.YELLOW;
     _showIDLabel = true;
     _showSectorLabels = true;
     _showTileBounds = true;
  }

  public DebugTilesLayer(GFont font, Color color, boolean showIDLabel, boolean showSectorLabels, boolean showTileBounds, int mercatorFirstLevel, int mercatorMaxLevel, int wgs84firstLevel, int wgs84maxLevel, float transparency, LayerCondition condition)
  {
     this(font, color, showIDLabel, showSectorLabels, showTileBounds, mercatorFirstLevel, mercatorMaxLevel, wgs84firstLevel, wgs84maxLevel, transparency, condition, new java.util.ArrayList<Info>());
  }
  public DebugTilesLayer(GFont font, Color color, boolean showIDLabel, boolean showSectorLabels, boolean showTileBounds, int mercatorFirstLevel, int mercatorMaxLevel, int wgs84firstLevel, int wgs84maxLevel, float transparency)
  {
     this(font, color, showIDLabel, showSectorLabels, showTileBounds, mercatorFirstLevel, mercatorMaxLevel, wgs84firstLevel, wgs84maxLevel, transparency, null, new java.util.ArrayList<Info>());
  }
  public DebugTilesLayer(GFont font, Color color, boolean showIDLabel, boolean showSectorLabels, boolean showTileBounds, int mercatorFirstLevel, int mercatorMaxLevel, int wgs84firstLevel, int wgs84maxLevel)
  {
     this(font, color, showIDLabel, showSectorLabels, showTileBounds, mercatorFirstLevel, mercatorMaxLevel, wgs84firstLevel, wgs84maxLevel, 1.0f, null, new java.util.ArrayList<Info>());
  }
  public DebugTilesLayer(GFont font, Color color, boolean showIDLabel, boolean showSectorLabels, boolean showTileBounds, int mercatorFirstLevel, int mercatorMaxLevel, int wgs84firstLevel)
  {
     this(font, color, showIDLabel, showSectorLabels, showTileBounds, mercatorFirstLevel, mercatorMaxLevel, wgs84firstLevel, 18, 1.0f, null, new java.util.ArrayList<Info>());
  }
  public DebugTilesLayer(GFont font, Color color, boolean showIDLabel, boolean showSectorLabels, boolean showTileBounds, int mercatorFirstLevel, int mercatorMaxLevel)
  {
     this(font, color, showIDLabel, showSectorLabels, showTileBounds, mercatorFirstLevel, mercatorMaxLevel, 0, 18, 1.0f, null, new java.util.ArrayList<Info>());
  }
  public DebugTilesLayer(GFont font, Color color, boolean showIDLabel, boolean showSectorLabels, boolean showTileBounds, int mercatorFirstLevel)
  {
     this(font, color, showIDLabel, showSectorLabels, showTileBounds, mercatorFirstLevel, 18, 0, 18, 1.0f, null, new java.util.ArrayList<Info>());
  }
  public DebugTilesLayer(GFont font, Color color, boolean showIDLabel, boolean showSectorLabels, boolean showTileBounds)
  {
     this(font, color, showIDLabel, showSectorLabels, showTileBounds, 2, 18, 0, 18, 1.0f, null, new java.util.ArrayList<Info>());
  }
  public DebugTilesLayer(GFont font, Color color, boolean showIDLabel, boolean showSectorLabels, boolean showTileBounds, int mercatorFirstLevel, int mercatorMaxLevel, int wgs84firstLevel, int wgs84maxLevel, float transparency, LayerCondition condition, java.util.ArrayList<Info> layerInfo)
  {
     super(LayerTilesRenderParameters.createDefaultMultiProjection(mercatorFirstLevel, mercatorMaxLevel, wgs84firstLevel, wgs84maxLevel), transparency, condition, layerInfo);
     _font = font;
     _color = color;
     _showIDLabel = showIDLabel;
     _showSectorLabels = showSectorLabels;
     _showTileBounds = showTileBounds;
  }

  public final RenderState getRenderState()
  {
    return RenderState.ready();
  }

  public final Sector getDataSector()
  {
    return Sector.FULL_SPHERE;
  }

  public final String description()
  {
    return "[DebugTilesLayer]";
  }

  public final URL getFeatureInfoURL(Geodetic2D position, Sector sector)
  {
    return URL.nullURL();
  }

  public final DebugTilesLayer copy()
  {
    return new DebugTilesLayer(createParametersVectorCopy(), _font, _color, _showIDLabel, _showSectorLabels, _showTileBounds, _transparency, (_condition == null) ? null : _condition.copy(), _layerInfo);
  }

  public final TileImageProvider createTileImageProvider(G3MRenderContext rc, LayerTilesRenderParameters layerTilesRenderParameters)
  {
    return new DebugTileImageProvider(_font, _color, _showIDLabel, _showSectorLabels, _showTileBounds);
  }

  public final java.util.ArrayList<URL> getDownloadURLs(Tile tile)
  {
    java.util.ArrayList<URL> result = new java.util.ArrayList<URL>();
    return result;
  }

}