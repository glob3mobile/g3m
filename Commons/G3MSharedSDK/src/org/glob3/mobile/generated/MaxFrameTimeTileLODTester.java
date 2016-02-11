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

  private int _splitsInFrameCounter;


  public MaxFrameTimeTileLODTester(TimeInterval maxFrameTimeInMs, TileLODTester tileLODTester)
  {
     super(tileLODTester);
     _maxFrameTimeInMs = maxFrameTimeInMs.milliseconds();
     _splitsInFrameCounter = 0;
  }

  public void dispose()
  {
    super.dispose();
  }

  public final boolean meetsRenderCriteria(Tile tile, G3MRenderContext rc, TilesRenderParameters tilesRenderParameters, ITimer lastSplitTimer, double texWidthSquared, double texHeightSquared, long nowInMS)
  {
  
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#warning Diego at work!
  
    final boolean hasSubtiles = tile.areSubtilesCreated();
  
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
  
    final boolean result = _tileLODTester.meetsRenderCriteria(tile, rc, tilesRenderParameters, lastSplitTimer, texWidthSquared, texHeightSquared, nowInMS);
  
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