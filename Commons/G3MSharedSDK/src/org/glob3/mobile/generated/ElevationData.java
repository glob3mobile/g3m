package org.glob3.mobile.generated; 
//
//  ElevationData.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/17/13.
//
//

//
//  ElevationData.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/17/13.
//
//


//class Vector2I;


public abstract class ElevationData
{
  protected final Sector _sector ;
  protected final int _width;
  protected final int _height;

  protected final double _stepInLongitudeRadians;
  protected final double _stepInLatitudeRadians;

  public ElevationData(Sector sector, Vector2I resolution)
  
  {
     _sector = new Sector(sector);
     _width = resolution._x;
     _height = resolution._y;
     _stepInLongitudeRadians = sector.getDeltaLongitude().radians() / resolution._x;
     _stepInLatitudeRadians = sector.getDeltaLatitude().radians() / resolution._y;
  }

  public void dispose()
  {
  }


  //double ElevationData::getElevationAt(int x, int y) const {
  //  //return _buffer->get( (x * _width) + y );
  //  //return _buffer->get( (x * _height) + y );
  //
  //  //        const double height = elevationData->getElevationAt(x, extent._y-1-y);
  //
  ////  const int a = (_height-1-(y+_margin)) * _width;
  ////  const int a1 = _height-1-(y+_margin);
  //
  ////  const int index = ((_height-1-(y+_margin)) * _width) + (x+_margin);
  //  const int index = ((_height-1-y) * _width) + x;
  //  if ((index < 0) ||
  //      (index >= _buffer->size())) {
  //    printf("break point on me\n");
  //  }
  //  return _buffer->get( index );
  //}
  
  public Vector2I getExtent()
  {
    return new Vector2I(_width, _height);
  }

  public abstract double getElevationAt(int x, int y);


  public abstract double getElevationAt(Angle latitude, Angle longitude, int type);

  public double getElevationAt(Geodetic2D position)
  {
    int type;
    return getElevationAt(position.latitude(), position.longitude(), type);
  }

  public abstract String description(boolean detailed);

}