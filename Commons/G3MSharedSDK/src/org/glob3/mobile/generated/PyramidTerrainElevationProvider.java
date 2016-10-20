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




//class TerrainElevationGrid;
//class PyramidTerrainElevationNode;


public abstract class PyramidTerrainElevationProvider extends TerrainElevationProvider
{

  protected final void insertGrid(int z, int x, int y, TerrainElevationGrid grid, boolean sticky)
  {
    java.util.ArrayList<PyramidTerrainElevationNode> rootNodes = getRootNodes();
    for (int i = 0; i < _rootNodesCount; i++)
    {
      PyramidTerrainElevationNode rootNode = rootNodes.get(i);
      if (rootNode.insertGrid(z, x, y, grid, sticky, this))
      {
        return;
      }
    }
    throw new RuntimeException("can't insert grid");
  }

  private final int _rootNodesCount;
  private java.util.ArrayList<PyramidTerrainElevationNode> _rootNodes;


  private java.util.ArrayList<PyramidTerrainElevationNode> getRootNodes()
  {
    if (_rootNodes == null)
    {
      _rootNodes = new java.util.ArrayList<PyramidTerrainElevationNode>();
      for (int i = 0; i < _rootNodesCount; i++)
      {
        PyramidTerrainElevationNode rootNode = createNode(null, i);
        _rootNodes.add(rootNode);
      }
    }
    return _rootNodes;
  }

  protected PyramidTerrainElevationProvider(int rootNodesCount)
  {
     _rootNodesCount = rootNodesCount;
     _rootNodes = null;
  }

  public void dispose()
  {
    if (_rootNodes != null)
    {
      for (int i = 0; i < _rootNodesCount; i++)
      {
        PyramidTerrainElevationNode rootNode = _rootNodes.get(i);
        if (rootNode != null)
           rootNode.dispose();
      }
      _rootNodes = null;
    }
    super.dispose();
  }

  public abstract PyramidTerrainElevationNode createNode(PyramidTerrainElevationNode parent, int childID);

}