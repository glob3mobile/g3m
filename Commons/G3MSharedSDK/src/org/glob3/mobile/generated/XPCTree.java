package org.glob3.mobile.generated;
//
//  XPCTree.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez-Deck on 1/18/21.
//

//
//  XPCTree.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez-Deck on 1/18/21.
//



//class XPCNode;
//class XPCPointCloud;
//class G3MRenderContext;
//class GLState;
//class Frustum;
//class Sector;
//class XPCSelectionResult;
//class ITimer;
//class BoundingVolume;



public class XPCTree
{
  private final String _id;
  private XPCNode _rootNode;

  private XPCRenderingState _renderingState = new XPCRenderingState();


  public XPCTree(String id, XPCNode rootNode)
  {
     _id = id;
     _rootNode = rootNode;
  
  }

  public void dispose()
  {
    _rootNode.cancel();
    _rootNode._release();
  }

  public final Sector getSector()
  {
    return _rootNode.getSector();
  }

  public final double getMinHeight()
  {
    return _rootNode.getMinHeight();
  }

  public final double getMaxHeight()
  {
    return _rootNode.getMaxHeight();
  }

  public final long render(XPCPointCloud pointCloud, G3MRenderContext rc, ITimer lastSplitTimer, GLState glState, Frustum frustum, long nowInMS, boolean renderDebug, BoundingVolume fence)
  {
    if (_rootNode == null)
    {
      return 0;
    }
  
    _renderingState.reset();
  
    return _rootNode.render(pointCloud, _id, rc, lastSplitTimer, glState, frustum, nowInMS, renderDebug, _renderingState, fence);
  }

  public final boolean selectPoints(XPCSelectionResult selectionResult, XPCPointCloud pointCloud)
  {
    return (_rootNode != null) && _rootNode.selectPoints(selectionResult, pointCloud, _id);
  }

  public final void cancel()
  {
    _rootNode.cancel();
  }

  public final void reloadNodes()
  {
    _rootNode.reload();
  }

}