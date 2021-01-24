package org.glob3.mobile.generated;
//
//  XPCPointSelectionListener.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez-Deck on 1/24/21.
//

//
//  XPCPointSelectionListener.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez-Deck on 1/24/21.
//



//class XPCPointCloud;
//class Vector3D;
//class Geodetic3D;


public abstract class XPCPointSelectionListener
{

  public void dispose()
  {
  }

  public abstract boolean onSelectedPoint(XPCPointCloud pointCloud, Vector3D cartesian, Geodetic3D geodetic, String treeID, String nodeID, int pointIndex, double distanceToRay);

}