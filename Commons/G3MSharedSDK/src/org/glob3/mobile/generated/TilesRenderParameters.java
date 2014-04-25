package org.glob3.mobile.generated; 
public class TilesRenderParameters
{
  public final boolean _renderDebug;
  public final boolean _useTilesSplitBudget;
  public final boolean _forceFirstLevelTilesRenderOnStart;
  public final boolean _incrementalTileQuality;
  public double _texturePixelsPerInch; //UNIT: Dots / Inch^2 (ppi)

  public final Quality _quality;

  public TilesRenderParameters(boolean renderDebug, boolean useTilesSplitBudget, boolean forceFirstLevelTilesRenderOnStart, boolean incrementalTileQuality, Quality quality)
  {
     _renderDebug = renderDebug;
     _useTilesSplitBudget = useTilesSplitBudget;
     _forceFirstLevelTilesRenderOnStart = forceFirstLevelTilesRenderOnStart;
     _incrementalTileQuality = incrementalTileQuality;
     _quality = quality;
    switch (quality)
    {
      case QUALITY_LOW:
        _texturePixelsPerInch = 128 / 2;
        break;
      case QUALITY_MEDIUM:
        _texturePixelsPerInch = 256 / 2;
        break;
      default: //HIGH
        _texturePixelsPerInch = 512 / 2;
        break;
    }
  }

  public void dispose()
  {
  }

}