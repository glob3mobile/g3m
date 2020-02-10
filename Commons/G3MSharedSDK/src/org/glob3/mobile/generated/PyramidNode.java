package org.glob3.mobile.generated;
//
//  PyramidNode.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 10/20/16.
//
//

//
//  PyramidNode.hpp
//  G3M
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
      _childrenSize = 4;
      for (int i = 0; i < _childrenSize; i++)
      {
        PyramidNode child = _pyramidDEMProvider.createNode(this, i);
        _children.add(child);
      }
    }
    return _children;
  }

  private DEMGrid _grid;
//  bool _stickyGrid;

  private java.util.ArrayList<PyramidNode> _children;
  private int _childrenSize;

//  const PyramidNode*  _parent;
//  const size_t        _childID;
  private PyramidDEMProvider _pyramidDEMProvider;

  private java.util.ArrayList<DEMSubscription> _subscriptions;

  private final Geodetic2D _resolution ;

  public final Sector _sector ;
  public final int _z;
  public final int _x;
  public final int _y;

  public PyramidNode(PyramidNode parent, int childID, Sector sector, int z, int x, int y, PyramidDEMProvider pyramidDEMProvider)
  //_parent(parent),
  //_childID(childID),
  //_stickyGrid(false),
  {
     _sector = new Sector(sector);
     _resolution = new Geodetic2D(sector._deltaLatitude.div(pyramidDEMProvider._tileExtent._y), sector._deltaLongitude.div(pyramidDEMProvider._tileExtent._x));
     _z = z;
     _x = x;
     _y = y;
     _pyramidDEMProvider = pyramidDEMProvider;
     _grid = null;
     _children = null;
     _childrenSize = 0;
     _subscriptions = null;
  
  }

  public void dispose()
  {
    if (_grid != null)
    {
      _grid._release();
    }
  
    if (_children != null)
    {
      for (int i = 0; i < _childrenSize; i++)
      {
        PyramidNode child = _children.get(i);
        if (child != null)
           child.dispose();
      }
      _children = null;
    }
  
    if (_subscriptions != null)
    {
      final int subscriptionsSize = _subscriptions.size();
      for (int i = 0; i < subscriptionsSize; i++)
      {
        DEMSubscription subscription = _subscriptions.get(i);
        subscription._release();
      }
      _subscriptions = null;
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
  //      _stickyGrid = stickyGrid;
        return true;
      }
      return false;
    }
  
    java.util.ArrayList<PyramidNode> children = getChildren();
    for (int i = 0; i < _childrenSize; i++)
    {
      PyramidNode child = children.get(i);
      if (child.insertGrid(z, x, y, grid, stickyGrid))
      {
        return true;
      }
    }
    return false;
  }

  public final void addSubscription(DEMGrid grid, DEMSubscription subscription)
  {
    if (subscription._sector.touchesWith(_sector))
    {
      DEMGrid bestGrid = (_grid == null) ? grid : _grid;
  
      final boolean notEnoughResolution = (_resolution._latitude.greaterThan(subscription._resolution._latitude) || _resolution._longitude.greaterThan(subscription._resolution._longitude));
      if (notEnoughResolution)
      {
        java.util.ArrayList<PyramidNode> children = getChildren();
        for (int i = 0; i < _childrenSize; i++)
        {
          PyramidNode child = children.get(i);
          child.addSubscription(bestGrid, subscription);
        }
      }
      else
      {
        if (_subscriptions == null)
        {
          _subscriptions = new java.util.ArrayList<DEMSubscription>();
        }
        subscription._retain();
        _subscriptions.add(subscription);
        if (bestGrid != null)
        {
          DEMGrid selectedGrid = DEMGridUtils.bestGridFor(bestGrid, subscription._sector, subscription._extent);
          if (selectedGrid != null)
          {
            subscription.onGrid(selectedGrid);
          }
        }
      }
    }
  }

  public final void removeSubscription(DEMSubscription subscription)
  {
    if (_subscriptions != null)
    {
      final int subscriptionsSize = _subscriptions.size();
      for (int i = 0; i < subscriptionsSize; i++)
      {
        if (_subscriptions.get(i) == subscription)
        {
          subscription._release();
          _subscriptions.remove(i);
          break;
        }
      }
      if (_subscriptions.isEmpty())
      {
        _subscriptions = null;
        _subscriptions = null;
      }
    }
  
    if (_children != null)
    {
      for (int i = 0; i < _childrenSize; i++)
      {
        PyramidNode child = _children.get(i);
        child.removeSubscription(subscription);
      }
    }
  
  }

}
