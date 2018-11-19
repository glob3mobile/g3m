package org.glob3.mobile.generated;
//
//  GradualSplitsTileLODTester.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/6/16.
//
//

//
//  GradualSplitsTileLODTester.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/6/16.
//
//



//class TimeInterval;


public class GradualSplitsTileLODTester extends DecoratorTileLODTester
{
  private final long _delayInMs;


  public GradualSplitsTileLODTester(TimeInterval delay, TileLODTester tileLODTester)
  {
     super(tileLODTester);
     _delayInMs = delay.milliseconds();
  }

  public void dispose()
  {
    super.dispose();
  }

  public final boolean meetsRenderCriteria(G3MRenderContext rc, PlanetRenderContext prc, Tile tile)
  {
  
    final boolean result = _tileLODTester.meetsRenderCriteria(rc, prc, tile);
  
    if (!result)
    {
      final boolean hasSubtiles = tile.hasSubtiles();
  
      if (!hasSubtiles) // the tile needs to create the subtiles
      {
        if (prc._lastSplitTimer.elapsedTimeInMilliseconds() <= _delayInMs)
        {
          // there are not more time-budget to spend
          return true;
        }
      }
    }
  
    return result;
  }

}
