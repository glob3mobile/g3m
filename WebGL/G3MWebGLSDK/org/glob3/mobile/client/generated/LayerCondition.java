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
//class RenderContext;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class EventContext;

public abstract class LayerCondition
{
  public void dispose()
  {
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual boolean isAvailable(const RenderContext* rc, const Tile* tile) const = 0;
  public abstract boolean isAvailable(RenderContext rc, Tile tile);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual boolean isAvailable(const EventContext* ec, const Tile* tile) const = 0;
  public abstract boolean isAvailable(EventContext ec, Tile tile);

}