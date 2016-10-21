package org.glob3.mobile.generated; 
//
//  PyramidDEMProvider.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/19/16.
//
//

//
//  PyramidDEMProvider.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/19/16.
//
//




//class DEMGrid;
//class PyramidNode;


public abstract class PyramidDEMProvider extends DEMProvider
{

  private final int _rootNodesCount;
  private java.util.ArrayList<PyramidNode> _rootNodes;

  private java.util.ArrayList<PyramidNode> getRootNodes()
  {
    if (_rootNodes == null)
    {
      _rootNodes = new java.util.ArrayList<PyramidNode>();
      for (int i = 0; i < _rootNodesCount; i++)
      {
        PyramidNode rootNode = createNode(null, i);
        _rootNodes.add(rootNode);
      }
    }
    return _rootNodes;
  }


  protected final Vector2I _tileExtent;


  protected final void insertGrid(int z, int x, int y, DEMGrid grid, boolean stickyGrid)
  {
    java.util.ArrayList<PyramidNode> rootNodes = getRootNodes();
    for (int i = 0; i < _rootNodesCount; i++)
    {
      PyramidNode rootNode = rootNodes.get(i);
      if (rootNode.insertGrid(z, x, y, grid, stickyGrid))
      {
        return;
      }
    }
    throw new RuntimeException("can't insert grid");
  }

  protected PyramidDEMProvider(double deltaHeight, int rootNodesCount, Vector2I tileExtent)
  {
     super(deltaHeight);
     _rootNodesCount = rootNodesCount;
     _tileExtent = tileExtent;
     _rootNodes = null;
  }

  public void dispose()
  {
    if (_rootNodes != null)
    {
      for (int i = 0; i < _rootNodesCount; i++)
      {
        PyramidNode rootNode = _rootNodes.get(i);
        if (rootNode != null)
           rootNode.dispose();
      }
      _rootNodes = null;
    }
    super.dispose();
  }



  public abstract PyramidNode createNode(PyramidNode parent, int childID);

  public final long subscribe(Sector sector, Vector2I extent, DEMListener listener, boolean deleteListener)
  {
    DEMSubscription subscription = new DEMSubscription(sector, extent, listener, deleteListener);
  
    boolean holdSubscription = false;
    java.util.ArrayList<PyramidNode> rootNodes = getRootNodes();
    for (int i = 0; i < _rootNodesCount; i++)
    {
      PyramidNode rootNode = rootNodes.get(i);
      if (rootNode.addSubscription(subscription))
      {
        holdSubscription = true;
      }
    }
  
    if (holdSubscription)
    {
      return subscription._id;
    }
  
    if (subscription != null)
       subscription.dispose();
    return -1;
  }

  public final void unsubscribe(long subscriptionID)
  {
    if (subscriptionID < 0)
    {
      return;
    }
  
    if (_rootNodes != null)
    {
      DEMSubscription subscription = null;
  
      for (int i = 0; i < _rootNodesCount; i++)
      {
        PyramidNode rootNode = _rootNodes.get(i);
        DEMSubscription removedSubscription = rootNode.removeSubscription(subscriptionID);
        if (removedSubscription != null)
        {
          if (subscription == null)
          {
            subscription = removedSubscription;
          }
          else
          {
            if (subscription != removedSubscription)
            {
              throw new RuntimeException("Logic error!");
            }
          }
        }
      }
  
      if (subscription != null)
         subscription.dispose();
    }
  }

}