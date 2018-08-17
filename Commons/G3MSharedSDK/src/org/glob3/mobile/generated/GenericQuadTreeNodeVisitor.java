package org.glob3.mobile.generated;import java.util.*;

//
//  GenericQuadTree.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 07/08/13.
//
//

//
//  GenericQuadTree.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 07/08/13.
//
//



//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class GenericQuadTree_Node;

public abstract class GenericQuadTreeNodeVisitor
{
  public void dispose()
  {
  }

  public abstract boolean visitNode(GenericQuadTree_Node node);
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual void endVisit(boolean aborted) const = 0;
  public abstract void endVisit(boolean aborted);
}
