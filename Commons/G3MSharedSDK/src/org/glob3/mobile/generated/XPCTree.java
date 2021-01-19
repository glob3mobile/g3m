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


public class XPCTree
{
  private final String _id;
  private XPCNode _rootNode;


  public XPCTree(String id, XPCNode rootNode)
  {
     _id = id;
     _rootNode = rootNode;
  
  }

  public void dispose()
  {
    _rootNode._release();
  }

  public final long render(XPCPointCloud pointCloud, G3MRenderContext rc, GLState glState, Frustum frustum, long nowInMS)
  {
  
    return (_rootNode == null) ? 0 : _rootNode.render(pointCloud, _id, rc, glState, frustum, nowInMS);
  }

  public final Sector getSector()
  {
    return _rootNode.getSector();
  }

}