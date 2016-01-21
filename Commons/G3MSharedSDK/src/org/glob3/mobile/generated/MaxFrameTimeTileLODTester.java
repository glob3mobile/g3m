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

  private long _lastElapsedTime;
  private int _nSplitsInFrame;


  public MaxFrameTimeTileLODTester(TimeInterval maxFrameTimeInMs, TileLODTester nextTester)
  {
     _maxFrameTimeInMs = maxFrameTimeInMs.milliseconds();
     _nextTester = nextTester;
     _lastElapsedTime = 0;
     _nSplitsInFrame = 0;
  }

  public void dispose()
  {
    if (_nextTester != null)
       _nextTester.dispose();
  }

  public boolean meetsRenderCriteria(int testerLevel, Tile tile, G3MRenderContext rc)
  {

    final boolean hasSubtiles = tile.areSubtilesCreated();
    long elapsedTime = rc.getFrameStartTimer().elapsedTimeInMilliseconds();
    if (elapsedTime < _lastElapsedTime)
    {
      //New frame
      //      if (_nSplitsInFrame > 0) {
      //        printf("Tile splits on last frame: %d\n", _nSplitsInFrame);
      //      }
      _nSplitsInFrame = 0;
    }
    _lastElapsedTime = elapsedTime;

    if (!hasSubtiles && elapsedTime > _maxFrameTimeInMs && _nSplitsInFrame > 0)
    {
      return true;
    }

    boolean res = (_nextTester == null)? true : _nextTester.meetsRenderCriteria(testerLevel+1, tile, rc);

    if (!res && !hasSubtiles)
    {
      _nSplitsInFrame++;
    }

    return res;
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