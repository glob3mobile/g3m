package org.glob3.mobile.generated; 
//
//  PlanetRenderer.cpp
//  G3MiOSSDK
//
//  Created by Agustin Trujillo Pino on 12/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

//
//  PlanetRenderer.h
//  G3MiOSSDK
//
//  Created by Agustin Trujillo Pino on 12/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//


//class Tile;
//class TileTessellator;
//class TileTexturizer;
//class LayerSet;
//class VisibleSectorListenerEntry;
//class VisibleSectorListener;
//class ElevationDataProvider;
//class LayerTilesRenderParameters;


//class EllipsoidShape;

//class TileRasterizer;

public class PlanetRendererContext
{
  private final TileTessellator _tessellator;
  private ElevationDataProvider _elevationDataProvider;
  private TileTexturizer _texturizer;
  private TileRasterizer _tileRasterizer;

  private final TilesRenderParameters _parameters;
  private TilesStatistics _statistics;
  private final LayerSet _layerSet;

  private final boolean _isForcedFullRender;

  private final float _verticalExaggeration;


  private ITimer _lastSplitTimer; // timer to start every time a tile get splitted into subtiles

  private long _texturePriority;

  private Sector _renderedSector ;

  private final LayerTilesRenderParameters _layerTilesRenderParameters;

  public PlanetRendererContext(TileTessellator tessellator, ElevationDataProvider elevationDataProvider, TileTexturizer texturizer, TileRasterizer tileRasterizer, LayerSet layerSet, LayerTilesRenderParameters layerTilesRenderParameters, TilesRenderParameters parameters, TilesStatistics statistics, ITimer lastSplitTimer, boolean isForcedFullRender, long texturePriority, float verticalExaggeration, Sector renderedSector)
  {
     _tessellator = tessellator;
     _elevationDataProvider = elevationDataProvider;
     _texturizer = texturizer;
     _tileRasterizer = tileRasterizer;
     _layerSet = layerSet;
     _layerTilesRenderParameters = layerTilesRenderParameters;
     _parameters = parameters;
     _statistics = statistics;
     _lastSplitTimer = lastSplitTimer;
     _isForcedFullRender = isForcedFullRender;
     _texturePriority = texturePriority;
     _verticalExaggeration = verticalExaggeration;
     _renderedSector = new Sector(renderedSector);

  }

  public final TileRasterizer getTileRasterizer()
  {
    return _tileRasterizer;
  }

  public final float getVerticalExaggeration()
  {
    return _verticalExaggeration;
  }

  public final LayerSet getLayerSet()
  {
    return _layerSet;
  }

  public final TileTessellator getTessellator()
  {
    return _tessellator;
  }

  public final ElevationDataProvider getElevationDataProvider()
  {
    return _elevationDataProvider;
  }

  public final TileTexturizer getTexturizer()
  {
    return _texturizer;
  }

  public final TilesRenderParameters getParameters()
  {
    return _parameters;
  }

  public final TilesStatistics getStatistics()
  {
    return _statistics;
  }

  public final ITimer getLastSplitTimer()
  {
    return _lastSplitTimer;
  }

  public final boolean isForcedFullRender()
  {
    return _isForcedFullRender;
  }

  public final long getTexturePriority()
  {
    return _texturePriority;
  }

  public final LayerTilesRenderParameters getLayerTilesRenderParameters()
  {
    return _layerTilesRenderParameters;
  }

  public final Sector getRenderedSector()
  {
    return _renderedSector;
  }

}