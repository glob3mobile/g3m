package org.glob3.mobile.generated; 
//
//  GenericQuadTree.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 07/08/13.
//
//

//
//  GenericQuadTree.h
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 07/08/13.
//
//




//class GenericQuadTree_Node;

public abstract class GenericQuadTreeNodeVisitor
{
  public void dispose()
  {
    JAVA_POST_DISPOSE
  }

  public abstract boolean visitNode(GenericQuadTree_Node node);
  public abstract void endVisit(boolean aborted);
}