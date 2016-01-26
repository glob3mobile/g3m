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


  public TimedTileLODTester(TimeInterval time, TileLODTester tileLODTester)
  {
     super(tileLODTester);
     _timeInMs = time.milliseconds();
  }

  public void dispose()
  {
    super.dispose();
  }

  public final boolean meetsRenderCriteria(Tile tile, G3MRenderContext rc)
  {
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#warning TODO: move now up in the chain
    long now = rc.getFrameStartTimer().nowInMilliseconds();
  
    TimedTileLODTesterData data = (TimedTileLODTesterData) tile.getDataForLODTester(_id);
    if (data == null)
    {
      data = new TimedTileLODTesterData(now);
      tile.setDataForLODTester(_id, data);
      data._lastMeetsRenderCriteriaResult = _tileLODTester.meetsRenderCriteria(tile, rc);
    }
    else if ((now - data._lastMeetsRenderCriteriaTimeInMS) > _timeInMs)
    {
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#warning TODO: talk with JM
      data._lastMeetsRenderCriteriaTimeInMS = now;
      data._lastMeetsRenderCriteriaResult = _tileLODTester.meetsRenderCriteria(tile, rc);
    }
  
    return data._lastMeetsRenderCriteriaResult;
  }

  public final boolean isVisible(Tile tile, G3MRenderContext rc)
  {
    return _tileLODTester.isVisible(tile, rc);
  }

  public final void onTileHasChangedMesh(Tile tile)
  {
    _tileLODTester.onTileHasChangedMesh(tile);
  }

  public final void onLayerTilesRenderParametersChanged(LayerTilesRenderParameters ltrp)
  {
    _tileLODTester.onLayerTilesRenderParametersChanged(ltrp);
  }

}