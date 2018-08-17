package org.glob3.mobile.generated;import java.util.*;

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



//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
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
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	super.dispose();
//#endif
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean meetsRenderCriteria(const G3MRenderContext* rc, const PlanetRenderContext* prc, const Tile* tile) const
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
