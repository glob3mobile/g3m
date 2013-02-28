package org.glob3.mobile.generated; 
//
//  TilesRenderParameters.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 08/08/12.
//
//

//
//  TilesRenderParameters.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 08/08/12.
//
//


///#include "Sector.hpp"
///#include "Vector2I.hpp"

public class TilesRenderParameters
{
  public final boolean _renderDebug;
  public final boolean _useTilesSplitBudget;
  public final boolean _forceTopLevelTilesRenderOnStart;
  public final boolean _incrementalTileQuality;

  public TilesRenderParameters(boolean renderDebug, boolean useTilesSplitBudget, boolean forceTopLevelTilesRenderOnStart, boolean incrementalTileQuality)
  {
     _renderDebug = renderDebug;
     _useTilesSplitBudget = useTilesSplitBudget;
     _forceTopLevelTilesRenderOnStart = forceTopLevelTilesRenderOnStart;
     _incrementalTileQuality = incrementalTileQuality;

  }

  public void dispose()
  {
  }

//  static TilesRenderParameters* createDefault(const bool renderDebug,
//                                              const bool useTilesSplitBudget,
//                                              const bool forceTopLevelTilesRenderOnStart,
//                                              const bool incrementalTileQuality) {
//    return new TilesRenderParameters(renderDebug,
//                                     useTilesSplitBudget,
//                                     forceTopLevelTilesRenderOnStart,
//                                     incrementalTileQuality);
//  }

}