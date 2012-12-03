package org.glob3.mobile.generated; 
//
//  LayerCondition.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 23/08/12.
//
//

//
//  LayerCondition.h
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 23/08/12.
//
//


//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Tile;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class G3MRenderContext;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class G3MEventContext;

public abstract class LayerCondition
{
  public void dispose()
  {
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual boolean isAvailable(const G3MRenderContext* rc, const Tile* tile) const = 0;
  public abstract boolean isAvailable(G3MRenderContext rc, Tile tile);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual boolean isAvailable(const G3MEventContext* ec, const Tile* tile) const = 0;
  public abstract boolean isAvailable(G3MEventContext ec, Tile tile);

}