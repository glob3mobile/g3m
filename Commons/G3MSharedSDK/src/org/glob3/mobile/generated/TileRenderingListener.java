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

//  /**
//   The given Tile started to be rendered
//   */
//  virtual void startRendering(const Tile* tile) = 0;
//
//  /**
//   The given Tile stopped to be rendered
//   */
//  virtual void stopRendering(const Tile* tile) = 0;

  public abstract void changedTileRendering(java.util.ArrayList<Tile> tilesStartedRendering, java.util.ArrayList<Tile> tilesStoppedRendering);

}