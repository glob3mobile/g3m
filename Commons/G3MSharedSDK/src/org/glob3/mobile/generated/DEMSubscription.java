package org.glob3.mobile.generated; 
//
//  DEMSubscription.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/20/16.
//
//

//
//  DEMSubscription.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/20/16.
//
//



//class DEMListener;


public class DEMSubscription extends RCObject
{
  private static long _instanceCounter = 0;

  private final Vector2I _extent;

  private DEMListener _listener;
  private final boolean _deleteListener;

  public void dispose()
  {
    if (_deleteListener)
    {
      if (_listener != null)
         _listener.dispose();
    }
    super.dispose();
  }

  public final long _id;
  public final Sector _sector ;
  public final Geodetic2D _resolution ;

  public DEMSubscription(Sector sector, Vector2I extent, DEMListener listener, boolean deleteListener)
  {
     _id = ++_instanceCounter;
     _sector = new Sector(sector);
     _extent = extent;
     _resolution = new Geodetic2D(sector._deltaLatitude.div(extent._y), sector._deltaLongitude.div(extent._x));
     _listener = listener;
     _deleteListener = deleteListener;
  }

}