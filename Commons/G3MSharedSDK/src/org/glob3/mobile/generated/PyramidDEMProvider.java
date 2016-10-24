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

  protected PyramidDEMProvider(double deltaHeight, int rootNodesCount, Vector2S tileExtent)
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


  protected final Vector2S _tileExtent;

  public abstract PyramidNode createNode(PyramidNode parent, int childID);

  public final DEMSubscription subscribe(Sector sector, Vector2S extent, DEMListener listener, boolean deleteListener)
  {
    DEMSubscription subscription = new DEMSubscription(this, sector, extent, listener, deleteListener);
  
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
  
    subscription._release();
  
    if (holdSubscription)
    {
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#warning TODO: fire event!
      return subscription;
    }
  
    return null;
  }

  public final void unsubscribe(DEMSubscription subscription)
  {
    throw new RuntimeException("Not yet done!");
    if (subscription == null)
    {
      return;
    }
  
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#warning Diego at work!
    //  if (_rootNodes != NULL) {
    //    DEMSubscription* subscription = NULL;
    //
    //    for (size_t i = 0; i < _rootNodesCount; i++) {
    //      PyramidNode* rootNode = _rootNodes->at(i);
    //      DEMSubscription* removedSubscription = rootNode->removeSubscription(subscriptionID);
    //      if (removedSubscription != NULL) {
    //        if (subscription == NULL) {
    //          subscription = removedSubscription;
    //        }
    //        else {
    //          if (subscription != removedSubscription) {
    //            THROW_EXCEPTION("Logic error!");
    //          }
    //        }
    //      }
    //    }
    //
    //    delete subscription;
    //  }
  }

}