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
//class Ray;


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

  public final Sector getSector()
  {
    return _rootNode.getSector();
  }

  public final long render(XPCPointCloud pointCloud, G3MRenderContext rc, GLState glState, Frustum frustum, long nowInMS, boolean renderDebug)
  {
  
    return (_rootNode == null) ? 0 : _rootNode.render(pointCloud, _id, rc, glState, frustum, nowInMS, renderDebug);
  }

  public final boolean touchesRay(Ray ray)
  {
    return (_rootNode != null) && _rootNode.touchesRay(ray);
  }

}