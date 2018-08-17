package org.glob3.mobile.generated;import java.util.*;

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



//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class TimeInterval;


public class TimedCacheTileLODTester extends DecoratorTileLODTester
{
  private long _timeoutInMS;

  private static class PvtData extends TileData
  {
	public boolean _lastMeetsRenderCriteriaResult;
	public long _lastMeetsRenderCriteriaTimeInMS;

	public PvtData(long now)
	{
		super(DefineConstants.TimedCacheTLTDataID);
	  _lastMeetsRenderCriteriaTimeInMS = now;
	  _lastMeetsRenderCriteriaResult = false;
	}
  }


  public TimedCacheTileLODTester(TimeInterval timeout, TileLODTester tileLODTester)
  {
	  super(tileLODTester);
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
//ORIGINAL LINE: boolean meetsRenderCriteria(const G3MRenderContext* rc, const PlanetRenderContext* prc, const Tile* tile) const
  public final boolean meetsRenderCriteria(G3MRenderContext rc, PlanetRenderContext prc, Tile tile)
  {
  
	final long nowInMS = prc._nowInMS;
  
	PvtData data = (PvtData) tile.getData(DefineConstants.TimedCacheTLTDataID);
	if (data == null)
	{
	  data = new PvtData(nowInMS);
	  tile.setData(data);
	  data._lastMeetsRenderCriteriaResult = _tileLODTester.meetsRenderCriteria(rc, prc, tile);
	}
	else if ((nowInMS - data._lastMeetsRenderCriteriaTimeInMS) > _timeoutInMS)
	{
	  data._lastMeetsRenderCriteriaTimeInMS = nowInMS;
	  data._lastMeetsRenderCriteriaResult = _tileLODTester.meetsRenderCriteria(rc, prc, tile);
	}
  
	return data._lastMeetsRenderCriteriaResult;
  }

}
