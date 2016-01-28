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

  public final boolean isVisible(Tile tile, G3MRenderContext rc, long nowInMS)
  {
    // return _tileVisibilityTester->isVisible(tile, rc, nowInMS);
  
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#warning Calculate ID;
    int _id = 32;
  
    boolean result;
    PvtData data = (PvtData) tile.getData(_id);
  
    if (data == null)
    {
      result = _tileVisibilityTester.isVisible(tile, rc, nowInMS);
      if (result)
      {
        data = new PvtData(nowInMS + _timeoutInMS);
        tile.setData(_id, data);
      }
    }
    else
    {
      if (data._timeoutTimeInMS <= nowInMS)
      {
        result = true;
      }
      else
      {
        result = _tileVisibilityTester.isVisible(tile, rc, nowInMS);
        if (result)
        {
          data._timeoutTimeInMS = nowInMS + _timeoutInMS;
        }
        else
        {
          tile.setData(_id, null);
        }
      }
    }
  
    return result;
  }

}