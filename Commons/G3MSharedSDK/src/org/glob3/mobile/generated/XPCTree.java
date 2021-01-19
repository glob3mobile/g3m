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
  private final int _id;
  private XPCNode _rootNode;


  public XPCTree(int id, XPCNode rootNode)
  {
     _id = id;
     _rootNode = rootNode;
  
  }

  public void dispose()
  {
    if (_rootNode != null)
       _rootNode.dispose();
  }

  public final long render(XPCPointCloud pointCloud, G3MRenderContext rc, GLState glState, Frustum frustum, long nowInMS)
  {
  
    return (_rootNode == null) ? 0 : _rootNode.render(pointCloud, rc, glState, frustum, nowInMS);
  }

  public final Sector getSector()
  {
    return _rootNode.getSector();
  }

}