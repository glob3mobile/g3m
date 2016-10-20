package org.glob3.mobile.generated; 
//
//  PyramidTerrainElevationNode.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/20/16.
//
//

//
//  PyramidTerrainElevationNode.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/20/16.
//
//



//class PyramidTerrainElevationProvider;
//class TerrainElevationGrid;


public class PyramidTerrainElevationNode
{
  private java.util.ArrayList<PyramidTerrainElevationNode> getChildren(PyramidTerrainElevationProvider pyramidTerrainElevationProvider)
  {
    if (_children == null)
    {
      _children = new java.util.ArrayList<PyramidTerrainElevationNode>();
      for (int i = 0; i < 4; i++)
      {
        PyramidTerrainElevationNode child = pyramidTerrainElevationProvider.createNode(this, i);
        _children.add(child);
      }
    }
    return _children;
  }

  public final PyramidTerrainElevationNode _parent;
  public final int _childID;
  public final Sector _sector ;
  public final int _z;
  public final int _x;
  public final int _y;

  public TerrainElevationGrid _grid;
  public boolean _stickyGrid;

  public java.util.ArrayList<PyramidTerrainElevationNode> _children;

  public PyramidTerrainElevationNode(PyramidTerrainElevationNode parent, int childID, Sector sector, int z, int x, int y)
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
        PyramidTerrainElevationNode child = _children.get(i);
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
  
    java.util.ArrayList<PyramidTerrainElevationNode> children = getChildren(pyramidTerrainElevationProvider);
    for (int i = 0; i < children.size(); i++)
    {
      PyramidTerrainElevationNode child = children.get(i);
      if (child.insertGrid(z, x, y, grid, sticky, pyramidTerrainElevationProvider))
      {
        return true;
      }
    }
    return false;
  }

}