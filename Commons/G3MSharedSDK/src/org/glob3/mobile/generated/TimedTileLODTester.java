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

  public boolean meetsRenderCriteria(Tile tile, G3MRenderContext rc)
  {

    long now = rc.getFrameStartTimer().nowInMilliseconds();

    TimedTileLODTesterData data = (TimedTileLODTesterData) tile.getDataForLODTester(_id);
    if (data == null)
    {
      data = new TimedTileLODTesterData(now);
      tile.setDataForLODTester(_id, data);
      data._lastMeetsRenderCriteriaResult = (_nextTester == null)? true : _nextTester.meetsRenderCriteria(tile, rc);
    }

    if ((now - data._lastMeetsRenderCriteriaTimeInMS) > _timeInMs)
    {
      data._lastMeetsRenderCriteriaResult = (_nextTester == null)? true : _nextTester.meetsRenderCriteria(tile, rc);
    }

    return data._lastMeetsRenderCriteriaResult;
  }

  public boolean isVisible(Tile tile, G3MRenderContext rc)
  {
    if (_nextTester == null)
    {
      return true;
    }
    return _nextTester.isVisible(tile, rc);
  }

  public void onTileHasChangedMesh(Tile tile)
  {
    if (_nextTester != null)
    {
      _nextTester.onTileHasChangedMesh(tile);
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