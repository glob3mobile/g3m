package org.glob3.mobile.generated; 
//
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
  
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#warning Calculate ID;
    final int id = 32;
  
    boolean result;
    PvtData data = (PvtData) tile.getData(id);
  
    if (data == null)
    {
      result = _tileVisibilityTester.isVisible(rc, prc, tile);
      if (result)
      {
        data = new PvtData(nowInMS + _timeoutInMS);
        tile.setData(id, data);
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
          tile.setData(id, null);
        }
      }
    }
  
    return result;
  }

  public final void renderStarted()
  {
  
  }

}