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



public class TimedTileLODTester extends TileLODTester
{
  private TileLODTester _nextTester;
  private long _timeInMs;

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


  public TimedTileLODTester(TimeInterval time, TileLODTester nextTester)
  {
     _timeInMs = time.milliseconds();
     _nextTester = nextTester;
  }

  public void dispose()
  {
    if (_nextTester != null)
       _nextTester.dispose();
  }

  public boolean meetsRenderCriteria(int testerLevel, Tile tile, G3MRenderContext rc)
  {

    long now = rc.getFrameStartTimer().nowInMilliseconds();

    TimedTileLODTesterData data = (TimedTileLODTesterData) tile.getDataForLoDTester(testerLevel);
    if (data == null)
    {
      data = new TimedTileLODTesterData(now);
      tile.setDataForLoDTester(testerLevel, data);
      data._lastMeetsRenderCriteriaResult = (_nextTester == null)? true : _nextTester.meetsRenderCriteria(testerLevel+1, tile, rc);
    }

    if ((now - data._lastMeetsRenderCriteriaTimeInMS) > _timeInMs)
    {
      data._lastMeetsRenderCriteriaResult = (_nextTester == null)? true : _nextTester.meetsRenderCriteria(testerLevel+1, tile, rc);
    }

    return data._lastMeetsRenderCriteriaResult;
  }

  public boolean isVisible(int testerLevel, Tile tile, G3MRenderContext rc)
  {
    if (_nextTester == null)
    {
      return true;
    }
    return _nextTester.isVisible(testerLevel+1, tile, rc);
  }

  public void onTileHasChangedMesh(int testerLevel, Tile tile)
  {
    if (_nextTester != null)
    {
      _nextTester.onTileHasChangedMesh(testerLevel+1, tile);
    }
  }

  public final void onLayerTilesRenderParametersChanged(LayerTilesRenderParameters ltrp)
  {
    if (_nextTester != null)
    {
      _nextTester.onLayerTilesRenderParametersChanged(ltrp);
    }
  }

}