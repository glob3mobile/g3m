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




//class TimeInterval;


public class MaxFrameTimeTileLODTester extends DecoratorTileLODTester
{
  private long _maxFrameTimeInMs;

  private long _lastElapsedTime;
  private int _nSplitsInFrame;


  public MaxFrameTimeTileLODTester(TimeInterval maxFrameTimeInMs, TileLODTester tileLODTester)
  {
     super(tileLODTester);
     _maxFrameTimeInMs = maxFrameTimeInMs.milliseconds();
     _lastElapsedTime = 0;
     _nSplitsInFrame = 0;
  }

  public void dispose()
  {
    super.dispose();
  }

  public final boolean meetsRenderCriteria(Tile tile, G3MRenderContext rc)
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
  
    final boolean res = _tileLODTester.meetsRenderCriteria(tile, rc);
  
    if (!res && !hasSubtiles)
    {
      _nSplitsInFrame++;
    }
  
    return res;
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