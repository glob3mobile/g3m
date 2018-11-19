package org.glob3.mobile.generated;
//
//  MercatorPyramidDEMProvider.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/19/16.
//
//

//
//  MercatorPyramidDEMProvider.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/19/16.
//
//



public abstract class MercatorPyramidDEMProvider extends PyramidDEMProvider
{

  protected MercatorPyramidDEMProvider(double deltaHeight, Vector2S tileExtent)
  {
     super(deltaHeight, 1, tileExtent);
  }

  public void dispose()
  {
    super.dispose();
  }


  public final PyramidNode createNode(PyramidNode parent, int childID)
  {
    if (parent == null)
    {
      // creating root node
      return new PyramidNode(null, childID, Sector.FULL_SPHERE, 0, 0, 0, this); // z, x, y -  parent
    }
  
    final int nextZ = parent._z + 1;
    final int x2 = parent._x * 2;
    final int y2 = parent._y * 2;
  
    final Geodetic2D lower = parent._sector._lower;
    final Geodetic2D upper = parent._sector._upper;
    final Angle splitLongitude = Angle.midAngle(lower._longitude, upper._longitude);
    final Angle splitLatitude = MercatorUtils.calculateSplitLatitude(lower._latitude, upper._latitude);
  
    if (childID == 0)
    {
      final Sector sector = new Sector(lower, new Geodetic2D(splitLatitude, splitLongitude));
      return new PyramidNode(parent, childID, sector, nextZ, x2, y2 + 1, this);
    }
    else if (childID == 1)
    {
      final Sector sector = new Sector(new Geodetic2D(lower._latitude, splitLongitude), new Geodetic2D(splitLatitude, upper._longitude));
      return new PyramidNode(parent, childID, sector, nextZ, x2 + 1, y2 + 1, this);
    }
    else if (childID == 2)
    {
      final Sector sector = new Sector(new Geodetic2D(splitLatitude, lower._longitude), new Geodetic2D(upper._latitude, splitLongitude));
      return new PyramidNode(parent, childID, sector, nextZ, x2, y2, this);
    }
    else if (childID == 3)
    {
      final Sector sector = new Sector(new Geodetic2D(splitLatitude, splitLongitude), upper);
      return new PyramidNode(parent, childID, sector, nextZ, x2 + 1, y2, this);
    }
    else
    {
      throw new RuntimeException("Man, isn't it a QuadTree?");
    }
  }

}
