package org.glob3.mobile.generated; 
//
//  TileRenderer.cpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 12/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

//
//  TileRenderer.h
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 12/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//


//class Tile;
//class TileTessellator;
//class TileTexturizer;
//class LayerSet;
//class VisibleSectorListenerEntry;
//class VisibleSectorListener;



public class TileRenderContext
{
  private final TileTessellator _tessellator;
  private TileTexturizer _texturizer;
  private final TilesRenderParameters _parameters;
  private TilesStatistics _statistics;

  private final LayerSet _layerSet;

  private final boolean _isForcedFullRender;

  private ITimer _lastSplitTimer; // timer to start every time a tile get splitted into subtiles

  public TileRenderContext(TileTessellator tessellator, TileTexturizer texturizer, LayerSet layerSet, TilesRenderParameters parameters, TilesStatistics statistics, ITimer lastSplitTimer, boolean isForcedFullRender)
  {
     _tessellator = tessellator;
     _texturizer = texturizer;
     _layerSet = layerSet;
     _parameters = parameters;
     _statistics = statistics;
     _lastSplitTimer = lastSplitTimer;
     _isForcedFullRender = isForcedFullRender;

  }

  public final LayerSet getLayerSet()
  {
    return _layerSet;
  }

  public final TileTessellator getTessellator()
  {
    return _tessellator;
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

}