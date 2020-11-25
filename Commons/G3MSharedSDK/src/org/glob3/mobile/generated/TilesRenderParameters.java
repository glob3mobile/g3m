package org.glob3.mobile.generated;
public class TilesRenderParameters
{
  public final boolean _renderDebug;
  public boolean _incrementalTileQuality;
  public double _texturePixelsPerInch; //UNIT: Dots / Inch^2 (ppi)
  public final Quality _quality;

  public TilesRenderParameters(boolean renderDebug, boolean incrementalTileQuality, Quality quality)
  {
     _renderDebug = renderDebug;
     _incrementalTileQuality = incrementalTileQuality;
     _quality = quality;
    switch (quality)
    {
      case QUALITY_LOW:
        _texturePixelsPerInch = 64;
        break;
      case QUALITY_MEDIUM:
        _texturePixelsPerInch = 128;
        break;
      default: //HIGH
        _texturePixelsPerInch = 256;
        break;
    }
  }

  public void dispose()
  {
  }

}