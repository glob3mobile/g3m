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


public class TilesRenderParameters
{
  public final boolean _renderDebug;
  public final boolean _useTilesSplitBudget;
  public final boolean _forceFirstLevelTilesRenderOnStart;
  public final boolean _incrementalTileQuality;
  public final double _texturePixelsPerInch; //UNIT: Dots / Inch^2 (ppi)

  public TilesRenderParameters(boolean renderDebug, boolean useTilesSplitBudget, boolean forceFirstLevelTilesRenderOnStart, boolean incrementalTileQuality, double texturePixelsPerInch)
  {
     _renderDebug = renderDebug;
     _useTilesSplitBudget = useTilesSplitBudget;
     _forceFirstLevelTilesRenderOnStart = forceFirstLevelTilesRenderOnStart;
     _incrementalTileQuality = incrementalTileQuality;
     _texturePixelsPerInch = texturePixelsPerInch;
  }

  public void dispose()
  {
  }

}