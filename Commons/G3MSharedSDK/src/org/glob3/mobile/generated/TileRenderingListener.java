package org.glob3.mobile.generated; 
//
//  TileRenderingListener.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 3/21/14.
//
//


//class Tile;

public abstract class TileRenderingListener
{
  public void dispose()
  {
  }

  /**
   The given Tile started to be rendered
   */
  public abstract void startRendering(Tile tile);

  /**
   The given Tile stoped to be rendered
   */
  public abstract void stopRendering(Tile tile);

}