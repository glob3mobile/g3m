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



//class DEMProvider;
//class DEMListener;
//class DEMGrid;


public class DEMSubscription extends RCObject
{
  private DEMProvider _demProvider;

  private DEMListener _listener;
  private final boolean _deleteListener;

  public void dispose()
  {
    if (_deleteListener)
    {
      if (_listener != null)
         _listener.dispose();
    }
  
    if (_demProvider != null)
    {
      _demProvider.unsubscribe(this);
      _demProvider._release();
    }
  
    super.dispose();
  }

  public final Sector _sector ;
  private final Vector2S _extent;
  public final Geodetic2D _resolution ;

  public DEMSubscription(DEMProvider demProvider, Sector sector, Vector2S extent, DEMListener listener, boolean deleteListener)
  {
     _demProvider = demProvider;
     _sector = new Sector(sector);
     _extent = extent;
     _resolution = new Geodetic2D(sector._deltaLatitude.div(extent._y), sector._deltaLongitude.div(extent._x));
     _listener = listener;
     _deleteListener = deleteListener;
    _demProvider._retain();
  }

  public final void onGrid(DEMGrid grid)
  {
    _listener.onGrid(grid);
  }

  public final void cancel()
  {
    if (_demProvider != null)
    {
      _demProvider.unsubscribe(this);
      _demProvider._release();
      _demProvider = null;
    }
  }

}