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



public abstract class QuadTreeVisitor extends Disposable
{
  public void dispose()
  {
  super.dispose();

  }

  public abstract boolean visitElement(Sector sector, Object element);

  public abstract void endVisit(boolean aborted);

}