package org.glob3.mobile.generated; 
//
//  LoDShape.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 29/12/14.
//
//



//class SGNode;
//class OrientedBox;
//class BoundingVolume;
//class SGShape;

public class LoDLevel
{


  public SGShape _sgShape; //DUE TO A TERRIBLE DEPENDENCY FOR INITIALIZATION
  public final SGNode _node;
  public final String _uriPrefix;
  public final double _perfectDistanceSquared;
  public final boolean _isTransparent;

  public OrientedBox _boundingVolume;

  public LoDLevel(SGNode node, String uriPrefix, double perfectDistance, boolean isTransparent)
  {
     _node = node;
     _uriPrefix = uriPrefix;
     _perfectDistanceSquared = perfectDistance * perfectDistance;
     _isTransparent = isTransparent;
     _boundingVolume = null;
     _sgShape = null;

  }

  public void dispose()
  {
    if (_node != null)
       _node.dispose();
    if (_boundingVolume != null)
       _boundingVolume.dispose();
    if (_sgShape != null)
       _sgShape.dispose();
  }
}