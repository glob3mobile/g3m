package org.glob3.mobile.generated; 
//
//  InterpolatedSubviewElevationData.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 8/1/13.
//
//

//
//  InterpolatedSubviewElevationData.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 8/1/13.
//
//




public class InterpolatedSubviewElevationData extends SubviewElevationData
{

  public InterpolatedSubviewElevationData(ElevationData elevationData, Sector sector, Vector2I extent)
  {
     super(elevationData, sector, extent);

    if (sector.isEquals(elevationData.getSector()) && extent._x == elevationData.getExtentWidth() && extent._y == elevationData.getExtentHeight())
    {

      //Performing copy
      for (int x = 0; x < _width; x++)
      {
        for (int y = 0; y < _height; y++)
        {
          final int index = ((_height-1-y) * _width) + x;
          _buffer[index] = (float)elevationData.getElevationAt(x, y);
        }
      }

    }
    else
    {

      for (int x = 0; x < _width; x++)
      {
        final double u = (double) x / (_width - 1);

        final Angle longitude = _sector.getInnerPointLongitude(u);

        for (int y = 0; y < _height; y++)
        {
          final double v = 1.0 - ((double) y / (_height - 1));

          final Angle latitude = _sector.getInnerPointLatitude(v);

          final int index = ((_height-1-y) * _width) + x;

          final double height = elevationData.getElevationAt(latitude, longitude);

          _buffer[index] = (float) height;

          if (!_hasNoData)
          {
            if ((height != height))
            {
              _hasNoData = true;
            }
          }
        }
      }
    }
  }
}