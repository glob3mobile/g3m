package org.glob3.mobile.generated; 
//
//  SingleBillElevationDataProvider.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/21/13.
//
//

//
//  SingleBillElevationDataProvider.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/21/13.
//
//





public class SingleBillElevationDataProvider_Request
{
  public final Sector _sector ;
  public final Vector2I _extent;
  public final IElevationDataListener _listener;
  public final boolean _autodeleteListener;

  public SingleBillElevationDataProvider_Request(Sector sector, Vector2I extent, IElevationDataListener listener, boolean autodeleteListener)
  {
     _sector = new Sector(sector);
     _extent = extent;
     _listener = listener;
     _autodeleteListener = autodeleteListener;
  }

  public void dispose()
  {
  }

}