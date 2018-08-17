package org.glob3.mobile.generated;import java.util.*;

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




//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
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
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	super.dispose();
//#endif
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean meetsRenderCriteria(const G3MRenderContext* rc, const PlanetRenderContext* prc, const Tile* tile) const
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

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void renderStarted() const
  public final void renderStarted()
  {
	_splitsInFrameCounter = 0;
	super.renderStarted();
  }

}
