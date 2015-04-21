package org.glob3.mobile.generated; 
//
//  QuadTree.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/10/13.
//
//

//
//  QuadTree.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/10/13.
//
//




//class QuadTree_Content;

public abstract class QuadTreeVisitor
{
  public void dispose()
  {
  }

  public abstract boolean visitElement(Sector sector, QuadTree_Content content);

  public abstract void endVisit(boolean aborted);

}