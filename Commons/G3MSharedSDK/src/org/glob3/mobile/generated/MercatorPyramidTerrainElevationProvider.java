package org.glob3.mobile.generated; 
//
//  MercatorPyramidTerrainElevationProvider.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/19/16.
//
//

//
//  MercatorPyramidTerrainElevationProvider.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/19/16.
//
//



public abstract class MercatorPyramidTerrainElevationProvider extends PyramidTerrainElevationProvider
{
  protected final MercatorPyramidTerrainElevationProvider.Node createNode(MercatorPyramidTerrainElevationProvider.Node parent, int childID)
  {
    if (parent == null)
    {
      // creating root node
      return new Node(null, childID, Sector.FULL_SPHERE); // parent
    }
    throw new RuntimeException("Man at work!");
  }

  public MercatorPyramidTerrainElevationProvider(int nodeWidth, int nodeHeight)
  {
     super(1, nodeWidth, nodeHeight);
  }

}