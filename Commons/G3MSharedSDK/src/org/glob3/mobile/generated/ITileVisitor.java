package org.glob3.mobile.generated;//
//  ITileVisitor.hpp
//  G3MiOSSDK
//
//  Created by Vidal on 1/22/13.
//
//


//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Layer;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Tile;

public abstract class ITileVisitor
{
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
  public void dispose()
  {
  }
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  void dispose();
//#endif

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual void visitTile(java.util.ArrayList<Layer*>& layers, const Tile* tile) const = 0;
  public abstract void visitTile(tangible.RefObject<java.util.ArrayList<Layer>> layers, Tile tile);
}
