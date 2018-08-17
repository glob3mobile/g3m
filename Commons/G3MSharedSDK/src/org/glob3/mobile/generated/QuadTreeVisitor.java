package org.glob3.mobile.generated;import java.util.*;

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




//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class QuadTree_Content;

public abstract class QuadTreeVisitor
{
  public void dispose()
  {
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual boolean visitElement(const Sector& sector, const QuadTree_Content* content) const = 0;
  public abstract boolean visitElement(Sector sector, QuadTree_Content content);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual void endVisit(boolean aborted) const = 0;
  public abstract void endVisit(boolean aborted);

}
