package org.glob3.mobile.generated; 
//
//  PyramidTerrainElevationProvider.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/19/16.
//
//

//
//  PyramidTerrainElevationProvider.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/19/16.
//
//





public abstract class PyramidTerrainElevationProvider extends TerrainElevationProvider
{

  protected static class Node
  {
    private final Node _parent;
    private final int _childID;
    private final Sector _sector ;

    public Node(PyramidTerrainElevationProvider.Node parent, int childID, Sector sector)
    {
       _parent = parent;
       _childID = childID;
       _sector = new Sector(sector);
    
    }

    public void dispose()
    {
    
    }
  }


  private java.util.ArrayList<PyramidTerrainElevationProvider.Node> _rootNodes = new java.util.ArrayList<PyramidTerrainElevationProvider.Node>();

  private final int _nodeWidth;
  private final int _nodeHeight;

  protected abstract PyramidTerrainElevationProvider.Node createNode(PyramidTerrainElevationProvider.Node parent, int childID);

  protected PyramidTerrainElevationProvider(int rootNodesCount, int nodeWidth, int nodeHeight)
  {
     _nodeWidth = nodeWidth;
     _nodeHeight = nodeHeight;
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
    super.dispose();
  }



}