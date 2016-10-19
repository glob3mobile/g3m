package org.glob3.mobile.generated; 
//
//  TerrainElevationPyramid.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/19/16.
//
//

//
//  TerrainElevationPyramid.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/19/16.
//
//





public abstract class TerrainElevationPyramid
{

  protected static class Node
  {
    private final Node _parent;
    private final int _childID;
    private final Sector _sector ;

    public Node(TerrainElevationPyramid.Node parent, int childID, Sector sector)
    {
       _parent = parent;
       _childID = childID;
       _sector = new Sector(sector);
    
    }

    public void dispose()
    {
    
    }
  }


  private java.util.ArrayList<TerrainElevationPyramid.Node> _rootNodes = new java.util.ArrayList<TerrainElevationPyramid.Node>();


  protected abstract TerrainElevationPyramid.Node createNode(TerrainElevationPyramid.Node parent, int childID);

  protected TerrainElevationPyramid(int rootNodesCount)
  {
    for (int i = 0; i < rootNodesCount; i++)
    {
      Node rootNode = createNode(null, i);
      _rootNodes.add(rootNode);
    }
  }

  public void dispose()
  {
    final int size = _rootNodes.size();
    for (int i = 0; i < size; i++)
    {
      Node rootNode = _rootNodes.get(i);
      if (rootNode != null)
         rootNode.dispose();
    }
  }



}