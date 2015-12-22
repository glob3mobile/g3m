package org.glob3.mobile.generated; 
//
//  MaxFrameTimeTileLODTester.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 22/12/15.
//
//

//
//  MaxFrameTimeTileLODTester.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 22/12/15.
//
//




public class MaxFrameTimeTileLODTester extends TileLODTester
{
  private TileLODTester _nextTester;
  private long _maxFrameTimeInMs;


  public MaxFrameTimeTileLODTester(long maxFrameTimeInMs, TileLODTester nextTester)
  {
     _maxFrameTimeInMs = maxFrameTimeInMs;
     _nextTester = nextTester;
  }

  public void dispose()
  {
    if (_nextTester != null)
       _nextTester.dispose();
  }

  public boolean meetsRenderCriteria(int testerLevel, Tile tile, G3MRenderContext rc)
  {

    if (!tile.areSubtilesCreated() && rc.getFrameStartTimer().elapsedTimeInMilliseconds() > _maxFrameTimeInMs)
    {
      return true;
    }

    return _nextTester.meetsRenderCriteria(testerLevel+1, tile, rc);
  }

  public boolean isVisible(int testerLevel, Tile tile, G3MRenderContext rc)
  {
    return _nextTester.isVisible(testerLevel+1, tile, rc);
  }

  public void onTileHasChangedMesh(int testerLevel, Tile tile)
  {
    _nextTester.onTileHasChangedMesh(testerLevel+1, tile);
  }

}