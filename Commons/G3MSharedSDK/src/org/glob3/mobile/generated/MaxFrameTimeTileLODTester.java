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
  private final long _maxFrameTimeInMs;

  private int _splitsInFrameCounter;


  public MaxFrameTimeTileLODTester(TimeInterval maxFrameTime, TileLODTester tileLODTester)
  {
     super(tileLODTester);
     _maxFrameTimeInMs = maxFrameTime.milliseconds();
     _splitsInFrameCounter = 0;
  }

  public void dispose()
  {
    super.dispose();
  }

  public final boolean meetsRenderCriteria(G3MRenderContext rc, PlanetRenderContext prc, Tile tile)
  {
    final boolean hasSubtiles = tile.hasSubtiles();
  
    if (!hasSubtiles)
    {
      if (_splitsInFrameCounter > 0)
      {
        long elapsedTime = rc.getFrameStartTimer().elapsedTimeInMilliseconds();
        if (elapsedTime > _maxFrameTimeInMs)
        {
          return true;
        }
      }
    }
  
    final boolean result = _tileLODTester.meetsRenderCriteria(rc, prc, tile);
  
    if (!result && !hasSubtiles)
    {
      _splitsInFrameCounter++;
    }
  
    return result;
  }

  public final void renderStarted()
  {
    _splitsInFrameCounter = 0;
    super.renderStarted();
  }

}