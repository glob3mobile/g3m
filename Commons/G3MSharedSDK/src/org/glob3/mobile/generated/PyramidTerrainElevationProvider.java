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


public abstract class PyramidTerrainElevationProvider extends TerrainElevationProvider
{

  protected static class Node
  {
    private final Node _parent;
    private final int _childID;
    private final Sector _sector ;

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//    Node(Node parent, int childID, Sector sector);

    public void dispose()
    {
    
    }

    public final boolean insertGrid(TerrainElevationGrid grid, boolean sticky, int gridWidth, int gridHeight)
    {
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#error Diego at work!
    }

  }


  protected final void insertGrid(TerrainElevationGrid grid, boolean sticky)
  {
    for (int i = 0; i < _rootNodesCount; i++)
    {
      Node rootNode = _rootNodes.get(i);
      if (rootNode.insertGrid(grid, sticky, _gridWidth, _gridHeight))
      {
        return;
      }
    }
    throw new RuntimeException("can't insert grid");
  }


  private final int _rootNodesCount;
  private java.util.ArrayList<Node> _rootNodes = new java.util.ArrayList<Node>();

  private final int _gridWidth;
  private final int _gridHeight;

  protected abstract Node createNode(Node parent, int childID);

  protected PyramidTerrainElevationProvider(int rootNodesCount, int gridWidth, int gridHeight)
  {
     _rootNodesCount = rootNodesCount;
     _gridWidth = gridWidth;
     _gridHeight = gridHeight;
    for (int i = 0; i < _rootNodesCount; i++)
    {
      Node rootNode = createNode(null, i);
      _rootNodes.add(rootNode);
    }
  }

  public void dispose()
  {
    for (int i = 0; i < _rootNodesCount; i++)
    {
      Node rootNode = _rootNodes.get(i);
      if (rootNode != null)
         rootNode.dispose();
    }
    super.dispose();
  }



}