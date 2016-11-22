package org.glob3.mobile.generated;
//
//  DEMGrid.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/5/16.
//
//

//
//  DEMGrid.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/5/16.
//
//




//class Projection;


public abstract class DEMGrid extends RCObject
{
  protected final Sector _sector ;
  protected final Vector2I _extent;
  protected final Geodetic2D _resolution ;

  protected DEMGrid(Sector sector, Vector2I extent)
  {
     _sector = new Sector(sector);
     _extent = extent;
     _resolution = new Geodetic2D(sector._deltaLatitude.div(extent._y), sector._deltaLongitude.div(extent._x));
  }

  public void dispose()
  {
    super.dispose();
  }


  public final Sector getSector()
  {
    return _sector;
  }

  public final Vector2I getExtent()
  {
    return _extent;
  }

  public final Geodetic2D getResolution()
  {
    return _resolution;
  }

  public abstract Projection getProjection();

  public abstract double getElevationAt(int x, int y);

}
