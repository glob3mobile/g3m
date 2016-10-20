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

  protected MercatorPyramidDEMProvider(double deltaHeight)
  {
     super(deltaHeight, 1);
  }

  public void dispose()
  {
    super.dispose();
  }


  public final PyramidDEMNode createNode(PyramidDEMNode parent, int childID)
  {
    if (parent == null)
    {
      // creating root node
      return new PyramidDEMNode(null, childID, Sector.FULL_SPHERE, 0, 0, 0); // y -  x -  z -  parent
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
      final Sector s0 = new Sector(new Geodetic2D(lower._latitude, lower._longitude), new Geodetic2D(splitLatitude, splitLongitude));
  
      return new PyramidDEMNode(parent, childID, s0, nextZ, x2, y2 + 1);
    }
    else if (childID == 1)
    {
      final Sector s1 = new Sector(new Geodetic2D(lower._latitude, splitLongitude), new Geodetic2D(splitLatitude, upper._longitude));
      return new PyramidDEMNode(parent, childID, s1, nextZ, x2 + 1, y2 + 1);
    }
    else if (childID == 2)
    {
      final Sector s2 = new Sector(new Geodetic2D(splitLatitude, lower._longitude), new Geodetic2D(upper._latitude, splitLongitude));
      return new PyramidDEMNode(parent, childID, s2, nextZ, x2, y2);
    }
    else if (childID == 3)
    {
      final Sector s3 = new Sector(new Geodetic2D(splitLatitude, splitLongitude), new Geodetic2D(upper._latitude, upper._longitude));
      return new PyramidDEMNode(parent, childID, s3, nextZ, x2 + 1, y2);
    }
    else
    {
      throw new RuntimeException("Man, isn't it a QuadTree?");
    }
  }

}