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


//class Tile;
//class G3MRenderContext;
//class G3MEventContext;


public abstract class LayerCondition extends Disposable
{
  public void dispose()
  {
    JAVA_POST_DISPOSE
  }

  public abstract boolean isAvailable(G3MRenderContext rc, Tile tile);

  public abstract boolean isAvailable(G3MEventContext ec, Tile tile);

}