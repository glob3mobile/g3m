package org.glob3.mobile.generated;import java.util.*;

//
//  TileElevationDataListener.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 23/04/13.
//
//

//
//  TileElevationDataListener.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 23/04/13.
//
//




public class TileElevationDataRequestListener implements IElevationDataListener
{
  public TileElevationDataRequest _request;

  public TileElevationDataRequestListener(TileElevationDataRequest request)
  {
	  _request = request;
  }

  public final void onData(Sector sector, Vector2I resolution, ElevationData elevationData)
  {
	if (_request != null)
	{
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: _request->onData(sector, resolution, elevationData);
	  _request.onData(new Sector(sector), new Vector2I(resolution), elevationData);
	}
  }

  public final void onError(Sector sector, Vector2I resolution)
  {
	if (_request != null)
	{
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: _request->onError(sector, resolution);
	  _request.onError(new Sector(sector), new Vector2I(resolution));
	}
  }

  public final void onCancel(Sector sector, Vector2I resolution)
  {
	if (_request != null)
	{
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: _request->onCancel(sector, resolution);
	  _request.onCancel(new Sector(sector), new Vector2I(resolution));
	}
  }

  public void dispose()
  {
  }

}
