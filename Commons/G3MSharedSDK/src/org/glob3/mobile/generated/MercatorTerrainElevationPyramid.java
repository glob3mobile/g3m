package org.glob3.mobile.generated; 
//
//  MercatorTerrainElevationPyramid.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/19/16.
//
//

//
//  MercatorTerrainElevationPyramid.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/19/16.
//
//



public class MercatorTerrainElevationPyramid extends TerrainElevationPyramid
{
  protected final TerrainElevationPyramid.Node createNode(TerrainElevationPyramid.Node parent, int childID)
  {
    if (parent == null)
    {
      // creating root node
      return new Node(null, childID, Sector.FULL_SPHERE); // parent
    }
    throw new RuntimeException("Man at work!");
  }

  public MercatorTerrainElevationPyramid()
  {
     super(1);
  }

}