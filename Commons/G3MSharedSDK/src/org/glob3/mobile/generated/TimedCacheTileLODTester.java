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


public class TimedCacheTileLODTester extends DecoratorTileLODTester
{
  private long _timeoutInMS;

  private static class PvtData extends TileData
  {
    public boolean _lastMeetsRenderCriteriaResult;
    public long _lastMeetsRenderCriteriaTimeInMS;

    public PvtData(long now)
    {
       super(DefineConstants.TimedCacheTLTDataID);
      _lastMeetsRenderCriteriaTimeInMS = now;
      _lastMeetsRenderCriteriaResult = false;
    }
  }


  public TimedCacheTileLODTester(TimeInterval timeout, TileLODTester tileLODTester)
  {
     super(tileLODTester);
     _timeoutInMS = timeout.milliseconds();
  }

  public void dispose()
  {
    super.dispose();
  }

  public final boolean meetsRenderCriteria(G3MRenderContext rc, PlanetRenderContext prc, Tile tile)
  {
  
    final long nowInMS = prc._nowInMS;
  
    PvtData data = (PvtData) tile.getData(DefineConstants.TimedCacheTLTDataID);
    if (data == null)
    {
      data = new PvtData(nowInMS);
      tile.setData(data);
      data._lastMeetsRenderCriteriaResult = _tileLODTester.meetsRenderCriteria(rc, prc, tile);
    }
    else if ((nowInMS - data._lastMeetsRenderCriteriaTimeInMS) > _timeoutInMS)
    {
      data._lastMeetsRenderCriteriaTimeInMS = nowInMS;
      data._lastMeetsRenderCriteriaResult = _tileLODTester.meetsRenderCriteria(rc, prc, tile);
    }
  
    return data._lastMeetsRenderCriteriaResult;
  }

}
