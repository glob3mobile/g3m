package org.glob3.mobile.generated;import java.util.*;

//
//  TimedCacheTileVisibilityTester.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/28/16.
//
//

//
//  TimedCacheTileVisibilityTester.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/28/16.
//
//




//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class TimeInterval;


public class TimedCacheTileVisibilityTester extends DecoratorTileVisibilityTester
{
  private long _timeoutInMS;

  private static class PvtData extends TileData
  {
	public long _timeoutTimeInMS;

	public PvtData(long timeoutTimeInMS)
	{
		super(DefineConstants.TimedCacheTVTDataID);
		_timeoutTimeInMS = timeoutTimeInMS;
	}
  }

  public TimedCacheTileVisibilityTester(TimeInterval timeout, TileVisibilityTester tileVisibilityTester)
  {
	  super(tileVisibilityTester);
	  _timeoutInMS = timeout.milliseconds();
  
  }

  public void dispose()
  {
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	super.dispose();
//#endif
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean isVisible(const G3MRenderContext* rc, const PlanetRenderContext* prc, Tile* tile) const
  public final boolean isVisible(G3MRenderContext rc, PlanetRenderContext prc, Tile tile)
  {
  
	final long nowInMS = prc._nowInMS;
  
	boolean result;
	PvtData data = (PvtData) tile.getData(DefineConstants.TimedCacheTVTDataID);
  
	if (data == null)
	{
	  result = _tileVisibilityTester.isVisible(rc, prc, tile);
	  if (result)
	  {
		data = new PvtData(nowInMS + _timeoutInMS);
		tile.setData(data);
	  }
	}
	else
	{
	  if (data._timeoutTimeInMS > nowInMS)
	  {
		result = true;
	  }
	  else
	  {
		result = _tileVisibilityTester.isVisible(rc, prc, tile);
		if (result)
		{
		  data._timeoutTimeInMS = nowInMS + _timeoutInMS;
		}
		else
		{
		  tile.setData(data);
		}
	  }
	}
  
	return result;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void renderStarted() const
  public final void renderStarted()
  {
  
  }

}
