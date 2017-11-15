package org.glob3.mobile.generated; 
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


public abstract class LayerCondition
{
  public void dispose()
  {
  }

//  virtual bool isAvailable(const G3MRenderContext* rc,
//                           const Tile* tile) const = 0;
//  
//  virtual bool isAvailable(const G3MEventContext* ec,
//                           const Tile* tile) const = 0;

  public abstract boolean isAvailable(Tile tile);

  public abstract LayerCondition copy();

}