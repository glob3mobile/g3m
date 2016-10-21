package org.glob3.mobile.generated; 
//
//  PyramidNode.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/20/16.
//
//

//
//  PyramidNode.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/20/16.
//
//



//class PyramidDEMProvider;
//class DEMGrid;
//class DEMSubscription;


public class PyramidNode
{
  private java.util.ArrayList<PyramidNode> getChildren()
  {
    if (_children == null)
    {
      _children = new java.util.ArrayList<PyramidNode>();
      for (int i = 0; i < 4; i++)
      {
        PyramidNode child = _pyramidDEMProvider.createNode(this, i);
        _children.add(child);
      }
    }
    return _children;
  }

  private DEMGrid _grid;
  private boolean _stickyGrid;

  private java.util.ArrayList<PyramidNode> _children;

  private final PyramidNode _parent;
  private final int _childID;
  private PyramidDEMProvider _pyramidDEMProvider;

  public final Sector _sector ;
  public final int _z;
  public final int _x;
  public final int _y;

  public PyramidNode(PyramidNode parent, int childID, Sector sector, int z, int x, int y, PyramidDEMProvider pyramidDEMProvider)
  {
     _parent = parent;
     _childID = childID;
     _sector = new Sector(sector);
     _z = z;
     _x = x;
     _y = y;
     _pyramidDEMProvider = pyramidDEMProvider;
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
      final int size = _children.size();
      for (int i = 0; i < size; i++)
      {
        PyramidNode child = _children.get(i);
        if (child != null)
           child.dispose();
      }
      _children = null;
    }
  }

  public final boolean insertGrid(int z, int x, int y, DEMGrid grid, boolean stickyGrid)
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
        _stickyGrid = stickyGrid;
        return true;
      }
      return false;
    }
  
    java.util.ArrayList<PyramidNode> children = getChildren();
    final int size = children.size();
    for (int i = 0; i < size; i++)
    {
      PyramidNode child = children.get(i);
      if (child.insertGrid(z, x, y, grid, stickyGrid))
      {
        return true;
      }
    }
    return false;
  }

  public final boolean addSubscription(DEMSubscription subscription)
  {
    if (!subscription._sector.touchesWith(_sector))
    {
      return false;
    }
  
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#error Diego at work!
  }

}