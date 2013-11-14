package org.glob3.mobile.generated; 
public class TilesRenderParameters
{
  public final boolean _renderDebug;
  public final boolean _useTilesSplitBudget;
  public final boolean _forceFirstLevelTilesRenderOnStart;
  public final boolean _incrementalTileQuality;
  public final Quality _quality;

  public TilesRenderParameters(boolean renderDebug, boolean useTilesSplitBudget, boolean forceFirstLevelTilesRenderOnStart, boolean incrementalTileQuality, Quality quality)
  {
     _renderDebug = renderDebug;
     _useTilesSplitBudget = useTilesSplitBudget;
     _forceFirstLevelTilesRenderOnStart = forceFirstLevelTilesRenderOnStart;
     _incrementalTileQuality = incrementalTileQuality;
     _quality = quality;
  }

  public void dispose()
  {
  }

}