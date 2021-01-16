package org.glob3.mobile.generated;
//
//  XPCMetadata.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez-Deck on 1/15/21.
//

//
//  XPCMetadata.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez-Deck on 1/15/21.
//



//class JSONObject;
//class XPCDimension;
//class XPCNode;
//class XPCPointCloud;
//class G3MRenderContext;
//class GLState;
//class Frustum;

public class XPCMetadata
{

  public static XPCMetadata fromJSON(JSONObject jsonObject)
  {
    if (jsonObject == null)
    {
      return null;
    }
  
    final java.util.ArrayList<XPCDimension> extraDimensions = XPCDimension.fromJSON(jsonObject.getAsArray("extraDimensions"));
  
    final java.util.ArrayList<XPCNode> rootNodes = XPCNode.fromJSON(jsonObject.getAsArray("rootNodes"));
  
    return new XPCMetadata(extraDimensions, rootNodes);
  }

  public void dispose()
  {
    for (int i = 0; i < _extraDimensions.size(); i++)
    {
      final XPCDimension extraDimension = _extraDimensions.get(i);
      if (extraDimension != null)
         extraDimension.dispose();
    }
  
    for (int i = 0; i < _rootNodes.size(); i++)
    {
      final XPCNode rootNode = _rootNodes.get(i);
      if (rootNode != null)
         rootNode.dispose();
    }
  }


  public final long render(XPCPointCloud pointCloud, G3MRenderContext rc, GLState glState, Frustum frustum, float pointSize, boolean dynamicPointSize, long nowInMS)
  {
  
    long renderedCount = 0;
  
    for (int i = 0; i < _rootNodesSize; i++)
    {
      XPCNode rootNode = _rootNodes.get(i);
  
      renderedCount += rootNode.render(this, rc, glState, frustum, pointSize, dynamicPointSize, nowInMS);
    }
  
    return renderedCount;
  }



  private final java.util.ArrayList<XPCDimension> _extraDimensions;
  private final java.util.ArrayList<XPCNode> _rootNodes;
  private final int _rootNodesSize;

  private XPCMetadata(java.util.ArrayList<XPCDimension> extraDimensions, java.util.ArrayList<XPCNode> rootNodes)
  {
     _extraDimensions = extraDimensions;
     _rootNodes = rootNodes;
     _rootNodesSize = _extraDimensions.size();
  
  }

}