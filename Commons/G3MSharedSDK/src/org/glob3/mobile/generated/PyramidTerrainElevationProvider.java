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
    private java.util.ArrayList<PyramidTerrainElevationProvider.Node> getChildren(PyramidTerrainElevationProvider pyramidTerrainElevationProvider)
    {
      if (_children == null)
      {
        _children = new java.util.ArrayList<Node>();
        for (int i = 0; i < 4; i++)
        {
          Node child = pyramidTerrainElevationProvider.createNode(this, i);
          _children.add(child);
        }
      }
      return _children;
    }

    public final Node _parent;
    public final int _childID;
    public final Sector _sector ;
    public final int _z;
    public final int _x;
    public final int _y;

    public TerrainElevationGrid _grid;
    public boolean _stickyGrid;

    public java.util.ArrayList<Node> _children;

    public Node(PyramidTerrainElevationProvider.Node parent, int childID, Sector sector, int z, int x, int y)
    {
       _parent = parent;
       _childID = childID;
       _sector = new Sector(sector);
       _z = z;
       _x = x;
       _y = y;
       _grid = null;
       _stickyGrid = false;
       _children = null;
    
    }

    public void dispose()
    {
      if (_grid != null)
      {
        _grid._release();
      }
    
      if (_children != null)
      {
        for (int i = 0; i < _children.size(); i++)
        {
          Node child = _children.get(i);
          if (child != null)
             child.dispose();
        }
        _children = null;
      }
    }

    public final boolean insertGrid(int z, int x, int y, TerrainElevationGrid grid, boolean sticky, PyramidTerrainElevationProvider pyramidTerrainElevationProvider)
    {
      if (z < _z)
      {
        throw new RuntimeException("Logic error!");
      }
      else if (z == _z)
      {
        if ((x == _x) && (y == _y))
        {
          _grid = grid;
          _stickyGrid = sticky;
          return true;
        }
        return false;
      }
    
      java.util.ArrayList<Node> children = getChildren(pyramidTerrainElevationProvider);
      for (int i = 0; i < children.size(); i++)
      {
        Node child = children.get(i);
        if (child.insertGrid(z, x, y, grid, sticky, pyramidTerrainElevationProvider))
        {
          return true;
        }
      }
      return false;
    }

  }


  protected final void insertGrid(int z, int x, int y, TerrainElevationGrid grid, boolean sticky)
  {
    java.util.ArrayList<Node> rootNodes = getRootNodes();
    for (int i = 0; i < _rootNodesCount; i++)
    {
      Node rootNode = rootNodes.get(i);
      if (rootNode.insertGrid(z, x, y, grid, sticky, this))
      {
        return;
      }
    }
    throw new RuntimeException("can't insert grid");
  }

  private final int _rootNodesCount;
  private java.util.ArrayList<Node> _rootNodes;


  private java.util.ArrayList<PyramidTerrainElevationProvider.Node> getRootNodes()
  {
    if (_rootNodes == null)
    {
      _rootNodes = new java.util.ArrayList<Node>();
      for (int i = 0; i < _rootNodesCount; i++)
      {
        Node rootNode = createNode(null, i);
        _rootNodes.add(rootNode);
      }
    }
    return _rootNodes;
  }

  protected abstract Node createNode(Node parent, int childID);

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
        Node rootNode = _rootNodes.get(i);
        if (rootNode != null)
           rootNode.dispose();
      }
      _rootNodes = null;
    }
    super.dispose();
  }


}