package org.glob3.mobile.generated;//
//  TimedCacheTileVisibilityTester.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/28/16.
//
//

//
//  TimedCacheTileVisibilityTester.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/28/16.
//
//




//class TimeInterval;


public class TimedCacheTileVisibilityTester extends DecoratorTileVisibilityTester
{
  private long _timeoutInMS;

  private static class PvtData extends TileData
  {
    public long _timeoutTimeInMS;

    public PvtData(long timeoutTimeInMS)
    {
       super(DefineConstants.TimedCacheTVTDataID);
       _timeoutTimeInMS = timeoutTimeInMS;
    }
  }

  public TimedCacheTileVisibilityTester(TimeInterval timeout, TileVisibilityTester tileVisibilityTester)
  {
     super(tileVisibilityTester);
     _timeoutInMS = timeout.milliseconds();
  
  }

  public void dispose()
  {
    super.dispose();
  }

  public final boolean isVisible(G3MRenderContext rc, PlanetRenderContext prc, Tile tile)
  {
  
    final long nowInMS = prc._nowInMS;
  
    boolean result;
    PvtData data = (PvtData) tile.getData(DefineConstants.TimedCacheTVTDataID);
  
    if (data == null)
    {
      result = _tileVisibilityTester.isVisible(rc, prc, tile);
      if (result)
      {
        data = new PvtData(nowInMS + _timeoutInMS);
        tile.setData(data);
      }
    }
    else
    {
      if (data._timeoutTimeInMS > nowInMS)
      {
        result = true;
      }
      else
      {
        result = _tileVisibilityTester.isVisible(rc, prc, tile);
        if (result)
        {
          data._timeoutTimeInMS = nowInMS + _timeoutInMS;
        }
        else
        {
          tile.setData(data);
        }
      }
    }
  
    return result;
  }

  public final void renderStarted()
  {
  
  }

}
