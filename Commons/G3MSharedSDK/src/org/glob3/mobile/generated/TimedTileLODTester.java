package org.glob3.mobile.generated; 
//
//  TimedLODTester.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 22/12/15.
//
//

//
//  TimedLODTester.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 22/12/15.
//
//



//class TimeInterval;


public class TimedTileLODTester extends DecoratorTileLODTester
{
  private long _timeoutInMS;

  private static class TimedTileLODTesterData extends TileLODTesterData
  {
    public boolean _lastMeetsRenderCriteriaResult;
    public long _lastMeetsRenderCriteriaTimeInMS;

    public TimedTileLODTesterData(long now)
    {
      _lastMeetsRenderCriteriaTimeInMS = now;
      _lastMeetsRenderCriteriaResult = false;
    }
  }


  public TimedTileLODTester(TimeInterval timeout, TileLODTester tileLODTester)
  {
     super(tileLODTester);
     _timeoutInMS = timeout.milliseconds();
  }

  public void dispose()
  {
    super.dispose();
  }

  public final boolean meetsRenderCriteria(Tile tile, G3MRenderContext rc, TilesRenderParameters tilesRenderParameters, ITimer lastSplitTimer, double texWidthSquared, double texHeightSquared, long nowInMS)
  {
  
    TimedTileLODTesterData data = (TimedTileLODTesterData) tile.getDataForLODTester(_id);
    if (data == null)
    {
      data = new TimedTileLODTesterData(nowInMS);
      tile.setDataForLODTester(_id, data);
      data._lastMeetsRenderCriteriaResult = _tileLODTester.meetsRenderCriteria(tile, rc, tilesRenderParameters, lastSplitTimer, texWidthSquared, texHeightSquared, nowInMS);
    }
    else if ((nowInMS - data._lastMeetsRenderCriteriaTimeInMS) > _timeoutInMS)
    {
      data._lastMeetsRenderCriteriaTimeInMS = nowInMS;
      data._lastMeetsRenderCriteriaResult = _tileLODTester.meetsRenderCriteria(tile, rc, tilesRenderParameters, lastSplitTimer, texWidthSquared, texHeightSquared, nowInMS);
    }
  
    return data._lastMeetsRenderCriteriaResult;
  }

}