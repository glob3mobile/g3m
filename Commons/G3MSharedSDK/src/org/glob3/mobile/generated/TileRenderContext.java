package org.glob3.mobile.generated; 
//
//  TileRenderer.cpp
//  G3MiOSSDK
//
//  Created by Agustin Trujillo Pino on 12/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

//
//  TileRenderer.h
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

public class TileRenderContext
{
  private final TileTessellator _tessellator;
  private ElevationDataProvider _elevationDataProvider;
  private TileTexturizer _texturizer;

  private final TilesRenderParameters _parameters;
  private TilesStatistics _statistics;
  private final LayerSet _layerSet;

  private final boolean _isForcedFullRender;

  private final float _verticalExaggeration;


  private ITimer _lastSplitTimer; // timer to start every time a tile get splitted into subtiles

  private long _texturePriority;

  public TileRenderContext(TileTessellator tessellator, ElevationDataProvider elevationDataProvider, TileTexturizer texturizer, LayerSet layerSet, TilesRenderParameters parameters, TilesStatistics statistics, ITimer lastSplitTimer, boolean isForcedFullRender, long texturePriority, float verticalExaggeration)
  {
     _tessellator = tessellator;
     _elevationDataProvider = elevationDataProvider;
     _texturizer = texturizer;
     _layerSet = layerSet;
     _parameters = parameters;
     _statistics = statistics;
     _lastSplitTimer = lastSplitTimer;
     _isForcedFullRender = isForcedFullRender;
     _texturePriority = texturePriority;
     _verticalExaggeration = verticalExaggeration;

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
    return _layerSet.getLayerTilesRenderParameters();
  }

}